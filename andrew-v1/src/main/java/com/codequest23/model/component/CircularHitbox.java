package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

public class CircularHitbox implements Hitbox {
    private final double radiusSquared;
    private final double radius;

    private final DoublePair centre;
    private final BoundingBox boundingBox;

    public CircularHitbox(DoublePair centre, double radius) {
        this.centre = centre;
        this.radius = radius;
        this.radiusSquared = radius * radius;
        this.boundingBox = BoundingBox.ofSquare(this.radius * 2, this.centre);
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

    @Override
    public BoundingBox asBoundingBox() {
        return this.boundingBox;
    }
}
