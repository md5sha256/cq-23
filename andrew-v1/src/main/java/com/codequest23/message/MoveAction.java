package com.codequest23.message;

import com.codequest23.util.DoublePair;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public record MoveAction(DoublePair destination) implements OutboundMessage {

    @Override
    public Action action() {
        return Action.PATH;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        JsonArray array = new JsonArray();
        array.add(destination.x());
        array.add(destination.y());
        jsonObject.add("path", jsonObject);
        return jsonObject;
    }
}
