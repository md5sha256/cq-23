package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.OutboundMessage;
import com.codequest23.message.ShootAction;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;
import com.codequest23.util.MathUtil;

import java.util.Optional;
import java.util.OptionalDouble;

public class ShootEnemyResponse implements ResponseGenerator {

    private final ShootStrategyChain strategyChain;
    private final double maxDistanceSquared;
    private ResponseGenerator next;

    public ShootEnemyResponse(double maxDistance) {
        this(maxDistance, ShootStrategyChain.chain(new WallSpacedShotFilter(40), new DirectShotStrategy(true), new BulldozeShotStrategy(4)));
    }

    public ShootEnemyResponse(double maxDistance, ShootStrategyChain shootStrategy) {
        this.maxDistanceSquared = maxDistance * maxDistance;
        this.strategyChain = shootStrategy;
    }

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        if (!game.canShootThisTick()) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        Tank enemy = map.enemyTank();
        if (MathUtil.distanceSquared(us.hitbox().centre(), enemy.hitbox().centre()) >= this.maxDistanceSquared) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        OptionalDouble optionalAngle = this.strategyChain.generateShotCoordinates(game);
        if (optionalAngle.isPresent()) {
            return Optional.of(new ShootAction(optionalAngle.getAsDouble()));
        }
        return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
    }
}
