package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionInEvenSecond implements Processor {

    private final TimeGetter timeGetter;

    public ProcessorThrowExceptionInEvenSecond(TimeGetter timeGetter) {
        this.timeGetter = timeGetter;
    }

    @Override
    public Message process(Message message) {
        System.out.println("[TRACE] throwInEvenSecond");
        long second = this.timeGetter.getTimeInMillis() / 1000;
        System.out.println("[INFO]  second: " + second);
        if ((second & 1) == 0) {
            throw new RuntimeException("Even second: " + second);
        }

        return message.toBuilder().build();
    }
}