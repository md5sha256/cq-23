package com.codequest23.util;

import com.codequest23.model.BulletBuilder;
import com.codequest23.model.DestructibleWallBuilder;
import com.codequest23.model.PowerupBuilder;
import com.codequest23.model.TankBuilder;
import com.codequest23.model.WallBuilder;
import com.codequest23.util.DoublePair;

public interface GameObjectFactory {

    BulletBuilder createBullet(DoublePair position);

    DestructibleWallBuilder createDestructibleWall(DoublePair position);

    WallBuilder createWall(DoublePair position);

    PowerupBuilder createPowerup(DoublePair position);

    TankBuilder createTank(DoublePair position);

}
