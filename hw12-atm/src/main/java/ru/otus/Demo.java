package ru.otus;

import ru.otus.atm.ATM;
import ru.otus.atm.ATMImpl;
import ru.otus.atm.Banknote;
import ru.otus.atm.exception.ATMException;

import java.util.Map;

public class Demo {
    /*private final static String GET_MONEY_MSG_TEMPLATE = """
            |     Get money
            | ATM before: %s
            | ATM before: %s
            | ReceivedBills:  %s
            | BalanceBefore = %d RUB
            | BalanceAfter  = %d RUB""";*/

    public static void main(String[] args) {
        // Create ATM
        ATM atm = new ATMImpl(Map.of(
                Banknote.B2000, 3,
                Banknote.B1000, 1,
                Banknote.B500, 2,
                Banknote.B200, 3
        ));

        System.out.println("-----------------------");
        System.out.println("|     Get balance");
        System.out.println("| ATM: " + atm);
        System.out.println("| Balance = " + atm.getBalance() + " RUB");
        System.out.println("-----------------------");

        System.out.println("|     Add banknote");
        Banknote banknote = Banknote.B1000;
        atm.acceptBanknote(banknote);
        System.out.println("| Add " + banknote);
        System.out.println("| ATM: " + atm);
        System.out.println("-----------------------");

        System.out.println("|     Add banknotes...");
        atm.acceptBanknotes(Banknote.B500, Banknote.B1000, Banknote.B2000, Banknote.B2000);
        System.out.printf("| Add %s, %s, %s, %s%n", Banknote.B500, Banknote.B1000, Banknote.B2000, Banknote.B2000);
        System.out.println("| ATM: " + atm);
        System.out.println("-----------------------");

        System.out.println("|     Add banknotesMap");
        atm.acceptBanknotes(Map.of(
                Banknote.B1000, 1,
                Banknote.B2000, 2
        ));
        System.out.println("| Add " + Map.of(Banknote.B1000, 1, Banknote.B2000, 2));
        System.out.println("| ATM: " + atm);
        System.out.println("-----------------------");

        for (int requestedAmount : new int[]{123, 2_000_000, 5000, 1100}) {
            try {
                int balanceBefore = atm.getBalance();
                String atmBefore = atm.toString();
                Map<Banknote, Integer> receivedBills = atm.getMyMoney(requestedAmount);
                System.out.printf("""
                        |     Get money
                        | ATM before: %s
                        | ATM after:  %s
                        | ReceivedBills:  %s
                        | BalanceBefore = %d RUB
                        | BalanceAfter  = %d RUB%n""", atmBefore, atm.toString(), receivedBills.toString(),
                        balanceBefore, atm.getBalance());
            } catch (ATMException e) {
                System.out.println("| " + e.getMessage());
            }
            System.out.println("-----------------------");
        }
    }
}