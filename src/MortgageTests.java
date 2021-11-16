import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MortgageTests {

    @Test
    void testCreatedAccount() {
        //Ensures all values are as they should be
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        assertEquals("123456", acc.getAccountNumber());
        assertEquals(100000.00, acc.getBalance());
        assertEquals(0.01, acc.getAnnInterest());
        assertEquals(0.01 / 12, acc.getMonthInterest());
        assertEquals("Mortgage Account", acc.getType());
        //System.out.println("Test 1 details: ");
        //acc.printDetails();
    }

    @Test
    void testInterestChange() {
        //The interest values should be changing accordingly
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        assertEquals(0.01, acc.getAnnInterest());
        assertEquals(0.01 / 12, acc.getMonthInterest());
        acc.updateInterest(2.5);
        assertEquals(0.025, acc.getAnnInterest());
        assertEquals(0.025 / 12, acc.getMonthInterest());
        //System.out.println("Test 2 details: ");
        //acc.printDetails();
    }

    @Test
    void singleCurrPaymentTest() throws Exception {
        String action = "request";
        //One single payment which *should* affect the balance
        MortgageAcc morAcc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 1000.00);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        morAcc.deposit(876.04, curAcc,action);
        assertEquals(99124.69, morAcc.getBalance());
        assertEquals(123.96,curAcc.getBalance());
        //System.out.println("Test 3 details: ");
        morAcc.printDetails();
    }

    @Test
    void singleCredPaymentTest() throws Exception {
        String action = "request";
        //One single payment which *should* throw an error
        MortgageAcc morAcc = new MortgageAcc("123456", 100000.00, 1, 10);
        CreditAccount credAcc = new CreditAccount("234567", 1000.00, 100.00, 1.0);
        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        morAcc.deposit(876.04, credAcc,action);
        assertEquals(100000, morAcc.getBalance());
        //System.out.println("Test 3 details: ");
        morAcc.printDetails();
    }

    @Test
    void singleMorgPaymentTest() throws Exception {
        String action = "request";
        //One single payment which *should* throw an error
        MortgageAcc morAcc = new MortgageAcc("123456", 100000.00, 1, 10);
        MortgageAcc morAcc2 = new MortgageAcc("123456", 100000.00, 1, 15);
        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        morAcc.deposit(876.04, morAcc2,action);
        assertEquals(100000, morAcc.getBalance());
        //System.out.println("Test 3 details: ");
        morAcc.printDetails();
    }

    @Test
    void latePaymentTest() throws Exception {
        String action = "request";
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 1000.00);
        //Should print a warning about late payments and then accept it
        Thread.sleep(3000);
        acc.deposit(876.04, curAcc,action);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        assertEquals(99124.69, acc.getBalance());
        assertEquals(123.96,curAcc.getBalance());
        //System.out.println("Test 4 details: ");
        acc.printDetails();
    }

    @Test
    void lumpSum() throws Exception {
        String action = "request";
        //Should close the account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 1020000.00);
        acc.deposit(102000.0, curAcc,action);
        assertTrue(acc.getBalance() < 0);
        assertEquals(918000.00,curAcc.getBalance());
    }

    @Test
    void fullMonthPayment() throws Exception {
        String action = "request";
        //Should print a "full payment" message
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 1000.00);
        acc.deposit(600.0, curAcc,action);
        acc.deposit(170.0, curAcc,action);
        acc.deposit(106.04, curAcc,action);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 = 875.31 = 99124.69
        */

        assertEquals(99124.69, acc.getBalance());
        assertEquals(123.96,curAcc.getBalance());
        //System.out.println("Test 4 details: ");
        acc.printDetails();
    }

    @Test
    void twoConsecMonths() throws Exception {
        String action = "request";
        //Should print two fulfilled messages and show the correct change in balance
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 100000.00);
        //Month 1

        double curAccDeposit =acc.calcMonthly(acc.getBalance(), acc.getMonthInterest());
        System.out.println(curAccDeposit);
        acc.deposit(curAccDeposit, curAcc,action);

        /*
        Self calculation for Month 1:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        assertEquals(99124.69, acc.getBalance());
        assertEquals(99123.96, curAcc.getBalance());
        Thread.sleep(3000);
        //Month 2
        curAccDeposit =acc.calcMonthly(acc.getBalance(), acc.getMonthInterest());
        System.out.println(curAccDeposit);
        acc.deposit(curAccDeposit, curAcc,action);

        /*
        Self calculation for Month 2:
        monthly payment (via the formula) is 868.37
        interest = 0.0008... * 868.37 = 0.72
        new amount = 868.37 - 0.72 = 867.65
        balance = 99124.69 - 867.65 = 98257.04
        */

        assertEquals(98257.04, acc.getBalance());
        assertEquals(98255.59, curAcc.getBalance());
        //System.out.println("Test 5 details: ");
        acc.printDetails();
    }

    @Test
        //Should print three "full payment messages"
    void fullThreeMonths() throws Exception {
        String action = "request";
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 100000.00);

        //Month 1
        double curAccDeposit =acc.calcMonthly(acc.getBalance(), acc.getMonthInterest());
        System.out.println(curAccDeposit);
        acc.deposit(curAccDeposit, curAcc,action);

        /*
        Self calculation for Month 1:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        assertEquals(99124.69, acc.getBalance());
        assertEquals(99123.96, curAcc.getBalance());
        Thread.sleep(3000);
        //Month 2
        curAccDeposit =acc.calcMonthly(acc.getBalance(), acc.getMonthInterest());
        System.out.println(curAccDeposit);
        acc.deposit(curAccDeposit, curAcc,action);

        /*
        Self calculation for Month 2:
        monthly payment (via the formula) is 868.37
        interest = 0.0008... * 868.37 = 0.72
        new amount = 868.37 - 0.72 = 867.65
        balance = 99124.69 - 867.65 = 98257.04
        */

        assertEquals(98257.04, acc.getBalance());
        assertEquals(98255.59, curAcc.getBalance());
        Thread.sleep(3000);
        //Month 3
        curAccDeposit =acc.calcMonthly(acc.getBalance(), acc.getMonthInterest());
        System.out.println(curAccDeposit);
        acc.deposit(curAccDeposit, curAcc,action);

        /*
        Self calculation for Month 5:
        monthly payment (via the formula) is 860.77
        interest = 0.0008... * 860.77 = 0.72
        new amount = 860.77 - 0.72 = 860.05
        balance = 98257.04 - 860.05 = 97396.99
        */

        assertEquals(97396.99, acc.getBalance());
        assertEquals(97394.82, curAcc.getBalance());
        //System.out.println("Test 6 details: ");
        acc.printDetails();
    }

    @Test
        //Using singlePayment, we can look to check for a balance difference
    void normalOverpayment () throws Exception {
        String action = "request";
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 10000.00);
        acc.deposit(876.04, curAcc,action);
        acc.deposit(200.0, curAcc,action);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        balance = 99124.69 - 200 = 98924.69
        */

        assertEquals(98924.69, acc.getBalance());
        assertEquals(8923.96,curAcc.getBalance());
        //System.out.println("Test 3 details: ");
        acc.printDetails();
    }

    @Test
        //Checks to ensure the transactions are stored correctly
    void transactionPrint() throws Exception {
        String action = "request";
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        CurrentAccount curAcc = new CurrentAccount("234567", 1000.00);
        //Since there's no other working classes I'll be using the same account as receiver and sender
        acc.deposit(600.0, curAcc,action);
        acc.deposit(170.0, curAcc,action);
        acc.deposit(106.04, curAcc,action);
        ArrayList<Transaction> test = new ArrayList<>();
        test = acc.getTransactions();
        assertEquals(3, test.size());
        for (int i = 0; i < test.size(); i++) {
            System.out.println("Transaction source: " + test.get(i).getSource().getAccountNumber());
            System.out.println("Transaction receiver: " + test.get(i).getReceiver().getAccountNumber());
            System.out.println("Transaction amount: " + test.get(i).getAmount());
            System.out.println();
        }
    }
}
