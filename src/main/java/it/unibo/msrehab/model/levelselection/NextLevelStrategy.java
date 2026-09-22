package it.unibo.msrehab.model.levelselection;

import it.unibo.msrehab.model.JPAModel;
import it.unibo.msrehab.model.Model;
import it.unibo.msrehab.model.entities.Exercise;
import it.unibo.msrehab.model.entities.FitnessWeight;
import it.unibo.msrehab.model.entities.History;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NextLevelStrategy
{

    public static final int MIN_LEVEL = 1;

    private final Model model = JPAModel.getInstance();



    public LevelThreshold getNextLevelIncrementally(double passThreshold, int maxLevel, List<HistoryRecord> records)
    {
        if(records.size() < 2)
            return new LevelThreshold(MIN_LEVEL, passThreshold);

        records = records.stream()
                .sorted(Comparator.comparing(HistoryRecord::getTimestamp))
                .collect(Collectors.toList());

        HistoryRecord h0 = records.get(records.size() - 1);
        HistoryRecord h1 = records.get(records.size() - 2);

        int nextLevel;
        //If both are passed and the same level, increment the level
        if(h0.getPassed() && h1.getPassed() && Objects.equals(h0.getLevel(), h1.getLevel()))
            nextLevel = h0.getLevel() + 1;
        else
            nextLevel = h0.getLevel();

        nextLevel = Math.max(MIN_LEVEL, Math.min(maxLevel, nextLevel));
        return new LevelThreshold(nextLevel, passThreshold);

    }

    /**
     * Finds the next level and threshold with the adaptive strategy
     * @param minThreshold Minimum threshold below which level is decreased
     * @param startThreshold Starting threshold
     * @param thrPassedDelta Delta for the threshold when the history is passed
     * @param thrNotPassedDelta Delta for the threshold when the history is not passed
     * @param records History records
     * @return
     */
    public LevelThreshold getNextLevelAdaptively(
            double minThreshold,
            double startThreshold,
            double thrPassedDelta,
            double thrNotPassedDelta,
            int maxLevel,
            List<HistoryRecord> records
    )
    {
        HistoryRecord lastHistory = records.stream()
                .max(Comparator.comparing(HistoryRecord::getTimestamp))
                .orElse(null);

        if (lastHistory == null)    // No histories, return the default threshold
            return new LevelThreshold(MIN_LEVEL, startThreshold);

        int level;
        double threshold;

        if (lastHistory.getPassed())
        {
            level = lastHistory.getLevel() + 1;
            threshold = lastHistory.getThreshold() + thrPassedDelta;
        } else
        {
            level = lastHistory.getLevel();
            threshold = lastHistory.getThreshold() + thrNotPassedDelta;
        }
        if (threshold < minThreshold)
        {
            threshold = startThreshold;
            level -= 1;
        }

        threshold = Math.min(1, Math.max(minThreshold, threshold));
        level = Math.max(MIN_LEVEL, Math.min(maxLevel, level));
        return new LevelThreshold(level, threshold);
    }

    public LevelThreshold getNextLevelPid(
            double kp,
            double kd,
            double ki,
            double minThreshold,
            double defaultThreshold,
            List<HistoryRecord> records
    )
    {


        records.sort(Comparator.comparing(HistoryRecord::getTimestamp));

        if(records.isEmpty())
            return new LevelThreshold(MIN_LEVEL, defaultThreshold);

        HistoryRecord h0 = records.get(records.size() - 1);
        HistoryRecord h1 = records.size() > 2 ? records.get(records.size() - 2) : null;

        double prop;
        double deriv = 0;
        double integral = 0;

        prop = kp * getPidError(h0);

        if(h1 != null)
        {
            deriv = kd * (getPidError(h0) - getPidError(h1));
            integral = records.stream()
                    .mapToDouble(h -> ki * getPidError(h))
                    .sum();
        }

        int    level     = h0.getLevel();
        double threshold = h0.getThreshold() + prop + deriv + integral;

        if(threshold >= 1)
        {
            level += 1;
            threshold = defaultThreshold;
        }
        if(threshold < minThreshold)
        {
            level -= 1;
            threshold = defaultThreshold;
        }
        return new LevelThreshold(level, threshold);
    }

    private double getDefaultPassThreshold(Exercise exercise)
    {
        return model.getEntityController(FitnessWeight.class)
                .getAllEntities()
                .stream()
                .filter(pt -> pt.getExid().equals(exercise.getId()))
                .mapToDouble(FitnessWeight::getThr)
                .findFirst()
                .orElse(0.8d);
    }

    private boolean isToday(History history)
    {
        long todayMillis = System.currentTimeMillis() / (24 * 60 * 60 * 1000);
        long historyMillis = history.getCreationTimestamp() / (24 * 60 * 60 * 1000);

        return todayMillis == historyMillis;
    }

    private double getPidError(HistoryRecord h)
    {
        return h.getThreshold() - h.getPerformance();
    }
}
