package com.codequest23.model;

import com.codequest23.model.component.BoundingBox;
import com.codequest23.util.DoublePair;

public class ClosingBoundary extends Boundary implements GameObject {


    // border: bottom_left, bottom_right, top_right, 
    private final DoublePair[] velocity;

    public ClosingBoundary(String objectId, BoundingBox boundingBox, DoublePair[] velocities) {
        super(objectId, boundingBox);
        this.velocity = velocities;
    }


}
