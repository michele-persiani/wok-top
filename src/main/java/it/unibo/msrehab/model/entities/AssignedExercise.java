package it.unibo.msrehab.model.entities;

import javax.persistence.Entity;


@Entity
public class AssignedExercise extends BaseEntity
{
    private Integer exerciseId;

    private Integer userId;

    private Integer sessionId;

    private Integer rlAgent;

    private Integer level;

    private long timestampMillis = System.currentTimeMillis();

    public void setExerciseId(int exerciseId)
    {
        this.exerciseId = exerciseId;
    }

    public int getExerciseId()
    {
        return exerciseId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setSessionId(int sessionId)
    {
        this.sessionId = sessionId;
    }

    public int getSessionId()
    {
        return sessionId;
    }

    public Integer getRlAgent()
    {
        return rlAgent;
    }

    public void setRlAgent(Integer rlagent)
    {
        this.rlAgent = rlagent;
    }

    @Override
    public boolean isValid()
    {
        return super.isValid() &&
                exerciseId != null &&
                userId != null &&
                sessionId != null &&
                level != null
                ;
    }

    public long getTimestampMillis()
    {
        return timestampMillis;
    }

    public void setTimestampMillis(long timestamp)
    {
        this.timestampMillis = timestamp;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }
}
