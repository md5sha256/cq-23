package com.codequest23.model;

import com.codequest23.model.component.ShapeComponent;

public final class WallBuilder {
    private String objectId;
    private final ShapeComponent shapeComponent;

    public WallBuilder(ShapeComponent shapeComponent) {
        this.shapeComponent = shapeComponent;
    }

    public WallBuilder objectId(String objectId) {
        this.objectId = objectId;
        return this;
    }

    public Wall build() {
        return new Wall(objectId, shapeComponent);
    }
}
