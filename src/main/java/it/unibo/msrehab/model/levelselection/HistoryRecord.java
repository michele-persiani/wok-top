package it.unibo.msrehab.model.levelselection;

public class HistoryRecord
{
    private final int level;
    private final double performance;
    private final double threshold;

    private final long timestamp;

    public HistoryRecord(int level, double performance, double threshold, long timestamp)
    {
        this.level = level;
        this.performance = performance;
        this.threshold = threshold;
        this.timestamp = timestamp;
    }

    public boolean getPassed()
    {
        return performance >= threshold;
    }

    public int getLevel()
    {
        return level;
    }

    public double getPerformance()
    {
        return performance;
    }

    public double getThreshold()
    {
        return threshold;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
