package it.unibo.msrehab.rl.utils;


import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Utility class to create Maps
 */
public class Maps
{
    private Maps()
    {
    }

    public static <K0, V0> Map<K0, V0> empty()
    {
        return new LinkedHashMap<>();
    }

    public static <K0, V0> Map<K0, V0> of(K0 k0, V0 v0)
    {
        Map<K0, V0> m = empty();
        m.put(k0, v0);
        return m;
    }

    public static <K0, V0> Map<K0, V0> of(K0 k0, V0 v0, K0 k1, V0 v1)
    {
        Map<K0, V0> m = of(k0, v0);
        m.put(k1, v1);
        return m;
    }

    public static <K0, V0> Map<K0, V0> of(K0 k0, V0 v0, K0 k1, V0 v1, K0 k2, V0 v2)
    {
        Map<K0, V0> m = of(k0, v0, k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static <K0, V0> Map<K0, V0> of(K0 k0, V0 v0, K0 k1, V0 v1, K0 k2, V0 v2, K0 k3, V0 v3)
    {
        Map<K0, V0> m = of(k0, v0, k1, v1, k2, v2);
        m.put(k3, v3);
        return m;
    }

    public static <K0, V0> Map<K0, V0> of(K0 k0, V0 v0, K0 k1, V0 v1, K0 k2, V0 v2, K0 k3, V0 v3, K0 k4, V0 v4)
    {
        Map<K0, V0> m = of(k0, v0, k1, v1, k2, v2, k3, v3);
        m.put(k4, v4);
        return m;
    }


}
