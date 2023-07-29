package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.OutboundMessage;
import com.codequest23.message.ShootAction;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;
import com.codequest23.util.MathUtil;

import java.util.Optional;

public class ShootEnemyResponse implements ResponseGenerator {

    private ResponseGenerator next;

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        Tank enemy = map.enemyTank();
        if (MathUtil.distanceSquared(us.hitbox().centre(), enemy.hitbox().centre()) >= 600 * 600) {
            return Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game));
        }
        double angle = MathUtil.angleDegBetween(us.hitbox().centre(), enemy.hitbox().centre());
        return Optional.of(new ShootAction(angle));
    }
}
