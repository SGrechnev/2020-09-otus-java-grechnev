package ru.otus.atm;

import ru.otus.atm.exception.UnavailableAmountException;

import java.util.Map;
import java.util.TreeMap;

public class ATMStorageImpl implements ATMStorage {
    private final Map<Banknote, Integer> banknotesInATM = Banknote.getZeroMap();

    public ATMStorageImpl() {
    }

    public ATMStorageImpl(Map<Banknote, Integer> initialBills) {
        this.banknotesInATM.putAll(initialBills);
    }

    @Override
    public Map<Banknote, Integer> getState() {
        return new TreeMap<>(banknotesInATM);
    }

    @Override
    public void getBanknotes(Map<Banknote, Integer> banknotes) throws UnavailableAmountException {
        Map<Banknote, Integer> newState = new TreeMap<>(banknotesInATM);
        banknotes.forEach((banknote, count) -> {
            int inATMCount = banknotesInATM.get(banknote);
            if (inATMCount < count) {
                throw new UnavailableAmountException("Not enough " + banknote + ": in ATM " + inATMCount + ", requested " + count);
            }
            newState.put(banknote, inATMCount - count);
        });
        banknotesInATM.putAll(newState);
    }

    @Override
    public void putBanknotes(Map<Banknote, Integer> banknotes) {
        banknotes.forEach((banknote, count) -> {
            int currentCount = banknotesInATM.get(banknote);
            banknotesInATM.put(banknote, currentCount + count);
        });
    }

    @Override
    public void putBanknote(Banknote banknote) {
        int currentCount = banknotesInATM.get(banknote);
        banknotesInATM.put(banknote, currentCount + 1);
    }
}
