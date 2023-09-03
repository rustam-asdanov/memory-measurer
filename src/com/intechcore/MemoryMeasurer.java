package com.intechcore;

import java.lang.reflect.*;
import java.util.*;

public class MemoryMeasurer {

    private static Object reference;
    private static Field[] declaredFields;
    private static final IdentityHashMap<Object, String> usedReferencesOfObjects = new IdentityHashMap<>();
    private static final Map<Class<?>, Integer> sizeByType = Map.of(
            boolean.class, 1,
            byte.class, 1,
            char.class, 2,
            short.class, 2,
            int.class, 4,
            float.class, 4,
            double.class, 8,
            String.class, 8
    );

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


//                System.out.println(reference.getClass().getComponentType());

                totalSize = getCalculatedSizeByType(declaredField, totalSize);

                System.out.println(reference + " " + totalSize);

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
            totalSize += 8 + (long) reference.toString().length() * sizeByType.get(char.class);
        } else if (isArray(reference)) {
            totalSize = getArraySize(totalSize);
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

    private static long getArraySize(long totalSize) {
        Class<?> type = reference.getClass().getComponentType();
        if(type.isPrimitive()){
            totalSize += 8 + getArrayLength(reference) * sizeByType.get(reference.getClass().getComponentType());
        } else {
            totalSize += 8 + getArrayLength(reference) * sizeByType.getOrDefault(type, 8) + getCombinedStringArrayLength((String[]) reference) * sizeByType.get(char.class);
        }

        return totalSize;
    }

    private static long getCombinedStringArrayLength(String[] arrayOfString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : arrayOfString) {
            if (str != null) {
                stringBuilder.append(str);
            }
        }

        System.out.println("string builder " + stringBuilder);


        return stringBuilder.toString().length();
    }

}

