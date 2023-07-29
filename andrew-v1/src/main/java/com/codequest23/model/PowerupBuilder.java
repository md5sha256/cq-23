package com.codequest23.model;

import com.codequest23.model.component.ShapeComponent;

public final class PowerupBuilder {
    private String objectId;
    private TankAspect tankAspect;
    private final ShapeComponent shapeComponent;

    public PowerupBuilder(ShapeComponent shapeComponent) {
        this.shapeComponent = shapeComponent;
    }


    public PowerupBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public PowerupBuilder tankAspect(TankAspect tankAspect) {
        this.tankAspect = tankAspect;
        return this;
    }

    public Powerup build() {
        return new Powerup(objectId, tankAspect, shapeComponent);
    }
}
