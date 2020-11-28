package ru.otus.atm;

import ru.otus.atm.exception.UnavailableAmountException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ATMImpl implements ATM {

    private final Map<Banknote, Integer> banknotesInATM;

    private static final ATMHelper helper = new ATMHelper();
    private static final Banknote[] REVERSE_ORDERED_BANKNOTES;
    private static final Map<Banknote, Integer> EMPTY_ATM;
    private static final int GCD_OF_DENOMINATIONS = helper.gcdByEuclidsAlgorithm(Arrays.stream(Banknote.values()).mapToInt(Banknote::getDenomination).toArray());

    static {
        REVERSE_ORDERED_BANKNOTES = Arrays.stream(Banknote.values())
                .sorted(Comparator.comparingInt(Banknote::getDenomination).reversed())
                .toArray(Banknote[]::new);

        EMPTY_ATM = new TreeMap<>(Arrays.stream(Banknote.values())
                .collect(Collectors.toMap(Function.identity(), banknote -> 0)));
    }

    public ATMImpl() {
        this.banknotesInATM = new TreeMap<>();
    }

    public ATMImpl(Map<Banknote, Integer> initialBills) {
        this.banknotesInATM = new TreeMap<>(EMPTY_ATM);
        this.banknotesInATM.putAll(initialBills);
    }

    @Override
    public int getBalance() {
        return getBalance(this.banknotesInATM);
    }

    public static int getBalance(Map<Banknote, Integer> bundleOfBanknotes) {
        int sum = 0;
        for (Banknote banknote : bundleOfBanknotes.keySet()
        ) {
            sum += banknote.getDenomination() * bundleOfBanknotes.get(banknote);
        }
        return sum;
    }

    @Override
    public void acceptBanknote(Banknote banknote) {
        //System.out.println("Accept banknote: " + banknote);
        addBanknoteToMutableMap(banknote, banknotesInATM);
        /*int currentCount = banknotesInATM.get(banknote);
        banknotesInATM.put(banknote, currentCount + 1);*/
    }

    private static void addBanknoteToMutableMap(Banknote banknote, Map<Banknote, Integer> map) {
        int currentCount = map.get(banknote);
        map.put(banknote, currentCount + 1);
    }

    @Override
    public void acceptBanknotes(Banknote... banknotes) {
        for (Banknote banknote : banknotes) {
            acceptBanknote(banknote);
        }
    }

    @Override
    public void acceptBanknotes(Map<Banknote, Integer> addBanknotes) {
        addBanknotes.forEach((banknote, count) -> {
            int currentCount = banknotesInATM.get(banknote);
            banknotesInATM.put(banknote, currentCount + count);
        });
    }

    @Override
    public Map<Banknote, Integer> getMyMoney(int requestedAmount) throws UnavailableAmountException {
        if (requestedAmount <= 0 && requestedAmount % GCD_OF_DENOMINATIONS != 0) {
            throw new UnavailableAmountException("The ATM is unable to issue the amount: " + requestedAmount);
        }
        if (requestedAmount > getBalance(this.banknotesInATM)) {
            throw new UnavailableAmountException("There is not enough money in ATM. Requested amount: " + requestedAmount);
        }
        Map<Banknote, Integer> result = calculateMap(requestedAmount, EMPTY_ATM);
        subtractFromBanknotesInATM(result);
        return result;
    }

    /**
     * Dynamic programming method
     */
    private Map<Banknote, Integer> calculateMap(int requestedAmount, Map<Banknote, Integer> currentSolve) throws UnavailableAmountException {
        int remainingAmount = requestedAmount - getBalance(currentSolve);
        if (remainingAmount == 0) {
            return currentSolve;
        }
        // Go deeper ...
        for (Banknote banknote : REVERSE_ORDERED_BANKNOTES) {
            if (currentSolve.get(banknote) < this.banknotesInATM.get(banknote) &&
                    remainingAmount >= banknote.getDenomination()) {
                try {
                    Map<Banknote, Integer> nextSolve = new TreeMap<>(Map.copyOf(currentSolve));
                    addBanknoteToMutableMap(banknote, nextSolve);
                    return calculateMap(requestedAmount, nextSolve);
                } catch (UnavailableAmountException e) {
                    //System.out.println("--- curSolve.pop(" + banknote + ")");
                }
            }

        }
        // The denomination of the new banknote is too large
        throw new UnavailableAmountException("The ATM is unable to issue the amount: " + requestedAmount);
    }

    private void subtractFromBanknotesInATM(Map<Banknote, Integer> subtrahend) {
        // Check subtrahend <= banknotesInATM
        subtrahend.forEach((banknote, count) -> {
            if (count > this.banknotesInATM.get(banknote)) {
                throw new IllegalArgumentException("There is not enough " + banknote + " in ATM");
            }
        });
        // Subtract
        subtrahend.forEach((banknote, count) ->
                this.banknotesInATM.put(banknote, this.banknotesInATM.get(banknote) - count)
        );
    }

    @Override
    public String toString() {
        return this.banknotesInATM.toString();
    }
}
