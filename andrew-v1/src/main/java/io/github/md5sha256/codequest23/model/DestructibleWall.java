package io.github.md5sha256.codequest23.model;

import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.component.HealthComponent;
import io.github.md5sha256.codequest23.model.component.Hitbox;

import java.util.Objects;

public final class DestructibleWall
        implements GameObject {
    private final String objectId;
    private final HealthComponent healthComponent;
    private Hitbox hitbox;

    public DestructibleWall(String objectId, Hitbox hitbox, HealthComponent healthComponent) {
        this.objectId = objectId;
        this.hitbox = hitbox;
        this.healthComponent = healthComponent;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.DESTRUCTIBLE_WALL;
    }

    @Override
    public String objectId() {
        return objectId;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (DestructibleWall) obj;
        return Objects.equals(this.objectId, that.objectId) &&
                Objects.equals(this.hitbox, that.hitbox) &&
                Objects.equals(this.healthComponent, that.healthComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, hitbox, healthComponent);
    }

    @Override
    public String toString() {
        return "DestructibleWall[" +
                "objectId=" + objectId + ", " +
                "hitbox=" + hitbox + ", " +
                "healthComponent=" + healthComponent + ']';
    }


}
