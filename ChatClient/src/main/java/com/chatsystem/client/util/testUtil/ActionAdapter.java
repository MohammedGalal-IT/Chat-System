package com.chatsystem.client.util.testUtil;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import com.chatsystem.client.network.Action;

public class ActionAdapter extends TypeAdapter<Action> {
    @Override
    public void write(JsonWriter out, Action value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.name());
        }
    }

    @Override
    public Action read(JsonReader in) throws IOException {
        String value = in.nextString();
        return Action.fromString(value);
    }
}
