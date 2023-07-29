package io.github.md5sha256.codequest23.model;

import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.component.BoundingBox;
import io.github.md5sha256.codequest23.util.DoublePair;

public class ClosingBoundary extends Boundary implements GameObject {



    public ClosingBoundary(String objectId, BoundingBox boundingBox, DoublePair[] velocities) {
        super(objectId, boundingBox);
    }

    @Override
    public ObjectTypes objectType() {
        return ObjectTypes.CLOSING_BOUNDARY;
    }
}
