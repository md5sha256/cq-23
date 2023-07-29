package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.HealthComponent;
import com.codequest23.model.component.ShapeComponent;
import com.codequest23.util.DoublePair;

import java.util.Collections;
import java.util.Set;

public record Tank(String objectId, DoublePair velocity, ShapeComponent shapeComponent, HealthComponent healthComponent,
                   Set<TankAspect> activePowerups) implements GameObject {

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.TANK;
    }

    @Override
    public Set<TankAspect> activePowerups() {
        return this.activePowerups == null ? Collections.emptySet() : this.activePowerups;
    }
}
