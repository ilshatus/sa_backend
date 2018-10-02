package com.idc.idc.util;

import org.jclouds.javax.annotation.Nullable;

import java.util.*;

public class CollectionUtils {

    public static <T> List<T> trimToEmpty(@Nullable List<T> list) {
        if (list == null) return new ArrayList<>();
        return list;
    }

    public static <T> List<T> subList(@Nullable List<T> list, Integer left, Integer right) {
        if (list == null) return new ArrayList<>();
        if (left < 0) left = 0;
        if (right > list.size()) right = list.size();
        return list.subList(left, right);
    }
}
