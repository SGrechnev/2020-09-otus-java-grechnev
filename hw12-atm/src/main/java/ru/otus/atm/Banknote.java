package ru.otus.atm;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Banknote {
    B100(100),
    B200(200),
    B500(500),
    B1000(1000),
    B2000(2000),
    B5000(5000);

    private final int denomination;
    private static final Map<Banknote, Integer> BANKNOTES_ZERO_MAP = new TreeMap<>(Arrays.stream(Banknote.values())
            .collect(Collectors.toMap(Function.identity(), banknote -> 0)));

    Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    public static Map<Banknote, Integer> getZeroMap() {
        return new TreeMap<>(BANKNOTES_ZERO_MAP);
    }

    @Override
    public String toString() {
        return this.getDenomination() + " RUB ";
    }
}