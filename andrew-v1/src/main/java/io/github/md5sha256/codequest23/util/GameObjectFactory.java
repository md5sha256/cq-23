package io.github.md5sha256.codequest23.util;

import io.github.md5sha256.codequest23.model.BulletBuilder;
import io.github.md5sha256.codequest23.model.DestructibleWallBuilder;
import io.github.md5sha256.codequest23.model.PowerupBuilder;
import io.github.md5sha256.codequest23.model.TankBuilder;
import io.github.md5sha256.codequest23.model.WallBuilder;

public interface GameObjectFactory {

    BulletBuilder createBullet(DoublePair position);

    DestructibleWallBuilder createDestructibleWall(DoublePair position);

    WallBuilder createWall(DoublePair position);

    PowerupBuilder createPowerup(DoublePair position);

    TankBuilder createTank(DoublePair position);

}
