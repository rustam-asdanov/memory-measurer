package com.intechcore;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class MemoryMeasurer {

    private static Object reference;
    private static Field[] declaredFields;
    private static final IdentityHashMap<Object, String> usedReferencesOfObjects = new IdentityHashMap<>();
    private static final Map<Class<?>, Integer> sizeByType = new HashMap<>();

    /**
     * Method measure memory of object. It calculates all variables include references inside of it.
     * If there are "not null" references in that case it goes deeply inside inner object and
     * calculate its memory also. It doesn't work with dynamic objects(string, collections).
     *
     * @param data given object for measurement
     * @return total size of the object
     */
    public static long measure(Object data) {
        long totalSize = 0;
        sizeByType.put(boolean.class, 1);
        sizeByType.put(byte.class, 1);
        sizeByType.put(char.class, 2);
        sizeByType.put(short.class, 2);
        sizeByType.put(int.class, 4);
        sizeByType.put(float.class, 4);
        sizeByType.put(double.class, 8);

        return measure(data, totalSize);
    }

    public static long measure(Object data, long totalSize) {
        if (usedReferencesOfObjects.containsKey(data) || data == null) return totalSize;
        usedReferencesOfObjects.put(data, data.toString());

        declaredFields = data.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                reference = declaredField.get(data);
                if (Modifier.isStatic(declaredField.getModifiers())) continue;

                if (declaredField.getType().isPrimitive()) totalSize += sizeByType.get(declaredField.getType());
                else if (isArray(reference)) {
                    System.out.println(reference.getClass().getComponentType());
                    totalSize += 8 + (long) getArrayLength(reference) * sizeByType.get(reference.getClass().getComponentType());
                } else totalSize += 8;

                if (reference != null
                        && !usedReferencesOfObjects.containsKey(reference)
                        && !sizeByType.containsKey(declaredField.getType())) {
                    totalSize = measure(reference, totalSize);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        }


        return totalSize;
    }

    private static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static int getArrayLength(Object array) {
        if (array == null || !array.getClass().isArray()) {
            throw new IllegalArgumentException("Input is not an array.");
        }
        return Array.getLength(array);
    }

}

