package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.Hitbox;

import java.util.Objects;

public final class Powerup implements GameObject {
    private final String objectId;
    private final TankAspect tankAspect;
    private Hitbox hitbox;

    public Powerup(String objectId, TankAspect tankAspect, Hitbox hitbox) {
        this.objectId = objectId;
        this.tankAspect = tankAspect;
        this.hitbox = hitbox;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.POWERUP;
    }

    @Override
    public String objectId() {
        return objectId;
    }

    public TankAspect tankAspect() {
        return tankAspect;
    }

    @Override
    public Hitbox hitbox() {
        return hitbox;
    }

    @Override
    public void hitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Powerup) obj;
        return Objects.equals(this.objectId, that.objectId) &&
                Objects.equals(this.tankAspect, that.tankAspect) &&
                Objects.equals(this.hitbox, that.hitbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, tankAspect, hitbox);
    }

    @Override
    public String toString() {
        return "Powerup[" +
                "objectId=" + objectId + ", " +
                "tankAspect=" + tankAspect + ", " +
                "hitbox=" + hitbox + ']';
    }

}
