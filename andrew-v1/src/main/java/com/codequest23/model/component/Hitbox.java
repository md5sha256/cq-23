package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

public interface Hitbox {

    DoublePair centre();

    boolean intersects(DoublePair position);

    Hitbox reCentered(DoublePair centre);

}
