package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.util.DoublePair;
import com.codequest23.model.component.ShapeComponent;

public record Bullet(String objectId, String tankId, DoublePair velocity, int damage,
                     ShapeComponent shapeComponent) implements GameObject {

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.BULLET;
    }
}
