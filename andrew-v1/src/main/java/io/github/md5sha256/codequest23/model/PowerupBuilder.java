package io.github.md5sha256.codequest23.model;

import io.github.md5sha256.codequest23.model.component.Hitbox;

public final class PowerupBuilder {
    private final Hitbox hitbox;
    private String objectId;
    private TankAspect tankAspect;

    public PowerupBuilder(Hitbox hitbox) {
        this.hitbox = hitbox;
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
        return new Powerup(objectId, tankAspect, hitbox);
    }
}
