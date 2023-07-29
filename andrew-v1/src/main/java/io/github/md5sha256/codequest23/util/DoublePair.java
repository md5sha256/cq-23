package io.github.md5sha256.codequest23.util;

import java.util.Arrays;

public record DoublePair(double x, double y) {

    public static DoublePair from(double[] arr) {
        if (arr.length != 2) {
            throw new IllegalArgumentException("Invalid array: " + Arrays.toString(arr));
        }
        return new DoublePair(arr[0], arr[1]);
    }

    public double[] toArray() {
        return new double[]{x, y};
    }


}
