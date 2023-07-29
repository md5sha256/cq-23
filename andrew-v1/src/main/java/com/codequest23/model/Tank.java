package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.HealthComponent;
import com.codequest23.model.component.Hitbox;
import com.codequest23.util.DoublePair;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class Tank implements GameObject {
    private final String objectId;
    private final HealthComponent healthComponent;
    private final Set<TankAspect> powerups = EnumSet.noneOf(TankAspect.class);
    private final DoublePair velocity;
    private Hitbox hitbox;

    public Tank(String objectId,
                DoublePair velocity,
                Hitbox hitbox,
                HealthComponent healthComponent,
                Set<TankAspect> powerups) {
        this.objectId = objectId;
        this.velocity = velocity;
        this.hitbox = hitbox;
        this.healthComponent = healthComponent;
        this.powerups.addAll(powerups);
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.TANK;
    }

    @Override
    public String objectId() {
        return objectId;
    }

    public DoublePair velocity() {
        return velocity;
    }

    @Override
    public Hitbox hitbox() {
        return hitbox;
    }

    @Override
    public void hitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public HealthComponent healthComponent() {
        return healthComponent;
    }

    public Set<TankAspect> powerups() {
        return powerups;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Tank) obj;
        return Objects.equals(this.objectId, that.objectId) &&
                Objects.equals(this.velocity, that.velocity) &&
                Objects.equals(this.hitbox, that.hitbox) &&
                Objects.equals(this.healthComponent, that.healthComponent) &&
                Objects.equals(this.powerups, that.powerups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, velocity, hitbox, healthComponent, powerups);
    }

    @Override
    public String toString() {
        return "Tank[" +
                "objectId=" + objectId + ", " +
                "velocity=" + velocity + ", " +
                "hitbox=" + hitbox + ", " +
                "healthComponent=" + healthComponent + ", " +
                "powerups=" + powerups + ']';
    }

}
