package com.codequest23.util;

public class MathUtil {
    public static double distanceSquared(DoublePair us, DoublePair other) {
        double xDiff = other.x() - us.x();
        double yDiff = other.y() - us.y();
        return xDiff * xDiff + yDiff * yDiff;
    }

    public static double gradient(DoublePair point1, DoublePair point2) {
        return (point2.y() - point1.y()) / (point2.x() - point1.x());
    }

    public static double angleDegBetween(DoublePair point1, DoublePair point2) {
        double rad = Math.atan2((point2.y() - point1.y()), (point2.x() - point1.x()));
        return Math.toDegrees(rad);
    }

}
