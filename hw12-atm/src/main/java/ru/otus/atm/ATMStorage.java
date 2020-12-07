package ru.otus.atm;

import java.util.Map;

public interface ATMStorage {
    Map<Banknote, Integer> getState();

    void getBanknotes(Map<Banknote, Integer> banknotes);

    void putBanknote(Banknote banknote);

    void putBanknotes(Map<Banknote, Integer> banknotes);
}
