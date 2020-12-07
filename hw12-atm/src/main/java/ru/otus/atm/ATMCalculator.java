package ru.otus.atm;

import java.util.Map;

public interface ATMCalculator {
    int getBalance(Map<Banknote, Integer> bundleOfBanknotes);

    Map<Banknote, Integer> amountToBanknotes(int amount, Map<Banknote, Integer> atmState);
}