package it.unibo.msrehab.rl.impl;

import it.unibo.msrehab.model.entities.*;
import it.unibo.msrehab.rl.rewardmodel.RewardModel;
import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.model.JPAController;
import it.unibo.msrehab.rl.model.LevelAgentConfig;
import it.unibo.msrehab.rl.model.ThresholdAgentConfig;
import it.unibo.msrehab.rl.utils.Maps;
import it.unibo.msrehab.rl.utils.XYFunction;
import it.unibo.msrehab.rl.utils.regression.LinearRegression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;


public enum RLAgents
{
    LEVEL_AGENT(
            (exercise, user) -> {
                IModel model = JPAController.getInstance();
                LevelAgentConfig config = LevelAgentConfig.getSingletonEntity(model);
                LevelAgentConfig.PriorModelType priorType = config.getPriorModelType();

                int maxLevel = exercise.getMaxLevel();
                double targetPerformance = config.getTargetPerformance();
                double slope = config.getPriorSlope();
                double intercept = config.getPriorIntercept();
                double priorWeight = config.getPriorWeight();
                double discountFactor = config.getDiscountFactor();
                double policyEpsilon = config.getPolicyEpsilon();


                return new TemplateNextLevelAgent()
                        .setMinMaxLevel(1, maxLevel)
                        .setEpisodeSupplier(
                                EpisodesSupplier.modelBasedGroupByUserExerciseDay(model, exercise, user)
                        )
                        .setEpisodeTransitionAdapter(
                                StateTransitionAdapter.onlyArrivalState(History::getAbsperformance)
                        )
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
                                                                1 - priorWeight
                                                                ,
                                                                RewardModel.<Integer, Integer>createLambdaRewardModel(
                                                                        (from ,action, to ) ->
                                                                                Optional.of(priorType)
                                                                                        .filter(pt -> pt == LevelAgentConfig.PriorModelType.sigmoid)
                                                                                        .map(pt -> XYFunction.sigmoid(slope, intercept * maxLevel))
                                                                                        .orElse(XYFunction.linear(slope / maxLevel, intercept).clamp(0, 1))
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
    ),
    INCREMENTAL_AGENT(
            (exercise, user) -> {
                IModel model = JPAController.getInstance();

                double passThreshold = model.getEntityController(FitnessWeight.class)
                        .findEntity(fw -> Objects.equals(fw.getExid(), exercise.getId()))
                        .map(FitnessWeight::getThr)
                        .orElse(0.8)
                        ;
                return new TemplateNextLevelAgent()
                        .setMinMaxLevel(1, exercise.getMaxLevel())
                        .setEpisodeSupplier(
                                EpisodesSupplier.modelBasedGroupByUserExerciseDay(model, exercise, user)
                                        .onlyForToday()
                                        .onlyNotEmpty()
                        )
                        .setEpisodeTransitionAdapter(
                                StateTransitionAdapter.onlyArrivalState(History::getAbsperformance)
                        )
                        .setAgent(
                                NextLevelAgentFactory.incrementalAgent(passThreshold)
                        )
                        ;
            }
    ),

    THRESHOLD_AGENT(
            ((exercise, user) -> {
                IModel model = JPAController.getInstance();

                ThresholdAgentConfig config = ThresholdAgentConfig.getSingletonEntity(model);

                double lowerThreshold = config.getLowerThreshold();
                double startThreshold = config.getStartThreshold();
                double thresholdDeltaPassed = config.getThresholdDeltaPassed();
                double thresholdDeltaNotPassed = config.getThresholdDeltaNotPassed();


                return new TemplateNextLevelAgent()
                        .setMinMaxLevel(1, exercise.getMaxLevel())
                        .setEpisodeSupplier(
                                EpisodesSupplier.modelBasedGroupByUserExerciseDay(model, exercise, user)
                                    .onlyNotEmpty()
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
                                        exercise.getMaxLevel()
                                )
                        )
                        ;
            })
    );

    private final BiFunction<Exercise, MSRUser, NextLevelAgent> agentFactoryMethod;


    RLAgents(BiFunction<Exercise, MSRUser, NextLevelAgent> agentFactoryMethod)
    {
        this.agentFactoryMethod = agentFactoryMethod;
    }


    public NextLevelAgent createAgent(Exercise exercise, MSRUser user)
    {
        return agentFactoryMethod.apply(exercise, user);
    }
}
