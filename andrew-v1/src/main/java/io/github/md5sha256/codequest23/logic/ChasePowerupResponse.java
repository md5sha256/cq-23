package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import com.codequest23.ObjectTypes;
import io.github.md5sha256.codequest23.message.MoveAction;
import io.github.md5sha256.codequest23.message.OutboundMessage;
import io.github.md5sha256.codequest23.model.GameMap;
import io.github.md5sha256.codequest23.model.Powerup;
import io.github.md5sha256.codequest23.model.Tank;

import java.util.Optional;

public class ChasePowerupResponse implements ResponseGenerator {

    private ResponseGenerator next;

    @Override
    public void nextGenerator(ResponseGenerator generator) {
        this.next = generator;
    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        GameMap map = game.map();
        Tank us = map.friendlyTank();
        Optional<Powerup> closestPowerup = map.streamObjectsByType(ObjectTypes.POWERUP)
                .filter(map::isWithinBounds)
                .sorted(map.closestTo(us.hitbox().centre()))
                .map(Powerup.class::cast)
                .findFirst();
        return closestPowerup.<OutboundMessage>map(powerup -> new MoveAction(powerup.hitbox().centre()))
                .or(() -> Optional.ofNullable(this.next).flatMap(generator -> generator.generateMessage(game)));
    }
}
