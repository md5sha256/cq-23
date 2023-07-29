package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.util.MathUtil;

import java.util.OptionalDouble;

public class DirectShotStrategy implements ShootStrategyChain {

    private final boolean requireClearLineOfSight;
    private ShootStrategyChain next;

    public DirectShotStrategy(boolean requireClearLineOfSight) {
        this.requireClearLineOfSight = requireClearLineOfSight;
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
        if (!requireClearLineOfSight) {
            double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
            return OptionalDouble.of(angle);
        }
        boolean isBlockedByWall = map.streamWallObjects().anyMatch(map.inLineOfSight(us.hitbox().centre(), enemy.hitbox().centre()));
        if (isBlockedByWall) {
            return next == null ? OptionalDouble.empty() : next.generateShotCoordinates(game);
        }
        double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
        return OptionalDouble.of(angle);

    }
}
