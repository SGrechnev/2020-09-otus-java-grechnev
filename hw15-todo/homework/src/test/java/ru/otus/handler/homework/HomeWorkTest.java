package ru.otus.handler.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.model.Message;
import ru.otus.processor.homework.ProcessorThrowExceptionInEvenSecond;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class HomeWorkTest {
    @ParameterizedTest
    @DisplayName("ProcessorThrowExceptionInEvenSecond бросает исключение")
    @ValueSource(longs = {0, 2, 1_000_000_000_000_000L}) // time in seconds
    void throwExceptionTest(long time) {
        var message = new Message.Builder(1L).build();
        var throwableProcessor = new ProcessorThrowExceptionInEvenSecond(() -> time * 1000); // time in millis
        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> throwableProcessor.process(message));
    }

    @ParameterizedTest
    @DisplayName("ProcessorThrowExceptionInEvenSecond не бросает исключение")
    @ValueSource(longs = {1, 7, 1_000_000_000_000_001L}) // time in seconds
    void doNotThrowExceptionTest(long time) {
        var message = new Message.Builder(1L).build();
        var throwableProcessor = new ProcessorThrowExceptionInEvenSecond(() -> time * 1000); // time in millis
        throwableProcessor.process(message);
    }
}
