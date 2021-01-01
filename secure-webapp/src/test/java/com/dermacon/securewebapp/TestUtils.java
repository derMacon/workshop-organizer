package com.dermacon.securewebapp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtils {

    public static <T> Set<T> toSet(Iterable<T> it) {
        Set<T> out = new HashSet<>();
        it.forEach(out::add);
        return out;
    }
}
