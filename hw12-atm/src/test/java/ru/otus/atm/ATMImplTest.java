package ru.otus.atm;

import java.util.Map;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.atm.exception.ATMException;
import ru.otus.atm.exception.UnavailableAmountException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ATMImplTest {

    ATM atm;
    ATMCalculator atmCalculator = new ATMCalculatorImpl();

    private static final Map<Banknote, Integer> INITIALIZING_MAP = Map.of(
            Banknote.B2000, 2,
            Banknote.B1000, 1,
            Banknote.B500, 2,
            Banknote.B200, 3
    );
    private static final int START_BALANCE;

    static {
        int sum = 0;
        for (Banknote banknote : INITIALIZING_MAP.keySet()
        ) {
            sum += banknote.getDenomination() * INITIALIZING_MAP.get(banknote);
        }
        START_BALANCE = sum;
    }

    @BeforeEach
    void initATM() {
        this.atm = new ATMImpl(INITIALIZING_MAP);
    }

    @Test
    @Order(1)
    void getBalance() {
        assertEquals(this.atm.getBalance(), START_BALANCE);
    }

    @ParameterizedTest
    @EnumSource(Banknote.class)
    @Order(2)
    void acceptBanknote(Banknote banknote) {
        int balanceBefore = this.atm.getBalance();
        this.atm.acceptBanknote(banknote);
        assertEquals(balanceBefore + banknote.getDenomination(), this.atm.getBalance());
    }

    @Test
    @Order(3)
    void acceptBanknotes() {
        int balanceBefore = this.atm.getBalance();
        this.atm.acceptBanknotes(Banknote.B200, Banknote.B500, Banknote.B500);
        assertEquals(balanceBefore + 1200, this.atm.getBalance());
    }

    @Test
    @Order(3)
    void testAcceptBanknotes() {
        int balanceBefore = this.atm.getBalance();
        this.atm.acceptBanknotes(Map.of(
                Banknote.B200, 1,
                Banknote.B500, 2
        ));
        assertEquals(balanceBefore + 1200, this.atm.getBalance());
    }

    @ParameterizedTest
    @ValueSource(ints = {200, 3200, 6600})
    @Order(4)
    void getMyMoneyMustBeSuccess(int requestedAmount) throws UnavailableAmountException {
        int balanceBefore = this.atm.getBalance();
        // Correct sum
        assertEquals(
                requestedAmount,
                atmCalculator.getBalance(this.atm.getMyMoney(requestedAmount))
        );
        // Correct state
        assertEquals(balanceBefore - requestedAmount, this.atm.getBalance());
    }

    @ParameterizedTest
    @ValueSource(ints = {123, 3200000, 1800})
    @Order(5)
    void getMyMoneyMustBeFail(int requestedAmount) {
        assertThrows(ATMException.class, () -> this.atm.getMyMoney(requestedAmount));
    }
}