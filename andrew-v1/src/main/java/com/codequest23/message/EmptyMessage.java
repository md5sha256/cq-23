package com.codequest23.message;

import com.codequest23.message.Action;
import com.codequest23.message.OutboundMessage;
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
