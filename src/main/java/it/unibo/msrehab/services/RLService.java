package it.unibo.msrehab.services;


import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.model.levelselection.LevelThreshold;
import it.unibo.msrehab.model.levelselection.NextLevelStrategy;
import it.unibo.msrehab.model.levelselection.HistoryRecord;
import it.unibo.msrehab.model.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Service per la simulazione degli agenti dall'interfaccia admin.
 */
@Controller
public class RLService
{
    Model model = JPAModel.getInstance();
    JSONDeserializer<List<Map<String, String>>> dataDeserializer = new JSONDeserializer<>();
    JSONSerializer serializer = new JSONSerializer();


    @RequestMapping(value = "/rlconfig", method = RequestMethod.GET)
    public ModelAndView rlConfigPage()
    {
        LevelAgentConfig levelAgentConfig = LevelAgentConfig.getSingletonEntity(model);
        ThresholdAgentConfig thresholdAgentConfig = ThresholdAgentConfig.getSingletonEntity(model);
        return new ModelAndView("rlconfig")
                .addObject("levelAgentConfig", levelAgentConfig)
                .addObject("thresholdAgentConfig", thresholdAgentConfig)
                .addObject("deltaInferiorLevel", thresholdAgentConfig.getStartThreshold() - thresholdAgentConfig.getLowerThreshold())
                .addObject("maxLevel", 10)
                ;
    }

    @RequestMapping(value = "adaptive-agent-config", method = {RequestMethod.POST})
    public HttpEntity<String> thresholdAgentConfig(
            @RequestParam(required = false, defaultValue = "0.2") Double deltaInferiorLevel,
            @RequestParam(required = false, defaultValue = "0.8") Double startThreshold,
            @RequestParam(required = false, defaultValue = "0.03") Double thresholdDeltaPassed,
            @RequestParam(required = false, defaultValue = "-0.07") Double thresholdDeltaNotPassed
    )
    {
        ThresholdAgentConfig config = ThresholdAgentConfig.getSingletonEntity(model);

        config.setLowerThreshold(startThreshold - deltaInferiorLevel);
        config.setStartThreshold(startThreshold);
        config.setThresholdDeltaPassed(thresholdDeltaPassed);
        config.setThresholdDeltaNotPassed(thresholdDeltaNotPassed);


        if (!config.isValid())
            return ResponseEntity.badRequest().body("Invalid configuration");


        boolean updateSuccess = ThresholdAgentConfig.putSingletonEntity(model, config);


        if (!updateSuccess)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating configuration");

        return ResponseEntity.ok().body("Configuration updated successfully");
    }

    @RequestMapping(value = "/rl-agent-config", method = {RequestMethod.POST})
    public HttpEntity<String> levelAgentConfig(
            @RequestParam Optional<Double> targetPerformance,
            @RequestParam Optional<Double> priorWeight,
            @RequestParam Optional<LevelAgentConfig.PriorModelType> priorType,
            @RequestParam Optional<Double> priorModelSlope,
            @RequestParam Optional<Double> priorModelIntercept,
            @RequestParam Optional<Double> discountFactor,
            @RequestParam Optional<Double> policyEpsilon
    )
    {
        LevelAgentConfig config = LevelAgentConfig.getSingletonEntity(model);

        targetPerformance.ifPresent(config::setTargetPerformance);
        priorWeight.ifPresent(config::setPriorWeight);
        priorType.ifPresent(config::setPriorModelType);
        priorModelSlope.ifPresent(config::setPriorSlope);
        priorModelIntercept.ifPresent(config::setPriorIntercept);
        discountFactor.ifPresent(config::setDiscountFactor);
        policyEpsilon.map(eps -> Math.max(0, Math.min(1, eps))).ifPresent(config::setPolicyEpsilon);


        if (!config.isValid())
            return ResponseEntity.badRequest().body("Invalid configuration");


        boolean updateSuccess = LevelAgentConfig.putSingletonEntity(model, config);


        if (!updateSuccess)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating configuration");

        return ResponseEntity.ok().body("Configuration updated successfully");
    }

    private static final double PASS_THRESHOLD = 0.8;

    public enum SimulationType
    {
        adaptive,
        incremental
    }


