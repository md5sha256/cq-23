package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.ShapeComponent;

public record Powerup(String objectId, TankAspect tankAspect, ShapeComponent shapeComponent) implements GameObject {

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.POWERUP;
    }
}
