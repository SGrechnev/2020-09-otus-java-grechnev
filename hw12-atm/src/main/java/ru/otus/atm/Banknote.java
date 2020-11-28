package ru.otus.atm;

public enum Banknote {
    B100(100),
    B200(200),
    B500(500),
    B1000(1000),
    B2000(2000),
    B5000(5000);

    private final int denomination;

    Banknote(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }

    @Override
    public String toString() {
        return this.getDenomination() + " RUB ";
    }
}