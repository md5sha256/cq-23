package com.codequest23.model;

import com.codequest23.ObjectTypes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GameMap {

    private final Map<String, GameObject> objects = new HashMap<>();
    private final Map<ObjectTypes, Map<String, GameObject>> typeCache = new EnumMap<>(ObjectTypes.class);

    private Tank friendlyTank;
    private Tank enemyTank;

    private ClosingBoundary boundary;

    public GameMap() {
        for (ObjectTypes type : ObjectTypes.values()) {
            typeCache.put(type, new HashMap<>());
        }
    }

    public Tank friendlyTank() {
        return this.friendlyTank;
    }

    public void setFriendlyTank(Tank friendlyTank) {
        this.friendlyTank = friendlyTank;
    }

    public Tank enemyTank() {
        return this.enemyTank;
    }

    public void setEnemyTank(Tank enemyTank) {
        this.enemyTank = enemyTank;
    }

    public ClosingBoundary boundary() {
        return this.boundary;
    }

    public void setBoundary(ClosingBoundary boundary) {
        this.boundary = boundary;
    }

    public Map<String, GameObject> getObjectsByType(ObjectTypes type) {
        return Collections.unmodifiableMap(this.typeCache.get(type));
    }

    public GameObject getObject(String id) {
        return this.objects.get(id);
    }

    public void addObject(GameObject gameObject) {
        this.objects.put(gameObject.objectId(), gameObject);
        this.typeCache.get(gameObject.objectType()).put(gameObject.objectId(), gameObject);
    }

    public void removeObject(String id) {
        GameObject object = this.objects.remove(id);
        if (object != null) {
            this.typeCache.get(object.objectType()).remove(id);
        }
    }


}
