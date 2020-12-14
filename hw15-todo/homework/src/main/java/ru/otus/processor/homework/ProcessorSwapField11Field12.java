package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapField11Field12 implements Processor {

    @Override
    public Message process(Message message) {
        System.out.println("[TRACE] swapField11AndField12");
        return message.toBuilder().field11(message.getField12()).field12(message.getField11()).build();
    }
}