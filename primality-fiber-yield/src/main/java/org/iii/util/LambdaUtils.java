package org.iii.util;

public abstract class LambdaUtils {
    public static <T> T identify(T value) {
        return value;
    }

    public static Long add1(Long n) {
        return n + 1;
    }
}
