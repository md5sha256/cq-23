package com.codequest23.model;

import com.codequest23.util.DoublePair;
import com.codequest23.model.component.ShapeComponent;

public final class BulletBuilder {
    private String tankId;
    private String objectId;
    private DoublePair velocity;
    private int damage;

    private final ShapeComponent shapeComponent;

    public BulletBuilder(ShapeComponent shapeComponent) {
        this.shapeComponent = shapeComponent;
    }

    public BulletBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public BulletBuilder tankId(String tankId) {
        this.tankId = tankId;
        return this;
    }

    public BulletBuilder velocity(DoublePair velocity) {
        this.velocity = velocity;
        return this;
    }

    public BulletBuilder damage(int damage) {
        this.damage = damage;
        return this;
    }

    public Bullet build() {
        return new Bullet(objectId, tankId, velocity, damage, shapeComponent);
    }
}
