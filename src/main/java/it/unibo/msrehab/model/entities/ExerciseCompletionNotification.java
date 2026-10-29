package it.unibo.msrehab.model.entities;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ExerciseCompletionNotification extends BaseEntity
{
    @ManyToOne
    @JoinColumn(name = "notified_user_id")
    private MSRUser notifiedUser;

    private boolean enabled;

    public MSRUser getNotifiedUser()
    {
        return notifiedUser;
    }

    public void setNotifiedUser(MSRUser notifiedUser)
    {
        this.notifiedUser = notifiedUser;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
