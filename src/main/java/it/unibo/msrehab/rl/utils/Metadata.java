package it.unibo.msrehab.rl.utils;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;


/**
 * Class to store arbitrary metadata. Metadata can be serialized to JSON and deserialized from JSON.
 */
public class Metadata
{
    private final Map<Object, String> properties = new LinkedHashMap<>();


    public Metadata put(Object key, String value)
    {
        properties.put(key, value);
        return this;
    }

    public <T> Metadata put(Object key, T value)
    {
        JSONSerializer serializer = new JSONSerializer();
        properties.put(key, serializer.deepSerialize(value));
        return this;
    }

    public <T> T get(Object key, Class<T> type)
    {
        if (!contains(key))
            return null;
        JSONDeserializer<T> deserializer = new JSONDeserializer<>();
        return ExceptionUtils.attempt(() -> deserializer.deserialize(properties.get(key), type), null);
    }

    public String get(Object key)
    {
        return properties.getOrDefault(key, null);
    }

    public boolean contains(Object key)
    {
        return properties.containsKey(key);
    }


    public boolean isEmpty()
    {
        return properties.isEmpty();
    }

    public <T> boolean test(Object key, Class<T> type, Predicate<T> test)
    {
        return ExceptionUtils.attempt(() ->
                {
                    T value = get(key, type);
                    return value != null && test.test(value);
                },
                false);
    }

    public <T> boolean testEquals(Object key, Class<T> type, T value)
    {
        return test(key, type, v -> Objects.equals(v, value));
    }

    public String serialize()
    {
        return new JSONSerializer().deepSerialize(this);
    }

    public static Metadata deserialize(String json)
    {
        if (json == null)
            return new Metadata();
        JSONDeserializer<Metadata> deserializer = new JSONDeserializer<>();
        return deserializer.deserialize(json, Metadata.class);
    }

    @Override
    public String toString()
    {
        return serialize();
    }
}
