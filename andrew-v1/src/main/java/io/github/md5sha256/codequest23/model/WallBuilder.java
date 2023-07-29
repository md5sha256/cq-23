package io.github.md5sha256.codequest23.model;

import io.github.md5sha256.codequest23.model.component.Hitbox;

public final class WallBuilder {
    private final Hitbox hitbox;
    private String objectId;

    public WallBuilder(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public WallBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public Wall build() {
        return new Wall(objectId, hitbox);
    }
}
