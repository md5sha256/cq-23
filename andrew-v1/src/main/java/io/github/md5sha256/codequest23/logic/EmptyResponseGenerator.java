package io.github.md5sha256.codequest23.logic;

import io.github.md5sha256.codequest23.Game;
import io.github.md5sha256.codequest23.message.OutboundMessage;

import java.util.Optional;

public class EmptyResponseGenerator implements ResponseGenerator {

    @Override
    public void nextGenerator(ResponseGenerator generator) {

    }

    @Override
    public Optional<OutboundMessage> generateMessage(Game game) {
        return Optional.of(OutboundMessage.EMPTY_RESPONSE);
    }
}
