package com.codequest23.events;

import com.codequest23.Game;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Map;

public record ChangeEvent(Game game, Collection<String> deleted, Map<String, JsonObject> updated) implements GameEvent {

}
