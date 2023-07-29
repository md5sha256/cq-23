package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.DestructibleWall;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.GameObject;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.model.TankAspect;
import io.github.md5sha256.codequest23.util.MathUtil;

import java.util.List;
import java.util.OptionalDouble;

public class BulldozeShotStrategy implements ShootStrategyChain {

    private final int maxShots;
    private ShootStrategyChain next;

    public BulldozeShotStrategy(int maxShots) {
        this.maxShots = maxShots;
    }

    @Override
    public void nextStrategy(ShootStrategyChain next) {
        this.next = next;
    }

    @Override
    public OptionalDouble generateShotCoordinates(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        Tank enemy = map.enemyTank();
        int bulletDamage;
        if (us.powerups().contains(TankAspect.DAMAGE)) {
            bulletDamage = 2;
        } else {
            bulletDamage = 1;
        }
        List<GameObject> walls = map.streamWallObjects()
                .filter(map.inLineOfSight(us.hitbox().centre(), enemy.hitbox().centre()))
                .toList();
        int shotsRequired = 0;
        for (GameObject wall : walls) {
            if (wall.objectType() == ObjectTypes.WALL) {
                return next == null ? OptionalDouble.empty() : next.generateShotCoordinates(game);
            }
            DestructibleWall destructibleWall = (DestructibleWall) wall;
            int wallHealth = destructibleWall.healthComponent().health();
            shotsRequired += bulletDamage % wallHealth;
        }
        if (shotsRequired > this.maxShots) {
            return next == null ? OptionalDouble.empty() : next.generateShotCoordinates(game);
        }
        double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
        return OptionalDouble.of(angle);
    }
}
