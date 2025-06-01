package dev.xernas.atom.list;

import java.util.List;

public class ListUtils {

    public static <T> T getRandomValue(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }

}
