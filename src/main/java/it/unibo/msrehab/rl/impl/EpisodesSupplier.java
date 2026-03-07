package it.unibo.msrehab.rl.impl;


import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.History;
import it.unibo.msrehab.model.entities.MSRUser;
import it.unibo.msrehab.rl.model.IModel;
import it.unibo.msrehab.rl.utils.rv.IRandomVariable;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface EpisodesSupplier extends Supplier<List<List<History>>>
{

    /**
     * Sort the episodes supplied by the current instance using the specified comparator.
     *
     * @param comparator
     * @return
     */
    default EpisodesSupplier sorted(Comparator<List<History>> comparator)
    {
        return () -> get().stream()
                .sorted(comparator)
                .collect(Collectors.toList())
                ;
    }

    /**
     * Sort each episode by the specified comparator.
     *
     * @param comparator
     * @return
     */
    default EpisodesSupplier sortEpisodes(Comparator<History> comparator)
    {
        return () -> get().stream()
                .map(episode -> episode.stream().sorted(comparator).collect(Collectors.toList()))
                .collect(Collectors.toList())
                ;
    }

    /**
     * Filters the episodes supplied by the current instance using a specified predicate.
     * The predicate determines whether an episode should be included or excluded.
     *
     * @param filter a predicate to apply to each episode to determine if it should be included
     * @return a new EpisodesSupplier instance that supplies only the filtered lists of histories
     */
    default EpisodesSupplier filter(Predicate<List<History>> filter)
    {
        return () -> get()
                .stream()
                .filter(filter)
                .collect(Collectors.toList())
                ;
    }


    /**
     * Filters the histories within the episodes supplied by the current instance using a specified bi-predicate.
     * The bi-predicate determines whether each history within an episode should be included or excluded.
     *
     * @param filter a bi-predicate to apply to each episode and its corresponding individual histories to determine inclusion
     * @return a new EpisodesSupplier instance that supplies episodes containing only the filtered histories
     */
    default EpisodesSupplier filterHistories(BiPredicate<List<History>, History> filter)
    {
        return () -> get().stream()
                .map(episode -> episode.stream().filter(h -> filter.test(episode, h)).collect(Collectors.toList()))
                .collect(Collectors.toList())
                ;
    }

    /**
     * Filters the episodes supplied by the current instance, retaining only histories that have been marked as passed.
     * Subsequently filters out any episodes that are empty after applying the passed filter.
     *
     * @param passed true to retain only histories marked as passed, false to retain only histories marked as failed
     * @return a new EpisodesSupplier instance that supplies only non-empty episodes containing histories marked as passed
     */
    default EpisodesSupplier onlyPassedHistories(boolean passed)
    {
        return filterHistories((ep, h) -> Objects.equals(h.getPassed(), passed))
                .onlyNotEmpty()
                ;
    }

    /**
     * Filters out empty episodes.
     *
     * @return a new EpisodesSupplier instance that only supplies non-empty histories
     */
    default EpisodesSupplier onlyNotEmpty()
    {
        return filter(episode -> !episode.isEmpty());
    }

    /**
     * Filters out histories from the original supplier that are not from today.
     * @return
     */
    default EpisodesSupplier onlyForToday()
    {
        long today = epochMillisToEpochDay(System.currentTimeMillis());
        return filterHistories((ep, h) -> epochMillisToEpochDay(h.getTimestamp()) == today)
                .onlyNotEmpty()
                ;
    }

    /**
     * For each episode take only the last n histories.
     *
     * @param n tail size
     * @return
     */
    default EpisodesSupplier tailEpisodes(int n)
    {
        return filterHistories((ep, h) -> ep.indexOf(h) >= ep.size() - n - 1);
    }







    // Factory methods





    static EpisodesSupplier empty()
    {
        return ArrayList::new;
    }

    static EpisodesSupplier emptyEpisodes(int nEpisodes)
    {
        return () -> IntStream.range(0, nEpisodes)
                .mapToObj(i -> Collections.<History>emptyList())
                .collect(Collectors.toList());
    }


    static <T> EpisodesSupplier createSingleEpisode(Collection<T> data, BiConsumer<T, History> historyBuilder)
    {
        return () ->
        {
            List<History> episode = data.stream()
                    .map(datum -> {
                        History h = new History();
                        historyBuilder.accept(datum, h);
                        return h;
                    })
                    .collect(Collectors.toList())
                    ;
            List<List<History>> episodes = new ArrayList<>(1);
            episodes.add(episode);
            return episodes;
        };
    }

    static EpisodesSupplier simulatedEpisodes(
            int numEpisodes,
            int minLevel,
            int maxLevel,
            Function<Integer, IRandomVariable<Double>> rewardRv,
            Function<Double, Boolean> passedFunction
    )
    {
        if(minLevel > maxLevel)
            throw new IllegalArgumentException("minLevel must be less than maxLevel");

        return () -> Stream.iterate(0, i -> i + 1)
                .limit(numEpisodes)
                .map( i -> IntStream.range(minLevel, maxLevel + 1)
                        .mapToObj(level -> {
                            IRandomVariable<Double> rewardRvForLevel = rewardRv.apply(level);
                            if(rewardRvForLevel == null)
                                return null;

                            History h = new History();
                            h.setLevel(level);
                            h.setDifficulty("generated");
                            h.setTimestamp(System.currentTimeMillis());
                            double reward = rewardRvForLevel.sample();
                            h.setAbsperformance(reward);
                            h.setAbsperformance(reward);
                            h.setPassed(passedFunction.apply(reward));
                            h.setRelperformance(h.getAbsperformance());
                            return h;
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }



    static EpisodesSupplier modelBased(IModel model, Predicate<History> filter, Function<History, Object> episodeKeyExtractor)
    {
        return () -> model.getEntityController(History.class)
                .getAllEntities()
                .stream()
                .filter(h -> filter.test(h) && h.isSolved())
                .sorted(Comparator.comparing(History::getTimestamp))
                .collect(Collectors.groupingBy(episodeKeyExtractor, Collectors.toList()))
                .values()
                .stream()
                .filter(ep -> !ep.isEmpty())
                .sorted(
                        Comparator.comparing(ep -> ep.stream()
                                .findFirst()
                                .map(History::getTimestamp)
                                .orElse(System.currentTimeMillis()))
                )
                .collect(Collectors.toList())
                ;
    }

    /**
     * Take histories from the model, filtering by user and exercise. Episodes are grouped by day.
     *
     * @param model
     * @param exercise
     * @param user
     * @return
     */
    static EpisodesSupplier modelBasedGroupByUserExerciseDay(IModel model, Exercise exercise, MSRUser user)
    {
        return modelBased(
                model,
                h -> Objects.equals(h.getExid(), exercise.getId()) && Objects.equals(h.getUserid(), user.getId()),
                h -> epochMillisToEpochDay(h.getTimestamp())
        );
    }

    static long epochMillisToEpochDay(long epochMillis)
    {
        return epochMillis / (24L * 60L * 60L * 1000L);
    }
}
