package com.codequest23.logic;

import com.codequest23.Game;
import com.codequest23.message.OutboundMessage;

import java.util.Optional;

public interface ResponseGenerator {

    ResponseGenerator EMPTY = new EmptyResponseGenerator();

    static ResponseGenerator chain(ResponseGenerator first, ResponseGenerator... next) {
        if (next == null || next.length == 0) {
            first.nextGenerator(null);
            return first;
        }
        ResponseGenerator current = first;
        for (ResponseGenerator generator : next) {
            current.nextGenerator(generator);
            current = generator;
        }
        current.nextGenerator(EMPTY);
        return first;
    }

    void nextGenerator(ResponseGenerator generator);

    Optional<OutboundMessage> generateMessage(Game game);

}
