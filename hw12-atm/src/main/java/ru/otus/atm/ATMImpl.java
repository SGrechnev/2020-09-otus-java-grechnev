package ru.otus.atm;

import ru.otus.atm.exception.UnavailableAmountException;

import java.util.Map;

public class ATMImpl implements ATM {

    private final ATMStorage atmStorage;
    private final ATMCalculator atmCalculator = new ATMCalculatorImpl();

    public ATMImpl() {
        this.atmStorage = new ATMStorageImpl();
    }

    public ATMImpl(Map<Banknote, Integer> initialBills) {
        this.atmStorage = new ATMStorageImpl(initialBills);
    }

    @Override
    public int getBalance() {
        return this.atmCalculator.getBalance(this.atmStorage.getState());
    }

    @Override
    public void acceptBanknote(Banknote banknote) {
        this.atmStorage.putBanknote(banknote);
    }

    @Override
    public void acceptBanknotes(Banknote... banknotes) {
        for (Banknote banknote : banknotes) {
            acceptBanknote(banknote);
        }
    }

    @Override
    public void acceptBanknotes(Map<Banknote, Integer> addBanknotes) {
        this.atmStorage.putBanknotes(addBanknotes);
    }

    @Override
    public Map<Banknote, Integer> getMyMoney(int requestedAmount) throws UnavailableAmountException {
        Map<Banknote, Integer> result = this.atmCalculator.amountToBanknotes(requestedAmount, this.atmStorage.getState());
        this.atmStorage.getBanknotes(result);
        return result;
    }

    @Override
    public String toString() {
        return this.atmStorage.getState().toString();
    }
}
