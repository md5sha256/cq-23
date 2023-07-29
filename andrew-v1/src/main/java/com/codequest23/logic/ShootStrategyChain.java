package com.codequest23.logic;

import com.codequest23.Game;

import java.util.OptionalDouble;

public interface ShootStrategyChain {
    
    static ShootStrategyChain chain(ShootStrategyChain first, ShootStrategyChain... next) {
        if (next == null || next.length == 0) {
            first.nextStrategy(null);
            return first;
        }
        ShootStrategyChain current = first;
        for (ShootStrategyChain generator : next) {
            if (current == generator) {
                continue;
            }
            current.nextStrategy(generator);
            current = generator;
        }
        current.nextStrategy(null);
        return first;
    }

    void nextStrategy(ShootStrategyChain next);

    OptionalDouble generateShotCoordinates(Game game);

}
