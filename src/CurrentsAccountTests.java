import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    @Test
    void getCreatedAccount() {

        //Use this to test that we can create a Current Account and that all details from it are correct
        CurrentAccount acc = new CurrentAccount("98765432", 25.00, 4444);

        assertEquals(acc.getAccountNumber(), "98765432");
        assertEquals(acc.getBalance(), 25.00);
        assertEquals(acc.getType(), "Current Account");

    }

    @Test
    void getUpdateAccountNo() {

        //Use this to test that we can change an account's number
        CurrentAccount acc = new CurrentAccount("10000000", 0.00, 1111);
        assertEquals(acc.getAccountNumber(), "10000000");

        acc.updateAccountNo("10000002");
        assertEquals(acc.getAccountNumber(), "10000002");

    }

    @Test
    void getTransactionTests() {

        //Use this to test that we are able to validate and add transactions into our bank
        CurrentAccount acc = new CurrentAccount("12345678", 50.00, 1234);

        //Note - names of transaction sources (e.g. "McDonald's") used to demonstrate real-life scenarios
        assertTrue(acc.confirmPayment(new Transaction(-12.50, "McDonald's"), 1234));
        assertEquals(acc.getBalance(), 37.50);

        assertFalse(acc.confirmPayment(new Transaction(35.00, "Friend's gift"), 1111));
        assertEquals(acc.getBalance(), 37.50);

        assertTrue(acc.confirmPayment(new Transaction(25.30, "Teacher Loan"), 1234));
        assertEquals(acc.getBalance(), 62.80);

        assertEquals(acc.getTransactions().size(), 2);

        assertEquals(acc.getTransactions().get(0).getSource(), "McDonald's");
        assertEquals(acc.getTransactions().get(0).getAmount(), -12.50);

        assertEquals(acc.getTransactions().get(1).getSource(), "Teacher Loan");
        assertEquals(acc.getTransactions().get(1).getAmount(), 25.30);
    }

    @Test
    void getUpdatePin() {
        CurrentAccount acc = new CurrentAccount("01234567", 0.00, 1234);

        assertTrue(acc.confirmPayment(new Transaction(1.00, "Transaction test 1"), 1234));

        assertTrue(acc.updatePIN(1234, 4321));
        assertFalse(acc.confirmPayment(new Transaction(1.00, "Transaction test 2"), 1234));
        assertTrue(acc.confirmPayment(new Transaction(1.00, "Transaction test 3"), 4321));

        assertFalse(acc.updatePIN(1234, 1111));
        assertFalse(acc.confirmPayment(new Transaction(1.00, "Transaction test 4"), 1111));
        assertTrue(acc.confirmPayment(new Transaction(1.00, "Transaction test 5"), 4321));

        assertFalse(acc.updatePIN(4321, 4321));
        assertTrue(acc.confirmPayment(new Transaction(1.00, "Transaction test 6"), 4321));


    }
}
