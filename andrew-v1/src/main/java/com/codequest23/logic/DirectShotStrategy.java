package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;
import com.codequest23.util.MathUtil;

import java.util.OptionalDouble;

public class DirectShotStrategy implements ShootStrategyChain {

    private ShootStrategyChain next;
    private final boolean requireClearLineOfSight;

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
        boolean isBlockedByWall = map.streamWallObjects().noneMatch(map.inLineOfSight(us.hitbox().centre(), enemy.hitbox().centre()));
        if (isBlockedByWall) {
            return next == null ? OptionalDouble.empty() : next.generateShotCoordinates(game);
        }
        double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
        return OptionalDouble.of(angle);

    }
}
