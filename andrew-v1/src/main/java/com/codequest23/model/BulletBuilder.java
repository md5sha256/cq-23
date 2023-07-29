package com.codequest23.model;

import com.codequest23.model.component.Hitbox;
import com.codequest23.util.DoublePair;

public final class BulletBuilder {
    private final Hitbox hitbox;
    private String tankId;
    private String objectId;
    private DoublePair velocity;
    private int damage;

    public BulletBuilder(Hitbox hitbox) {
        this.hitbox = hitbox;
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
        return new Bullet(objectId, tankId, damage, velocity, hitbox);
    }
}
