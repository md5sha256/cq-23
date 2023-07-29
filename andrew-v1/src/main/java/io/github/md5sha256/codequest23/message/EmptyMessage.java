package io.github.md5sha256.codequest23.message;

import com.google.gson.JsonObject;

public class EmptyMessage implements OutboundMessage {

    @Override
    public Action action() {
        return null;
    }

    @Override
    public JsonObject toJson() {
        return new JsonObject();
    }
}
