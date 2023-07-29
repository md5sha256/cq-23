package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.BoundingBox;
import com.codequest23.model.component.Hitbox;

public class Boundary implements GameObject {

    private final String objectId;

    private Hitbox hitbox;

    public Boundary(String objectId, BoundingBox hitbox) {
        this.objectId = objectId;
        this.hitbox = hitbox;
    }

    @Override
    public String objectId() {
        return this.objectId;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.BOUNDARY;
    }

    @Override
    public Hitbox hitbox() {
        return this.hitbox;
    }

    @Override
    public void hitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }
}
