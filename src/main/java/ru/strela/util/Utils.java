package ru.strela.util;

import java.util.Collection;

public class Utils {

    /**
     * Возвращает первый элемент коллекции, если он имеется.
     * @param collection
     * @param <T>
     * @return
     */
    public static <T> T firstOrNull(Collection<T> collection) {
        if (collection != null && !collection.isEmpty()) {
            return collection.iterator().next();
        }

        return null;
    }

}
