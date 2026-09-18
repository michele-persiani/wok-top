package it.unibo.msrehab.model.entities;

import it.unibo.msrehab.model.EntityController;
import it.unibo.msrehab.model.Model;

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


    public static ThresholdAgentConfig getSingletonEntity(Model model)
    {
        return model.getEntityController(ThresholdAgentConfig.class)
                .getAllEntities()
                .stream()
                .findFirst()
                .orElse(new ThresholdAgentConfig())
                ;
    }

    public static boolean putSingletonEntity(Model model, ThresholdAgentConfig config)
    {
        if(!config.isValid())
            return false;
        EntityController<ThresholdAgentConfig> controller = model.getEntityController(ThresholdAgentConfig.class);
        controller.putEntity(config);
        return true;
    }
}
