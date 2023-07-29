package io.github.md5sha256.codequest23.util;

import java.util.Optional;

public record Trajectory(double gradient, double yIntercept) {

    public static Trajectory fromVelocity(DoublePair origin, DoublePair velocity) {
        // y = mx + c
        // 0 = mx + c
        // c = -mx
        double gradient = velocity.y() / velocity.x();
        double yIntercept = -gradient * origin.x();
        return new Trajectory(gradient, yIntercept);
    }

    public Optional<DoublePair> intercept(Trajectory other) {
        if (Math.abs(this.gradient - other.gradient) <= 0.001) {
            return Optional.empty();
        }
        // y = m1(x) + c1
        // y = m2(x) + c2
        // m1(x) + c1 = m2(x) + c2
        // (m1-m2)x = c2 - c1
        // x = (c2 - c1)/(m1 - m2)
        double x = (other.yIntercept - this.yIntercept) / (this.gradient - other.gradient);
        double y = y(x);
        return Optional.of(new DoublePair(x, y));
    }

    public double y(double x) {
        return this.gradient * x + this.yIntercept;
    }

}
