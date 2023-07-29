package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.Hitbox;
import com.codequest23.util.DoublePair;

import java.util.Objects;

public final class Bullet
        implements GameObject {
    private final String objectId;
    private final String tankId;
    private int damage;
    private DoublePair velocity;
    private Hitbox hitbox;

    public Bullet(String objectId, String tankId, int damage,
                  DoublePair velocity,
                  Hitbox hitbox) {
        this.objectId = objectId;
        this.tankId = tankId;
        this.damage = damage;
        this.velocity = velocity;
        this.hitbox = hitbox;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.BULLET;
    }

    @Override
    public String objectId() {
        return objectId;
    }

    public String tankId() {
        return tankId;
    }

    @Override
    public Hitbox hitbox() {
        return this.hitbox;
    }

    @Override
    public void hitbox(Hitbox hitbox) {
        this.hitbox = hitbox;
    }

    public DoublePair velocity() {
        return this.velocity;
    }

    public void velocity(DoublePair velocity) {
        this.velocity = velocity;
    }

    public int damage() {
        return this.damage;
    }

    public void damage(int damage) {
        this.damage = damage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bullet bullet = (Bullet) o;
        return damage == bullet.damage && Objects.equals(objectId, bullet.objectId) && Objects.equals(tankId, bullet.tankId) && Objects.equals(velocity, bullet.velocity) && Objects.equals(hitbox, bullet.hitbox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(objectId, tankId, damage, velocity, hitbox);
    }

    @Override
    public String toString() {
        return "Bullet{" +
                "objectId='" + objectId + '\'' +
                ", tankId='" + tankId + '\'' +
                ", damage=" + damage +
                ", velocity=" + velocity +
                ", hitbox=" + hitbox +
                '}';
    }
}
