package com.codequest23.model;

import com.codequest23.model.component.InteractionComponent;
import com.codequest23.ObjectTypes;
import com.codequest23.model.component.ShapeComponent;

public interface GameObject {

    String objectId();

    ObjectTypes objectType();

    ShapeComponent shapeComponent();

    default InteractionComponent collisionComponent() {
        return shapeComponent()::intersects;
    }

}
