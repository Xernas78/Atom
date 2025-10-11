package dev.xernas.atom.list;

import java.util.List;

public class ListUtils {

    public static <T> T getRandomValue(List<T> list) {
        return list.get((int) (Math.random() * list.size()));
    }

    public static <T> T[] getArrayFromList(List<T> list) {
        Object[] array = new Object[list.size()];
        for (int i = 0; i < list.size(); i++) array[i] = list.get(i);
        return (T[]) array;
    }

}
