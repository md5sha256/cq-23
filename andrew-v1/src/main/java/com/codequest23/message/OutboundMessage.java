package com.codequest23.message;

import com.google.gson.JsonObject;

public interface OutboundMessage {

    OutboundMessage EMPTY_RESPONSE = new EmptyMessage();


    Action action();

    JsonObject toJson();

}
