import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    @Test
    void getCreatedAccount() {

        //Use this to test that we can create a Current Account and that all details from it are correct
        CurrentAccount acc = new CurrentAccount("98765432", 25.00);

        assertEquals(acc.getAccountNumber(), "98765432");
        assertEquals(acc.getBalance(), 25.00);
        assertEquals(acc.getType(), "Current Account");

        assertEquals(acc.getDetails(), "Account number: 98765432\nBalance: 25.00\nAccount Type: Current Account\n");

    }

    @Test
    void getDepositAndWithdraw() {

        //Use this to test that we are able to validate and add transactions into our bank
        CurrentAccount acc = new CurrentAccount("12345678", 50.00);
        CurrentAccount acc2 = new CurrentAccount("23456789", 50.00);

        acc.deposit(7.50, acc2);
        acc2.withdraw(7.50, acc);
        assertEquals(acc.getBalance(), 57.50);
        assertEquals(acc2.getBalance(), 42.50);

        acc.withdraw(32.30, acc2);
        acc2.deposit(32.30, acc);
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

}