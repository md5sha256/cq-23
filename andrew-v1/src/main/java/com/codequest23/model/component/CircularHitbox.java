package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

public class CircularHitbox implements Hitbox {
    private final double radiusSquared;
    private final double radius;

    private final DoublePair centre;

    public CircularHitbox(DoublePair centre, double radius) {
        this.centre = centre;
        this.radius = radius;
        this.radiusSquared = radius * radius;
    }

    @Override
    public DoublePair centre() {
        return this.centre;
    }

    @Override
    public boolean intersects(DoublePair position) {
        double xDifference = this.centre.x() - position.x();
        double yDifference = this.centre.y() - position.y();
        double distanceSquared = (xDifference * xDifference) + (yDifference * yDifference);
        return distanceSquared <= this.radiusSquared;
    }

    @Override
    public Hitbox reCentered(DoublePair centre) {
        return new CircularHitbox(centre, this.radius);
    }
}
