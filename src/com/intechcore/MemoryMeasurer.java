package com.intechcore;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MemoryMeasurer {

    private static Set<Object> usedObjects = new HashSet<>();

    /**
     * Method measure memory of object. It calculates all variables include references inside of it.
     * If there are "not null" references in that case it goes deeply inside of inner object and
     * calculate its memory also. It doesn't work only with dynamic objects(string, collections).
     * @param myObject given object for measurement
     * @return total size of the object
     */
    public static long measure(Object myObject) {
        usedObjects.add(myObject);
        long totalSize = 0;
        Field[] declaredFields = myObject.getClass().getDeclaredFields();

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);

            Class<?> fieldType = declaredField.getType();
            String result = fieldType.getName();

            try {
                Object reference = declaredField.get(myObject);

                totalSize += sizeByType(result);

                if (reference != null
                        && !fieldType.isPrimitive()
                        && !usedObjects.contains(reference)) {

                    usedObjects.add(reference);
                    totalSize += measure(reference);
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return totalSize;
    }

    private static long sizeByType(String typeName) {
        long size = 0;
        switch (typeName) {
            case "boolean":
            case "byte":
                size = 1;
                break;
            case "char":
            case "short":
                size = 2;
                break;
            case "int":
            case "float":
                size = 4;
                break;
            default:
                size = 8;
                break;
        }

        return size;
    }

}
