package com.codequest23.message;

import com.google.gson.JsonObject;

public record ShootAction(double angle) implements OutboundMessage {
    @Override
    public Action action() {
        return Action.SHOOT;
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("shoot", this.angle);
        return object;
    }
}
