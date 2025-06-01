package dev.xernas.atom.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtils {

    public static <K, V> V getRandomValue(Map<K, V> map) {
        List<V> values = new ArrayList<>(map.values());
        return values.get((int) (Math.random() * values.size()));
    }

}
