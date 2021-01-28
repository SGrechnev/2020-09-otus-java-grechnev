package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ru.otus.gson.MyGson;
import ru.otus.gson.RecursionException;
import ru.otus.model.AnyObject;
import ru.otus.model.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class MyGsonTest {
    @ParameterizedTest
    @MethodSource("generateDataForCustomTest")
    void customTest(Object o) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        MyGson myGson = new MyGson();
        assertThat(myGson.toJson(o)).isEqualTo(gson.toJson(o));
    }

    private static Stream<Arguments> generateDataForCustomTest() {
        return Stream.of(
                null,
                Arguments.of(true), Arguments.of(false),
                Arguments.of((byte) 1), Arguments.of((short) 2f),
                Arguments.of(3), Arguments.of(4L), Arguments.of(5f), Arguments.of(6d),
                Arguments.of("aaa"), Arguments.of('b'),
                Arguments.of(new byte[]{1, 2, 3}),
                Arguments.of(new short[]{4, 5, 6}),
                Arguments.of(new int[]{7, 8, 9}),
                Arguments.of(new float[]{10f, 11f, 12f}),
                Arguments.of(new double[]{13d, 14d, 15d}),
                Arguments.of(List.of(16, 17, 18)),
                Arguments.of(Collections.singletonList(19)),
                Arguments.of(new AnyObject().toBuilder()
                        .fieldByte((byte) 123)
                        .fieldInt(1337)
                        .fieldShort((short) -1)
                        .fieldLong(9_223_300_000_000_000_000L)
                        .fieldBoolean(true)
                        .fieldFloat(13.37f)
                        .fieldDouble(1.337)
                        .fieldString("leet")
                        .fieldIntArray(new int[]{1, 3, 3, 7})
                        .fieldStringArray(new String[]{"s1", "s2", "s3"})
                        .fieldCollection(new ArrayList<>(List.of(1, 3, 3, 7)))
                        .build()),
                Arguments.of(
                        new Node(
                                "grandpa",
                                new Node(
                                        "parent",
                                        new Node(
                                                "child",
                                                null))))
        );
    }

    @Test
    void throwExceptionTest() {
        var tooDeepNode = new Node(
                "level5",
                new Node(
                        "level4",
                        new Node(
                                "level3",
                                new Node(
                                        "level2",
                                        new Node(
                                                "level1",
                                                null
                                        )
                                )
                        )
                )
        );
        MyGson myGson = new MyGson();
        assertThatExceptionOfType(RecursionException.class).isThrownBy(
                () -> myGson.toJson(tooDeepNode)
        );
    }
}