package io.github.md5sha256.codequest23.model.component;

import io.github.md5sha256.codequest23.util.DoublePair;

public interface Hitbox {

    DoublePair centre();

    boolean intersects(DoublePair position);

    Hitbox reCentered(DoublePair centre);

    BoundingBox asBoundingBox();

}
