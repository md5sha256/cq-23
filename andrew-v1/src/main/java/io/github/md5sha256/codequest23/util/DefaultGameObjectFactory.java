package io.github.md5sha256.codequest23.util;

import io.github.md5sha256.codequest23.model.BulletBuilder;
import io.github.md5sha256.codequest23.model.DestructibleWallBuilder;
import io.github.md5sha256.codequest23.model.PowerupBuilder;
import io.github.md5sha256.codequest23.model.TankBuilder;
import io.github.md5sha256.codequest23.model.WallBuilder;
import io.github.md5sha256.codequest23.model.component.BoundingBox;
import io.github.md5sha256.codequest23.model.component.CircularHitbox;

public class DefaultGameObjectFactory implements GameObjectFactory {

    @Override
    public BulletBuilder createBullet(DoublePair position) {
        return new BulletBuilder(new CircularHitbox(position, 5));
    }

    @Override
    public DestructibleWallBuilder createDestructibleWall(DoublePair position) {
        return new DestructibleWallBuilder(BoundingBox.ofSquare(18, position));
    }

    @Override
    public WallBuilder createWall(DoublePair position) {
        return new WallBuilder(BoundingBox.ofSquare(18, position));
    }

    @Override
    public PowerupBuilder createPowerup(DoublePair position) {
        return new PowerupBuilder(new CircularHitbox(position, 15));
    }

    @Override
    public TankBuilder createTank(DoublePair position) {
        return new TankBuilder(BoundingBox.ofSquare(20, position));
    }
}
