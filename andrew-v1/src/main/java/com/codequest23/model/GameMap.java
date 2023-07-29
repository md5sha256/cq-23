package com.codequest23.model;

import com.codequest23.ObjectTypes;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class GameMap {

    private final Map<ObjectTypes, Map<String, GameObject>> typeCache = new EnumMap<>(ObjectTypes.class);

    public GameMap() {
        for (ObjectTypes type : ObjectTypes.values()) {
            typeCache.put(type, new HashMap<>());
        }
    }

    public Map<String, GameObject> getObjectsByType(ObjectTypes type) {
        return Collections.unmodifiableMap(this.typeCache.get(type));
    }

    public void addObject(GameObject gameObject) {
        this.typeCache.get(gameObject.objectType()).put(gameObject.objectId(), gameObject);
    }

    public void removeObject(ObjectTypes objectType, String id) {
        this.typeCache.get(objectType).remove(id);
    }


}
