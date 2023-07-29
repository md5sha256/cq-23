package com.codequest23.util;

public record DoublePair(double x, double y) {

    public static DoublePair from(double[] arr) {
        if (arr.length != 2) {
            throw new IllegalArgumentException("Invalid array: " + arr);
        }
        return new DoublePair(arr[0], arr[1]);
    }

    public double[] toArray() {
        return new double[]{x, y};
    }

}
