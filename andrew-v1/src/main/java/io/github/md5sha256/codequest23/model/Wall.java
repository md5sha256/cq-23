package io.github.md5sha256.codequest23.model;

import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.component.Hitbox;

import java.util.Objects;

public final class Wall implements GameObject {
    private final String objectId;
    private Hitbox hitbox;

    public Wall(String objectId, Hitbox hitbox) {
        this.objectId = objectId;
        this.hitbox = hitbox;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.WALL;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Wall) obj;
        return Objects.equals(this.objectId, that.objectId) &&
                Objects.equals(this.hitbox, that.hitbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, hitbox);
    }

    @Override
    public String toString() {
        return "Wall[" +
                "objectId=" + objectId + ", " +
                "hitbox=" + hitbox + ']';
    }

}
