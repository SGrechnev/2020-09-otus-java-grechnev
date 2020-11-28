package ru.otus.atm;

import ru.otus.atm.exception.UnavailableAmountException;

import java.util.Map;

public interface ATM {

    int getBalance();

    void acceptBanknote(Banknote banknote);

    void acceptBanknotes(Banknote... banknotes);

    void acceptBanknotes(Map<Banknote, Integer> addBanknotes);

    Map<Banknote, Integer> getMyMoney(int requestedAmount) throws UnavailableAmountException;
}
