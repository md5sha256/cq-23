package com.codequest23.model;

import com.codequest23.model.component.HealthComponent;
import com.codequest23.model.component.SimpleHealthComponent;
import com.codequest23.util.DoublePair;
import com.codequest23.model.component.ShapeComponent;

import java.util.Set;

public final class TankBuilder {
    private final ShapeComponent shapeComponent;
    private String objectId;
    private final HealthComponent healthComponent = new SimpleHealthComponent(0);

    private DoublePair velocity;
    private Set<TankAspect> activePowerups;

    public TankBuilder(ShapeComponent shapeComponent) {
        this.shapeComponent = shapeComponent;
    }

    public TankBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public TankBuilder velocity(DoublePair velocity) {
        this.velocity = velocity;
        return this;
    }

    public TankBuilder activePowerups(Set<TankAspect> activePowerups) {
        this.activePowerups = activePowerups;
        return this;
    }

    public Tank build() {
        return new Tank(objectId, velocity, shapeComponent, healthComponent, activePowerups);
    }
}
