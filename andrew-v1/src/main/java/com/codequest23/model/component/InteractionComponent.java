package com.codequest23.model.component;

import com.codequest23.util.DoublePair;

@FunctionalInterface
public interface InteractionComponent {

    boolean canInteract(DoublePair position);

}
