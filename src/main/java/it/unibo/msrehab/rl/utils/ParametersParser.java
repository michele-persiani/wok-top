package it.unibo.msrehab.rl.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class ParametersParser
{
    private final Map<Object, Object> map;


    public ParametersParser(Map<?, ?> map)
    {
        this.map = new HashMap<>(map);
    }


    public ParametersParser getParser(Object key)
    {
        return new ParametersParser(getParameter(key, new HashMap<>()));
    }

    public Map<Object, Object> getParameters()
    {
        return new HashMap<>(map);
    }

    public <T> T getParameter(Object key, T defaultValue)
    {
        return (T) map.getOrDefault(key, defaultValue);
    }

    public boolean hasParameter(Object key)
    {
        return map.containsKey(key);
    }

    public <T> boolean hasParameter(Object key, Class<T> type)
    {
        return hasParameter(key) && canCast(key, type::cast);
    }

    public <T> T castParameter(Object key, T defaultValue, Function<Object, T> castFunction)
    {
        if(!hasParameter(key))
            return defaultValue;
        try
        {
            return castFunction.apply(getParameter(key, defaultValue));
        }
        catch (Exception e)
        {
            return defaultValue;
        }
    }

    public <T> boolean canCast(Object key, Function<Object, T> castFunction)
    {
        return castParameter( key, null, castFunction) != null;
    }

    public Double getDoubleParameter(Object key, Double defaultValue)
    {
        return castParameter(key, defaultValue, v -> Double.parseDouble(v.toString()));
    }

    public String getStringParameter(Object key, String defaultValue)
    {
        return castParameter(key, defaultValue, Object::toString);
    }

    public Integer getIntegerParameter(Object key, Integer defaultValue)
    {
        return castParameter(key, defaultValue, v -> Integer.parseInt(v.toString()));
    }

    public Long getLongParameter(Object key, Long defaultValue)
    {
        return castParameter(key, defaultValue, v -> Long.parseLong(v.toString()));
    }

    public boolean getBooleanParameter(String done, Boolean defaultValue)
    {
        return castParameter(done, defaultValue, v -> Boolean.parseBoolean(v.toString()));
    }


}
