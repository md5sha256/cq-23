package io.github.md5sha256.codequest23.model;

import io.github.md5sha256.codequest23.model.component.HealthComponent;
import io.github.md5sha256.codequest23.model.component.Hitbox;
import io.github.md5sha256.codequest23.model.component.SimpleHealthComponent;
import io.github.md5sha256.codequest23.util.DoublePair;

import java.util.Set;

public final class TankBuilder {
    private final Hitbox hitbox;
    private final HealthComponent healthComponent = new SimpleHealthComponent(0);
    private String objectId;
    private DoublePair velocity;
    private Set<TankAspect> activePowerups;

    public TankBuilder(Hitbox hitbox) {
        this.hitbox = hitbox;
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
        return new Tank(objectId, velocity, hitbox, healthComponent, activePowerups);
    }
}
