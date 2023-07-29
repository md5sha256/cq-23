package com.codequest23.model;

import com.codequest23.model.component.HealthComponent;
import com.codequest23.model.component.Hitbox;

public final class DestructibleWallBuilder {
    private final Hitbox hitbox;
    private final HealthComponent healthComponent = new SimpleHealthComponent(0);
    private String objectId;

    public DestructibleWallBuilder(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public DestructibleWallBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public DestructibleWall build() {
        return new DestructibleWall(objectId, hitbox, healthComponent);
    }
}
