package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

public class BoundingBox implements ShapeComponent {

    private final DoublePair centre;
    private final double maxX;
    private final double maxY;
    private final double minX;
    private final double minY;

    private BoundingBox(DoublePair centre, double maxX, double maxY, double minX, double minY) {
        this.centre = centre;
        this.maxX = maxX;
        this.maxY = maxY;
        this.minX = minX;
        this.minY = minY;
    }

    public static BoundingBox ofCoordinate(double maxX, double maxY, double minX, double minY) {
        double halfX = (minX + maxX) / 2;
        double halfY = (minY + maxY) / 2;
        DoublePair centre = new DoublePair(halfX, halfY);
        return new BoundingBox(centre, maxX, maxY, minX, minY);
    }

    public static BoundingBox ofCorners(DoublePair bottomLeft, DoublePair topRight) {
        double minX = bottomLeft.x();
        double minY = bottomLeft.y();
        double maxX = topRight.x();
        double maxY = topRight.y();
        return ofCoordinate(maxX, maxY, minX, minY);
    }

    public static BoundingBox ofSquare(double length, DoublePair centre) {
        return ofRectangle(length, length, centre);
    }

    public static BoundingBox ofRectangle(double lengthX, double lengthY, DoublePair centre) {
        double halfX = lengthX / 2;
        double halfY = lengthY / 2;
        double x = centre.x();
        double y = centre.y();
        double minX = x - halfX;
        double minY = y - halfY;
        double maxX = x + halfX;
        double maxY = y + halfY;
        return new BoundingBox(centre, maxX, maxY, minX, minY);
    }

    public double maxX() {
        return this.maxX;
    }

    public double maxY() {
        return this.maxY;
    }

    public double minX() {
        return this.minX;
    }

    public double minY() {
        return this.minY;
    }

    @Override
    public DoublePair centre() {
        return this.centre;
    }

    @Override
    public boolean intersects(DoublePair position) {
        double x = position.x();
        double y = position.y();
        return x <= this.maxX && x >= this.minX
                && y <= this.maxY && y >= this.minY;
    }

    @Override
    public String toString() {
        return "QuadrilateralHitRegion{" +
                ", centre=" + centre +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                ", minX=" + minX +
                ", minY=" + minY +
                '}';
    }
}
