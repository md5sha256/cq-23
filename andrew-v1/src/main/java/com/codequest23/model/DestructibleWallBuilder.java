package com.codequest23.model;

import com.codequest23.model.component.HealthComponent;
import com.codequest23.model.component.SimpleHealthComponent;
import com.codequest23.model.component.ShapeComponent;

public final class DestructibleWallBuilder {
    private String objectId;
    private final ShapeComponent shapeComponent;
    private final HealthComponent healthComponent = new SimpleHealthComponent(0);

    public DestructibleWallBuilder(ShapeComponent shapeComponent) {
        this.shapeComponent = shapeComponent;
    }

    public DestructibleWallBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public DestructibleWall build() {
        return new DestructibleWall(objectId, shapeComponent, healthComponent);
    }
}
