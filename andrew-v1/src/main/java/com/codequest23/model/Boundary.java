package com.codequest23.model;

import com.codequest23.ObjectTypes;
import com.codequest23.model.component.BoundingBox;
import com.codequest23.model.component.ShapeComponent;

public class Boundary implements GameObject {

    private final String objectId;

    private final BoundingBox boundingBox;

    public Boundary(String objectId, BoundingBox boundingBox) {
        this.objectId = objectId;
        this.boundingBox = boundingBox;
    }

    @Override
    public String objectId() {
        return this.objectId;
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.BOUNDARY;
    }

    @Override
    public ShapeComponent shapeComponent() {
        return this.boundingBox;
    }
}
