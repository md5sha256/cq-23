package com.codequest23.model;

import com.codequest23.model.component.HealthComponent;
import com.codequest23.ObjectTypes;
import com.codequest23.model.component.ShapeComponent;

public record DestructibleWall(String objectId, ShapeComponent shapeComponent, HealthComponent healthComponent)
        implements GameObject {

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.DESTRUCTIBLE_WALL;
    }


}
