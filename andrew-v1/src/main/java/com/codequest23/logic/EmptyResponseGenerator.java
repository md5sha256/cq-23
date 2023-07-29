package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.OutboundMessage;

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
