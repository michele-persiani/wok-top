package it.unibo.msrehab.model.levelselection;

public class LevelThreshold
{
    private final int level;
    private final double threshold;

    public LevelThreshold(int level, double threshold)
    {
        this.level = level;
        this.threshold = threshold;
    }

    public int getLevel()
    {
        return level;
    }

    public double getThreshold()
    {
        return threshold;
    }
}
