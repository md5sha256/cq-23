package io.github.md5sha256.codequest23.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.md5sha256.codequest23.util.DoublePair;

public record MoveAction(double angleDegrees) implements OutboundMessage {

    @Override
    public Action action() {
        return Action.PATH;
    }

    @Override
    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("move", this.angleDegrees);
        return jsonObject;
    }
}
