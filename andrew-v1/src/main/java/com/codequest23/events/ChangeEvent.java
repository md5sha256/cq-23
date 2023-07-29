package com.codequest23.events;

import com.codequest23.Game;
import com.codequest23.model.GameObject;

import java.util.Collection;
import java.util.Map;

public record ChangeEvent(Game game,
                          Collection<String> deleted,
                          Map<String, GameObject> updated,
                          Map<String, GameObject> addedObjects
) implements GameEvent {

}
