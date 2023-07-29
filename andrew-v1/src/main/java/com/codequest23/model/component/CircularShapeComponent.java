package com.codequest23.model.component;

import com.codequest23.model.component.ShapeComponent;
import com.codequest23.util.DoublePair;

public class CircularShapeComponent implements ShapeComponent {
    private final double radiusSquared;

    private final DoublePair centre;

    public CircularShapeComponent(DoublePair centre, double radius) {
        this.centre = centre;
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
        return distanceSquared <= radiusSquared;
    }

}
