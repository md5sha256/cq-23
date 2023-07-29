package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.ShapeComponent;

public record Wall(String objectId, ShapeComponent shapeComponent) implements GameObject {

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.WALL;
    }
}
