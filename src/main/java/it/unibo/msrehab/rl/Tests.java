package it.unibo.msrehab.rl;


import it.unibo.msrehab.rl.rewardmodel.RewardModel;
import it.unibo.msrehab.rl.common.StateTransition;
import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.model.JPAController;
import it.unibo.msrehab.rl.utils.regression.SigmoidRegressor;
import it.unibo.msrehab.rl.utils.rv.RandomVariableFactory;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Tests
{
    static Random random = new Random();
    static IModel db = JPAController.getInstance();

    public static void main(String[] args)
    {
        testRegression();
    }

    private static void testRegression()
    {
        int minLevel = 1;
        int maxLevel = 12;

        Function<Integer, Double> rewardFunction = lvl -> RandomVariableFactory.sigmoidBernoulli(1, 3, lvl).sample() ? 1d : 0d;

        RewardModel<Integer, Integer> model = RewardModel.createRegressionRewardModel(
                (from, action, to) -> Double.valueOf(to),
                new SigmoidRegressor(1, (maxLevel - minLevel) / 2d).setLrDampenFactor(0.99)
        );

        int n = 1000;
        model.fit(
                IntStream.range(0, n)
                .mapToObj(i -> {
                    int level = minLevel + random.nextInt(maxLevel - minLevel);
                    double reward = rewardFunction.apply(level);
                    return new StateTransition.Builder<Integer, Integer>()
                            .setFromState(level)
                            .setToState(level)
                            .setAction(0)
                            .setReward(reward)
                            .build();
                })
                .collect(Collectors.toList())
        );



        List<Double> predictions = IntStream.range(minLevel, maxLevel)
                .boxed()
                .map(lvl -> model.getReward(lvl, 0, lvl))
                .collect(Collectors.toList());

    }


}
