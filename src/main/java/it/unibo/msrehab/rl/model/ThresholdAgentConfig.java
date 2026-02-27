package it.unibo.msrehab.rl.model;

import it.unibo.msrehab.model.entities.BaseEntity;

import javax.persistence.Entity;


@Entity
public class ThresholdAgentConfig extends BaseEntity
{
    private double startThreshold = 0.8;

    private double lowerThreshold = 0.6;

    private double thresholdDeltaPassed = 0.1;

    private double thresholdDeltaNotPassed = -0.05;

    @Override
    public boolean isValid()
    {
        return super.isValid()
                && startThreshold >= 0 && startThreshold <= 1
                && lowerThreshold >= 0 && lowerThreshold <= 1
                ;
    }

    public double getStartThreshold()
    {
        return startThreshold;
    }

    public void setStartThreshold(double startThreshold)
    {
        this.startThreshold = startThreshold;
    }

    public double getLowerThreshold()
    {
        return lowerThreshold;
    }

    public void setLowerThreshold(double lowerThreshold)
    {
        this.lowerThreshold = lowerThreshold;
    }

    public double getThresholdDeltaPassed()
    {
        return thresholdDeltaPassed;
    }

    public void setThresholdDeltaPassed(double thresholdDelta)
    {
        this.thresholdDeltaPassed = thresholdDelta;
    }


    public double getThresholdDeltaNotPassed()
    {
        return thresholdDeltaNotPassed;
    }

    public void setThresholdDeltaNotPassed(double negativeThresholdDelta)
    {
        this.thresholdDeltaNotPassed = negativeThresholdDelta;
    }

    public static ThresholdAgentConfig getSingletonEntity(IModel model)
    {
        return model.getEntityController(ThresholdAgentConfig.class)
                .getAllEntities()
                .stream()
                .findFirst()
                .orElse(new ThresholdAgentConfig())
                ;
    }

    public static boolean putSingletonEntity(IModel model, ThresholdAgentConfig config)
    {
        if(!config.isValid())
            return false;
        IEntityController<ThresholdAgentConfig> controller = model.getEntityController(ThresholdAgentConfig.class);
        return controller.putEntity(config);
    }
}
