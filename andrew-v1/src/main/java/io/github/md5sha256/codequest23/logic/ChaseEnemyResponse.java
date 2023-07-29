package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import io.github.md5sha256.codequest23.message.MoveAction;
import io.github.md5sha256.codequest23.message.OutboundMessage;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Tank;

import java.util.Optional;

public class ChaseEnemyResponse implements ResponseGenerator {

    @Override
    public void nextGenerator(ResponseGenerator generator) {
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank enemy = map.enemyTank();
        return Optional.of(new MoveAction(enemy.hitbox().centre()));
    }
}
