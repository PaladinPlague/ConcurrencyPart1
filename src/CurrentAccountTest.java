import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    @Test
    void getCreatedAccount() {

        //Use this to test that we can create a Current Account and that all details from it are correct
        CurrentAccount acc = new CurrentAccount("98765432", 25.00, 1234);

        assertEquals(acc.getAccountNumber(), "98765432");
        assertEquals(acc.getBalance(), 25.00);
        assertEquals(acc.getType(), "Current Account");
        assertEquals(acc.getPIN(), 1234);

    }

    @Test
    void getDepositAndWithdraw() {

        //Use this to test that we are able to validate and add transactions into our bank
        CurrentAccount acc = new CurrentAccount("12345678", 50.00, 1234);
        CurrentAccount acc2 = new CurrentAccount("23456789", 50.00, 1234);

        acc.deposit(acc2, 7.50);
        acc2.withdraw(acc, 7.50);
        assertEquals(acc.getBalance(), 57.50);
        assertEquals(acc2.getBalance(), 42.50);

        acc.withdraw(acc2, 32.30);
        acc2.deposit(acc, 32.30);
        assertEquals(acc.getBalance(), 25.20);
        assertEquals(acc2.getBalance(), 74.80);

        assertEquals(acc.getTransactions().size(), 2);
        assertEquals(acc2.getTransactions().size(), 2);

        assertEquals(acc.getTransactions().get(0).getAmount(), 7.50);
        assertEquals(acc.getTransactions().get(0).getSource(), acc2);
        assertEquals(acc.getTransactions().get(0).getReceiver(), acc);

        assertEquals(acc2.getTransactions().get(0).getAmount(), 7.50);
        assertEquals(acc2.getTransactions().get(0).getSource(), acc2);
        assertEquals(acc2.getTransactions().get(0).getReceiver(), acc);

        assertEquals(acc.getTransactions().get(1).getAmount(), 32.30);
        assertEquals(acc.getTransactions().get(1).getSource(), acc);
        assertEquals(acc.getTransactions().get(1).getReceiver(), acc2);

        assertEquals(acc2.getTransactions().get(1).getAmount(), 32.30);
        assertEquals(acc2.getTransactions().get(1).getSource(), acc);
        assertEquals(acc2.getTransactions().get(1).getReceiver(), acc2);
    }

    @Test
    void getConfirmPayment() {

        //Test if we are able to use the "confirm payment" method well
        CurrentAccount acc = new CurrentAccount("12345678", 50.00, 1234);
        CurrentAccount acc2 = new CurrentAccount("23456789", 50.00, 1234);

        assertTrue(acc.confirmPayment(acc2, 10.00, 1234));
        assertEquals(acc.getBalance(), 40.00);

        assertFalse(acc.confirmPayment(acc2, 5.00, 1111));
        assertEquals(acc.getBalance(), 40.00);

    }

    @Test
    void getUpdatePin() {

        //Test if we are able to successfully update the PIN with correct input
        CurrentAccount acc = new CurrentAccount("12345678", 50.00, 1234);

        assertEquals(acc.getPIN(), 1234);

        assertTrue(acc.updatePIN(1234, 4321));
        assertEquals(acc.getPIN(), 4321);

        assertFalse(acc.updatePIN(1234, 1111));
        assertEquals(acc.getPIN(), 4321);

        assertFalse(acc.updatePIN(4321, 4321));
        assertEquals(acc.getPIN(), 4321);

    }
}
