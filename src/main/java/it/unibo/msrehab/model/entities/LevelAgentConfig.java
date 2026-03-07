package it.unibo.msrehab.model.entities;

import it.unibo.msrehab.rl.model.IEntityController;
import it.unibo.msrehab.rl.model.IModel;

import javax.persistence.Entity;

@Entity
public class LevelAgentConfig extends BaseEntity
{
    public enum PriorModelType
    {
        linear,
        sigmoid
    }


    // Value iteration agent parameters
    private Double discountFactor = 0.95;

    private Double targetPerformance = 0.5;


    // Patients prior model params

    private PriorModelType priorModelType = PriorModelType.sigmoid;

    private Double priorWeight = 0.05;

    private Double priorSlope = 1d;

    private Double priorIntercept = 0.5; // percentage between 0 and max level of the exercise

    private Double policyEpsilon = 0.05;


    @Override
    public boolean isValid()
    {
        return super.isValid()
                && discountFactor != null && discountFactor >= 0 && discountFactor <= 1
                && targetPerformance != null && targetPerformance >= 0 && targetPerformance <= 1
                && priorModelType != null
                && priorWeight != null && priorWeight >= 0 && priorWeight <= 1
                && priorIntercept != null && priorIntercept >= 0 && priorIntercept <= 1
                && policyEpsilon != null && policyEpsilon >= 0 && policyEpsilon <= 1
                ;
    }

    public Double getDiscountFactor()
    {
        return discountFactor;
    }

    public void setDiscountFactor(Double discountFactor)
    {
        this.discountFactor = discountFactor;
    }

    public Double getTargetPerformance()
    {
        return targetPerformance;
    }

    public void setTargetPerformance(Double targetPerformance)
    {
        this.targetPerformance = targetPerformance;
    }

    public PriorModelType getPriorModelType()
    {
        return priorModelType;
    }

    public void setPriorModelType(PriorModelType priorModelType)
    {
        this.priorModelType = priorModelType;
    }

    public Double getPriorWeight()
    {
        return priorWeight;
    }

    public void setPriorWeight(Double priorWeight)
    {
        this.priorWeight = priorWeight;
    }

    public Double getPriorSlope()
    {
        return priorSlope;
    }

    public void setPriorSlope(Double priorSlope)
    {
        this.priorSlope = priorSlope;
    }

    public Double getPriorIntercept()
    {
        return priorIntercept;
    }

    public void setPriorIntercept(Double priorIntercept)
    {
        this.priorIntercept = priorIntercept;
    }

    public Double getPolicyEpsilon()
    {
        return policyEpsilon;
    }

    public void setPolicyEpsilon(Double policyEpsilon)
    {
        this.policyEpsilon = policyEpsilon;
    }

    public static LevelAgentConfig getSingletonEntity(IModel model)
    {
        return model.getEntityController(LevelAgentConfig.class)
                .getAllEntities()
                .stream()
                .findFirst()
                .orElse(new LevelAgentConfig())
                ;
    }

    public static boolean putSingletonEntity(IModel model, LevelAgentConfig config)
    {
        if(!config.isValid())
            return false;
        IEntityController<LevelAgentConfig> controller = model.getEntityController(LevelAgentConfig.class);
        return controller.putEntity(config);
    }
}
