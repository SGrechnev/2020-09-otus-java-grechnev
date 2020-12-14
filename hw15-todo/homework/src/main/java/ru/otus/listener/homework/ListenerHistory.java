package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ListenerHistory implements Listener {

    private final List<Record> history = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        history.add(new Record(oldMsg, newMsg));
    }

    public void printHistory() {
        System.out.println(history);
    }

    static public class Record {
        private final Message oldMsg;
        private final Message newMsg;

        public Record(Message oldMsg, Message newMsg) {
            this.oldMsg = oldMsg.clone();
            this.newMsg = newMsg.clone();
        }

        public Message getOldMsg() {
            return oldMsg;
        }

        public Message getNewMsg() {
            return newMsg;
        }

        @Override
        public String toString() {
            return "Record{" +
                    "oldMsg='" + oldMsg + '\'' +
                    ", newMsg='" + newMsg + '\'' +
                    '}';
        }
    }
}