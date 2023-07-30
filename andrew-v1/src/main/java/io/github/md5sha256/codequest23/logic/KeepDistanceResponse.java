package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import io.github.md5sha256.codequest23.message.MoveAction;
import io.github.md5sha256.codequest23.message.OutboundMessage;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Tank;
import io.github.md5sha256.codequest23.util.MathUtil;

import java.util.Optional;

public class KeepDistanceResponse implements ResponseGenerator {

    private final double targetDistanceSquared;
    private ResponseGenerator next;

    public KeepDistanceResponse(double targetDistance) {
        this.targetDistanceSquared = targetDistance * targetDistance;
    }

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        Tank enemy = map.enemyTank();
        if (MathUtil.distanceSquared(us.hitbox().centre(), enemy.hitbox().centre()) > this.targetDistanceSquared) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        boolean isBlockedByWall = map.streamWallObjects().anyMatch(map.inLineOfSight(us.hitbox().centre(), enemy.hitbox().centre()));
        if (isBlockedByWall) {
            return new ChaseEnemyResponse().generateMessage(game);
        }
        double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
        boolean nearestWall = map.streamWallObjects().anyMatch(map.inLineOfSight(us.hitbox().centre(), angle + 90, 80));
        if (nearestWall) {
            angle = angle - 90;
        } else {
            angle = angle + 90;
        }
        return Optional.of(new MoveAction(angle));
    }
}
