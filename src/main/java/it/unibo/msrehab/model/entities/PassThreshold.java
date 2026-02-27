package it.unibo.msrehab.model.entities;

import javax.persistence.Entity;



@Entity
public class PassThreshold extends BaseEntity
{
    private Integer assignmentId;

    private int level;
    private Double threshold;

    public double getThreshold()
    {
        return threshold;
    }

    public void setThreshold(double passThreshold)
    {
        this.threshold = passThreshold;
    }

    public int getAssignmentId()
    {
        return assignmentId;
    }

    public void setAssignmentId(int assignmentId)
    {
        this.assignmentId = assignmentId;
    }

    @Override
    public boolean isValid()
    {
        return super.isValid() &&
                assignmentId != null &&
                threshold != null &&
                threshold >= 0 &&
                threshold <= 1
                ;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public static PassThreshold create(int assignmentId, int level, double threshold)
    {
        PassThreshold pt = new PassThreshold();
        pt.setAssignmentId(assignmentId);
        pt.setLevel(level);
        pt.setThreshold(threshold);
        return pt;
    }
}
