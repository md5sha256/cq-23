package com.codequest23.logic;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BulletTracker {

    public final Map<String, Integer> bulletCollisions = new HashMap<>();

    public BulletTracker() {
    }

    public BulletTracker(BulletTracker other) {
        this.bulletCollisions.putAll(other.bulletCollisions);
    }

    public Set<String> clearInvalidBullets(int maxCollisions) {
        final Set<String> keys = new HashSet<>();
        for (Map.Entry<String, Integer> entry : this.bulletCollisions.entrySet()) {
            if (entry.getValue() > maxCollisions) {
                keys.add(entry.getKey());
            }
        }
        keys.forEach(this::clear);
        return keys;
    }

    public int getNumCollisions(String bullet) {
        return bulletCollisions.getOrDefault(bullet, 0);
    }

    public void clear(String bullet) {
        this.bulletCollisions.remove(bullet);
    }

    public void addCollision(String bullet) {
        this.bulletCollisions.compute(bullet, (key, collisions) -> collisions == null ? 1 : collisions + 1);
    }

    public void setNumCollisions(String bullet, int numCollisions) {
        this.bulletCollisions.put(bullet, numCollisions);
    }

}
