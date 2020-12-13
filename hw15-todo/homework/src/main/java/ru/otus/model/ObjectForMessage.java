package ru.otus.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage getCopy() {
        var objForMessage = new ObjectForMessage();
        if (this.data != null) {
            objForMessage.setData(List.copyOf(this.data));
        }
        return objForMessage;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ObjectForMessage{data=" + (data == null ? "null" : data.toString()) + '}';
    }
}
