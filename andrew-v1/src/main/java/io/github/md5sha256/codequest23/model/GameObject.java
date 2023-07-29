package io.github.md5sha256.codequest23.model;

import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.component.Hitbox;

public interface GameObject {

    String objectId();

    ObjectTypes objectType();

    Hitbox hitbox();

    void hitbox(Hitbox hitbox);

}
