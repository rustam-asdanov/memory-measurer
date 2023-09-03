package com.intechcore;

import java.lang.reflect.*;
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
        sizeByType.put(String.class, 8);

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

                totalSize = getCalculatedSizeByType(declaredField, totalSize);

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

    private static long getCalculatedSizeByType(Field declaredField, long totalSize) {
        if (Modifier.isStatic(declaredField.getModifiers())) return totalSize;
        else if (declaredField.getType() == String.class) {
            totalSize += 8 + getStringLength(reference.toString()) * sizeByType.get(char.class);
        } else if (isArray(reference)) {
            totalSize += 8 + getArrayLength(reference) * sizeByType.get(reference.getClass().getComponentType());
        } else if (declaredField.getType().isPrimitive()) {
            totalSize += sizeByType.get(declaredField.getType());
        } else {
            totalSize += 8;
        }
        return totalSize;
    }

    private static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    private static long getArrayLength(Object array) {
        if (array == null || !array.getClass().isArray()) {
            throw new IllegalArgumentException("Input is not an array.");
        }
        return Array.getLength(array);
    }

    private static long getStringLength(Object s) {
        long length = 0;
        try {
            Method lengthMethod = String.class.getMethod("length");
            length = (int) lengthMethod.invoke(s);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return length;
    }
}

