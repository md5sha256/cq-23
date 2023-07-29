package com.codequest23.model;

import com.codequest23.model.component.HealthComponent;
import com.codequest23.util.DoublePair;
import com.codequest23.model.component.ShapeComponent;

import java.util.Collections;
import java.util.Set;

public record Tank(String objectId, DoublePair velocity, ShapeComponent shapeComponent, HealthComponent healthComponent,
                   Set<TankAspect> activePowerups) {

    @Override
    public Set<TankAspect> activePowerups() {
        return this.activePowerups == null ? Collections.emptySet() : this.activePowerups;
    }
}
