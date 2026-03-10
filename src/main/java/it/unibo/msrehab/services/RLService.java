package it.unibo.msrehab.services;


import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import it.unibo.msrehab.model.controller.FitnessWeightController;
import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.rl.impl.NextLevelAgent;
import it.unibo.msrehab.rl.impl.TemplateNextLevelAgent;
import it.unibo.msrehab.rl.rewardmodel.RewardModel;
import it.unibo.msrehab.rl.impl.*;
import it.unibo.msrehab.rl.model.*;
import it.unibo.msrehab.rl.utils.Maps;
import it.unibo.msrehab.rl.utils.Tuple;
import it.unibo.msrehab.rl.utils.XYFunction;
import it.unibo.msrehab.rl.utils.regression.LinearRegression;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@Controller
public class RLService
{
    IModel model = JPAController.getInstance();
    JSONDeserializer<List<Map<String, String>>> dataDeserializer = new JSONDeserializer<>();
    JSONSerializer serializer = new JSONSerializer();

    FitnessWeightController fitnessController = new FitnessWeightController();

    @RequestMapping(value = "/rlconfig", method = RequestMethod.GET)
    public ModelAndView rlConfigPage()
    {
        LevelAgentConfig levelAgentConfig = LevelAgentConfig.getSingletonEntity(model);
        ThresholdAgentConfig thresholdAgentConfig = ThresholdAgentConfig.getSingletonEntity(model);
        return new ModelAndView("rlconfig")
                .addObject("levelAgentConfig", levelAgentConfig)
                .addObject("thresholdAgentConfig", thresholdAgentConfig)
                .addObject("maxLevel", 10)
                ;
    }

    @RequestMapping(value = "/rl-threshold-agent-config", method = {RequestMethod.POST})
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



        if(!config.isValid())
            return ResponseEntity.badRequest().body("Invalid configuration");


        boolean updateSuccess = ThresholdAgentConfig.putSingletonEntity(model, config);


        if(!updateSuccess)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating configuration");

        return ResponseEntity.ok().body("Configuration updated successfully");
    }

    @RequestMapping(value = "/rl-level-agent-config", method = {RequestMethod.POST})
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



        if(!config.isValid())
            return ResponseEntity.badRequest().body("Invalid configuration");


        boolean updateSuccess = LevelAgentConfig.putSingletonEntity(model, config);


        if(!updateSuccess)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating configuration");

        return ResponseEntity.ok().body("Configuration updated successfully");
    }


    @RequestMapping(value = "/nextlevel", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpEntity<String> simulateStep(
            @RequestParam(required = false, defaultValue = "threshold") String simulatedAgent,
            @RequestParam(required = true) int currentLevel,
            @RequestParam(required = true) int maxLevel,
            @RequestParam(required = true) Optional<String> performanceData
    )
    {
        List<Tuple<Integer, Double>> levels = performanceData.map(json -> dataDeserializer.deserialize(json))
                .orElse(new ArrayList<>())
                .stream()
                .map(data -> Tuple.of(
                                Integer.valueOf(String.valueOf(data.get("level"))),
                                Double.valueOf(String.valueOf(data.get("performance")))
                        )
                )
                .collect(Collectors.toList());
        NextLevelAgent agent;
        if(Objects.equals(simulatedAgent, "level"))
            agent = createLevelAgent(maxLevel, levels);
        else if(Objects.equals(simulatedAgent, "threshold"))
            agent = createThresholdAgent(maxLevel, levels);
        else
            agent = createIncrementalAgent(maxLevel, levels);

        int nextLevel = agent.getNextLevel(currentLevel);

        return ResponseEntity.ok().body(
                serializer.deepSerialize(
                        Maps.of(
                                "nextLevel", nextLevel,
                                "stateValues", ValueIterationValues.getValuesString()
                        )
                )
        );

    }

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
                                (tp, h) -> {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
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
                                (tp, h) -> {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
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

        IModel model = JPAController.getInstance();


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
                                (tp, h) -> {
                                    int lvl = tp.first;
                                    double p = tp.second;
                                    h.setExid(123456789);
                                    h.setUserid(123456789);
                                    h.setAbsperformance(p);
                                    h.setRelperformance(p);
                                    h.setLevel(lvl);
                                    h.setPassed(p >= 0.8);
                                    h.setTimestamp(System.currentTimeMillis() + counter.getAndIncrement());
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
                                                                (from ,action, to ) ->
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
}
