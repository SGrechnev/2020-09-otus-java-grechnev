package ru.otus.atm;

import ru.otus.atm.exception.UnavailableAmountException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class ATMCalculatorImpl implements ATMCalculator {
    private static final ATMHelper helper = new ATMHelper();
    private static final int GCD_OF_DENOMINATIONS = helper.gcdByEuclidsAlgorithm(Arrays.stream(Banknote.values()).mapToInt(Banknote::getDenomination).toArray());
    private static final Banknote[] REVERSE_ORDERED_BANKNOTES;

    static {
        REVERSE_ORDERED_BANKNOTES = Arrays.stream(Banknote.values())
                .sorted(Comparator.comparingInt(Banknote::getDenomination).reversed())
                .toArray(Banknote[]::new);
    }

    @Override
    public int getBalance(Map<Banknote, Integer> bundleOfBanknotes) {
        int sum = 0;
        for (Banknote banknote : bundleOfBanknotes.keySet()
        ) {
            sum += banknote.getDenomination() * bundleOfBanknotes.get(banknote);
        }
        return sum;
    }

    @Override
    public Map<Banknote, Integer> amountToBanknotes(int amount, Map<Banknote, Integer> atmState) throws UnavailableAmountException {
        if (amount <= 0 && amount % GCD_OF_DENOMINATIONS != 0) {
            throw new UnavailableAmountException("The ATM is unable to issue the amount: " + amount);
        }
        if (amount > getBalance(atmState)) {
            throw new UnavailableAmountException("There is not enough money in ATM. Requested amount: " + amount);
        }
        AmountToMapCalculator atmc = new AmountToMapCalculator(amount, atmState);
        return atmc.calculateMap();
    }

    private class AmountToMapCalculator {
        private final int requestedAmount;
        private final Map<Banknote, Integer> atmState;

        AmountToMapCalculator(int requestedAmount, Map<Banknote, Integer> atmState) {
            this.requestedAmount = requestedAmount;
            this.atmState = new TreeMap<>(atmState);
        }

        private Map<Banknote, Integer> calculateMap() throws UnavailableAmountException {
            return this.calculateMap(Banknote.getZeroMap());
        }

        /**
         * Dynamic programming method
         */
        private Map<Banknote, Integer> calculateMap(Map<Banknote, Integer> currentSolve) throws UnavailableAmountException {
            int remainingAmount = this.requestedAmount - getBalance(currentSolve);
            if (remainingAmount == 0) {
                return currentSolve;
            }
            // Go deeper ...
            for (Banknote banknote : REVERSE_ORDERED_BANKNOTES) {
                if (currentSolve.get(banknote) < this.atmState.get(banknote) &&
                        remainingAmount >= banknote.getDenomination()) {
                    try {
                        Map<Banknote, Integer> nextSolve = new TreeMap<>(Map.copyOf(currentSolve));
                        int currentCount = nextSolve.get(banknote);
                        nextSolve.put(banknote, currentCount + 1);
                        return this.calculateMap(nextSolve);
                    } catch (UnavailableAmountException e) {
                        // NOP
                    }
                }

            }
            // The denomination of the new banknote is too large
            throw new UnavailableAmountException("The ATM is unable to issue the amount: " + this.requestedAmount);
        }
    }
}
