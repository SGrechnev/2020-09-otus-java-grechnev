package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.ListenerPrinter;
import ru.otus.listener.homework.ListenerHistory;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.homework.ProcessorSwapField11Field12;
import ru.otus.processor.homework.ProcessorThrowExceptionInEvenSecond;

import java.util.ArrayList;
import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */
        var processors = List.of(new ProcessorSwapField11Field12(),
                new ProcessorThrowExceptionInEvenSecond(System::currentTimeMillis));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println("Error in processing: " + ex.getMessage());
        });
        var listenerHistory = new ListenerHistory();
        complexProcessor.addListener(listenerHistory);

        List<String> data = new ArrayList<>();
        data.add("s1");
        data.add("s2");
        var f13 = new ObjectForMessage();
        f13.setData(data);

        var message = new Message.Builder(1L)
                .field11("field-11")
                .field12("FIELD-12")
                .field13(f13)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        result = complexProcessor.handle(result);
        System.out.println("result:" + result);

        listenerHistory.printHistory();
        data.add("AHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAHAHAHHAHAH");
        listenerHistory.printHistory();
        complexProcessor.removeListener(listenerHistory);
    }
}
