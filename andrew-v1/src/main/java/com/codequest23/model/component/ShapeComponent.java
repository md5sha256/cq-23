package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

public interface ShapeComponent {

    DoublePair centre();

    boolean intersects(DoublePair position);
}
