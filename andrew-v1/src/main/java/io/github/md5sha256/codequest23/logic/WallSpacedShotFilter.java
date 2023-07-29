package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.util.DoublePair;
import io.github.md5sha256.codequest23.util.MathUtil;

import java.util.OptionalDouble;

public class WallSpacedShotFilter implements ShootStrategyChain {

    private final double minDistanceSquared;
    private ShootStrategyChain next;

    public WallSpacedShotFilter(double minDistanceFromWall) {
        this.minDistanceSquared = minDistanceFromWall * minDistanceFromWall;
    }

    @Override
    public void nextStrategy(ShootStrategyChain next) {
        this.next = next;
    }

    @Override
    public OptionalDouble generateShotCoordinates(Game game) {
        GameMap map = game.map();
        Tank us = game.map().friendlyTank();
        if (!game.canShootThisTick()) {
            return OptionalDouble.empty();
        }
        DoublePair pos = us.hitbox().centre();
        boolean hasNearbyWall = map.streamObjectsByType(ObjectTypes.WALL)
                .noneMatch(wall -> MathUtil.distanceSquared(pos, wall.hitbox().centre()) < this.minDistanceSquared);
        if (hasNearbyWall) {
            return OptionalDouble.empty();
        }
        if (this.next != null) {
            return this.next.generateShotCoordinates(game);
        }
        return OptionalDouble.empty();
    }
}
