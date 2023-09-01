package com.intechcore;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemoryMeasurer {

    private static Set<Object> usedReferencesOfObjects = new HashSet<>();

    /**
     * Method measure memory of object. It calculates all variables include references inside of it.
     * If there are "not null" references in that case it goes deeply inside of inner object and
     * calculate its memory also. It doesn't work only with dynamic objects(string, collections).
     *
     * @param myObject given object for measurement
     * @return total size of the object
     */
    public static long measure(Object myObject) {
        usedReferencesOfObjects.add(myObject);
        long totalSize = 0;
        Field[] declaredFields = myObject.getClass().getDeclaredFields();

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);

            Class<?> fieldType = declaredField.getType();
            String result = fieldType.getName();

            try {

                Object reference = declaredField.get(myObject);

                // We check here is the variable static or not
                if (Modifier.isStatic(declaredField.getModifiers())) continue;

                totalSize += sizeByType(result);

                if (reference != null
                        && !fieldType.isPrimitive()
                        && !usedReferencesOfObjects.contains(reference)) {

                    usedReferencesOfObjects.add(reference);
                    totalSize += measure(reference);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return totalSize;
    }

    private static long sizeByType(String typeName) {
        Map<String, Integer> sizeByType = new HashMap<>();
        sizeByType.put("boolean", 1);
        sizeByType.put("byte", 1);
        sizeByType.put("char", 2);
        sizeByType.put("short", 2);
        sizeByType.put("int", 4);
        sizeByType.put("float", 4);

        return sizeByType.getOrDefault(typeName, 8);
    }

}
