package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.MoveAction;
import com.codequest23.message.OutboundMessage;
import com.codequest23.model.GameMap;
import com.codequest23.model.Tank;

import java.util.Optional;

public class ChaseEnemyResponse implements ResponseGenerator {

    private ResponseGenerator next;

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank enemy = map.enemyTank();
        return Optional.of(new MoveAction(enemy.hitbox().centre()));
    }
}