    @RequestMapping(value = "/nextlevel", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpEntity<String> simulateStep(
            @RequestParam SimulationType simulatedAgent,
            @RequestParam int maxLevel,
            @RequestParam String performanceData
    )
    {

        List<Map<String, String>> data = dataDeserializer.deserialize(performanceData);

        List<HistoryRecord> levels = IntStream.range(0, data.size())
                .mapToObj(i -> {
                    Map<String, String> d = data.get(i);
                    return new HistoryRecord(
                            Integer.parseInt(String.valueOf(d.get("level"))),
                            Double.parseDouble(String.valueOf(d.get("performance"))),
                            Double.parseDouble(String.valueOf(d.get("threshold"))),
                            i
                    );
                })
                .collect(Collectors.toList());

        NextLevelStrategy strategies = new NextLevelStrategy();

        LevelThreshold levelThreshold;
        switch (simulatedAgent)
        {
            case adaptive:
                ThresholdAgentConfig config = ThresholdAgentConfig.getSingletonEntity(model);
                levelThreshold = strategies.getNextLevelAdaptively(
                        config.getLowerThreshold(),
                        config.getStartThreshold(),
                        config.getThresholdDeltaPassed(),
                        config.getThresholdDeltaNotPassed(),
                        maxLevel,
                        levels
                );
                break;
            default: // incremental
                levelThreshold = strategies.getNextLevelIncrementally(
                        PASS_THRESHOLD,
                        maxLevel,
                        levels
                );

        }

        levelThreshold = new LevelThreshold(
                Math.min(maxLevel, levelThreshold.getLevel()),
                Math.max(0, Math.min(1, levelThreshold.getThreshold()))
        );

        Map<String, Object> body = new HashMap<>();
        body.put("nextLevel", levelThreshold.getLevel());
        body.put("nextThreshold", levelThreshold.getThreshold());

        return ResponseEntity.ok().body(
                serializer.deepSerialize(body)
        );

    }

    /*
    private NextLevelAgent createThresholdAgent(int maxLevel, List<Tuple<Integer, Double>> performanceData)
    {
        ThresholdAgentConfig config = ThresholdAgentConfig.getSingletonEntity(model);

        double lowerThreshold = config.getLowerThreshold();
        double startThreshold = config.getStartThreshold();
        double thresholdDeltaPassed = config.getThresholdDeltaPassed();
        double thresholdDeltaNotPassed = config.getThresholdDeltaNotPassed();

        AtomicLong counter = new AtomicLong(0);
        return new TemplateNextLevelAgent()
                .setMinMaxLevel(1, maxLevel)
                .setEpisodeSupplier(
                        EpisodesSupplier.createSingleEpisode(
                                performanceData,
                                (tp, h) ->
                                {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setCreateTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
                                }
                        )
                )
                .setEpisodeTransitionAdapter(
                        StateTransitionAdapter.onlyArrivalState(History::getAbsperformance)
                )
                .setAgent(
                        NextLevelAgentFactory.thresholdAgent(
                                lowerThreshold,
                                startThreshold,
                                thresholdDeltaPassed,
                                thresholdDeltaNotPassed,
                                1,
                                maxLevel
                        )
                )
                ;
    }

    private NextLevelAgent createIncrementalAgent(int maxLevel, List<Tuple<Integer, Double>> performanceData)
    {
        double passThreshold = 0.8;
        AtomicLong counter = new AtomicLong(0);
        return new TemplateNextLevelAgent()
                .setMinMaxLevel(1, maxLevel)
                .setEpisodeSupplier(
                        EpisodesSupplier.createSingleEpisode(
                                performanceData,
                                (tp, h) ->
                                {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setCreateTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
                                }
                        )
                )
                .setEpisodeTransitionAdapter(
                        StateTransitionAdapter.onlyArrivalState(History::getAbsperformance)
                )
                .setAgent(
                        NextLevelAgentFactory.incrementalAgent(passThreshold)
                );
    }


    private NextLevelAgent createLevelAgent(
            int maxLevel,
            List<Tuple<Integer, Double>> performanceData
    )
    {

        Model model = JPAModel.getInstance();


        LevelAgentConfig config = LevelAgentConfig.getSingletonEntity(model);
        double targetPerformance = config.getTargetPerformance();
        double slope = config.getPriorSlope();
        double intercept = config.getPriorIntercept();
        double priorWeight = config.getPriorWeight();
        double discountFactor = config.getDiscountFactor();
        double policyEpsilon = config.getPolicyEpsilon();
        LevelAgentConfig.PriorModelType priorType = config.getPriorModelType();

        AtomicLong counter = new AtomicLong(0);

        return new TemplateNextLevelAgent()
                .setMinMaxLevel(1, maxLevel)
                .setEpisodeSupplier(
                        EpisodesSupplier.createSingleEpisode(
                                performanceData.subList(Math.max(0, performanceData.size() - 5), performanceData.size()),
                                (tp, h) ->
                                {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setCreateTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
                                }
                        )
                )
                .setEpisodeTransitionAdapter(StateTransitionAdapter.onlyArrivalState(History::getAbsperformance))
                .setAgent(
                        NextLevelAgentFactory.valueIterationAgent(
                                1,
                                maxLevel,
                                discountFactor,
                                policyEpsilon,
                                RewardModel.createMixtureRewardModel(
                                                Maps.of(
                                                        RewardModel.<Integer, Integer>createRegressionRewardModel(
                                                                (from, action, to) -> to.doubleValue(),
                                                                new LinearRegression(0, 0.5)
                                                        ).clampReward(0, 1),
                                                        1 - priorWeight,
                                                        RewardModel.<Integer, Integer>createLambdaRewardModel(
                                                                (from, action, to) ->
                                                                        Optional.of(priorType)
                                                                                .filter(pt -> pt == LevelAgentConfig.PriorModelType.sigmoid)
                                                                                .map(pt -> XYFunction.sigmoid(slope, intercept * maxLevel))
                                                                                .orElse(XYFunction.linear(slope / maxLevel, intercept))
                                                                                .apply(to.doubleValue())
                                                        ).clampReward(0, 1),
                                                        priorWeight
                                                )
                                        )
                                        .clampReward(0, 1)
                                        .applyToRewards(reward -> 1 - Math.abs(targetPerformance - reward))

                        )
                )
                ;
    }
    */

}
