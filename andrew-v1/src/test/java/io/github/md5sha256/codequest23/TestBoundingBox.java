package io.github.md5sha256.codequest23;


import io.github.md5sha256.codequest23.model.component.BoundingBox;
import io.github.md5sha256.codequest23.util.DoublePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestBoundingBox {


    @Test
    void testBoundingBoxIntersect() {
        BoundingBox boundingBox = BoundingBox.ofSquare(10, new DoublePair(0, 0));
        Assertions.assertTrue(boundingBox.intersects(new DoublePair(1, 1)));
    }

}
