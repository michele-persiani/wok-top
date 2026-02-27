package it.unibo.msrehab.model.entities;

import flexjson.JSONSerializer;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;


@MappedSuperclass
public class BaseEntity implements Serializable, Comparable<BaseEntity>
{
    private static class Util
    {
        private static final Comparator<BaseEntity> comparator = Comparator.nullsLast(Comparator.comparingLong(BaseEntity::getId));

        private static final JSONSerializer jsonSerializer = new JSONSerializer().exclude("*.class");
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    public Integer getId()
    {
        return id;
    }


    public void setId(Integer id)
    {
        this.id = id;
    }


    public boolean isNew()
    {
        return this.id == null || this.id < 0;
    }


    public boolean isValid()
    {
        return true;
    }


    public String toJSONString()
    {
        return Util.jsonSerializer.serialize(this);
    }

    @Override
    public String toString()
    {
        return String.format("Entity[class=%s, id=%s]", getClass().getSimpleName(), id);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!getClass().equals(obj.getClass()))
            return false;
        BaseEntity other = (BaseEntity) obj;
        if(id == null || other.id == null)
            return false;
        return Objects.equals(id, other.id);
    }


    public int hashCode()
    {
        return Objects.hash(getClass(), isNew()? -1 : id);
    }


    @Override
    public int compareTo(BaseEntity o)
    {
        return Objects.compare(this, o, Util.comparator);
    }
}
