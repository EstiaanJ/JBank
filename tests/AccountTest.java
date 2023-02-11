import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayDeque;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void debit() {
        Account account = new Account("Test Account", 123456, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), true);
        BigDecimal amount = new BigDecimal(100);

        Account updatedAccount = account.debit(amount);

        //assertEquals("The debit amount should be added to the debits history", amount, updatedAccount.debits.getLast());
        assertEquals( -100.0, updatedAccount.balance().doubleValue(), 0.0001);

        updatedAccount.credit(200);
        assertEquals(100,updatedAccount.balance().doubleValue());
    }

    @Test
    void test_debit_negativeDebit() {
        Account account = new Account("Test Account", 123, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), true);
        BigDecimal debitAmount = new BigDecimal(-100);
        Account updatedAccount = account.debit(debitAmount);
        assertEquals(0, updatedAccount.balanceFloatPoint(), 0);
    }

    @Test
    void test_Debit_overdrawAllowed_overdrawn() {
        Account account = new Account("Test Account", 123, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), true);
        BigDecimal debitAmount = new BigDecimal(100);
        Account updatedAccount = account.debit(debitAmount);
        assertEquals(-debitAmount.doubleValue(), updatedAccount.balanceFloatPoint(), 0);
    }

    @Test
    void test_Debit_overdrawNOTALLOWED_overdrawn() {
        Account account = new Account("Test Account", 123, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), false);
        BigDecimal debitAmount = new BigDecimal(100);
        Account updatedAccount = account.debit(debitAmount);
        assertEquals(0, updatedAccount.balance().doubleValue(), 0);
    }

    @Test
    void test_Debit_overdrawAllowed_notOverdrawn() {
        Account account = new Account("Test Account", 123, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), true);
        BigDecimal debitAmount = new BigDecimal(100);
        Account updatedAccount = account.credit(200);
        updatedAccount = account.debit(debitAmount);
        assertEquals(100, updatedAccount.balance().doubleValue(), 0);
    }

    @Test
    void test_Debit_overdrawNotAllowed_notOverdrawn() {
        Account account = new Account("Test Account", 123, new ArrayDeque<BigDecimal>(), new ArrayDeque<BigDecimal>(), false);
        BigDecimal debitAmount = new BigDecimal(100);
        Account updatedAccount = account.credit(200);
        System.out.println(updatedAccount.balanceFloatPoint());
        updatedAccount = updatedAccount.debit(debitAmount);
        System.out.println(updatedAccount.balanceFloatPoint());
        assertEquals(100, updatedAccount.balance().doubleValue(), 0);
    }


    @Test
    void testDebit() {
    }

    @Test
    void credit() {
    }

    @Test
    void testCredit() {
    }

    @Test
    void balance() {
    }

    @Test
    void balanceFloatPoint() {
    }

    @Test
    void quickBalance() {
    }
}