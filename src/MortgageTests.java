import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//Mortgage account testing class
//Made by
public class MortgageTests {

    //Test creation of new mortgage account and check all getter methods work as expected
    @Test
    void testCreatedAccount() {
        //Create a new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Ensures all values from getter methods are as they should be
        assertEquals("123456", acc.getAccountNumber());
        assertEquals(100000.00, acc.getBalance());
        assertEquals(0.01, acc.getAnnInterest());
        assertEquals(0.01 / 12, acc.getMonthInterest());
        assertEquals("Mortgage Account", acc.getType());
    }

    //Test methods that change the interest of the mortgage account
    @Test
    void testInterestChange() {
        //Create a new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Check the getter methods for account's interest return correct results
        assertEquals(0.01, acc.getAnnInterest());
        assertEquals(0.01 / 12, acc.getMonthInterest());
        //change the interest of the account and ensure getter methods return appropriate results
        acc.updateInterest(2.5);
        assertEquals(0.025, acc.getAnnInterest());
        assertEquals(0.025 / 12, acc.getMonthInterest());
    }

    //Test process of single deposit for mortgage account
    @Test
    void singlePaymentTest() throws Exception {
        //Create mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */
        //One single deposit which should affect the balance, and methods to verify variable changes
        acc.deposit(876.04, acc);
        assertEquals(99124.69, acc.getBalance());
        acc.printDetails();
    }

    //Test process of delayed payment for mortgage account
    @Test
    void latePaymentTest() throws Exception {
        //Create mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Delay process of payment
        //Should print a warning about late payments and then accept it
        Thread.sleep(3000);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        //One single deposit which should affect the balance, and methods to verify variable changes
        acc.deposit(876.04, acc);
        assertEquals(99124.69, acc.getBalance());
        acc.printDetails();
    }

    //Test deposit method case which should close the account
    @Test
    void lumpSum() throws Exception {
        //Create new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Check that deposit causes the account to close, and verify this by checking if the balance is less than 0
        acc.deposit(102000.0, acc);
        assertTrue(acc.getBalance() < 0);
    }

    //Test multiple payment processes in one month for account
    @Test
    void fullMonthPayment() throws Exception {
        //Create new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Deposit into account three times in sequence
        acc.deposit(600.0, acc);
        acc.deposit(170.0, acc);
        acc.deposit(106.04, acc);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 = 875.31 = 99124.69
        */

        //Check after deposits that details are updated accordingly
        assertEquals(99124.69, acc.getBalance());
        acc.printDetails();
    }

    //Test multiple payment processes in two consecutive months for account
    //Should print two fulfilled messages and show the correct change in balance
    @Test
    void twoConsecMonths() throws Exception {
        //Create new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Deposit of account for first month
        acc.deposit(acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()), acc);

        /*
        Self calculation for Month 1:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        //Check account details are updated accordingly
        assertEquals(99124.69, acc.getBalance());
        //Simulate delay to wait for start of next month
        Thread.sleep(3000);
        //Deposit of account for second month
        acc.deposit(acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()), acc);

        /*
        Self calculation for Month 2:
        monthly payment (via the formula) is 868.37
        interest = 0.0008... * 868.37 = 0.72
        new amount = 868.37 - 0.72 = 867.65
        balance = 99124.69 - 867.65 = 98257.04
        */

        //Check account details are updated accordingly
        assertEquals(98257.04, acc.getBalance());
        acc.printDetails();
    }

    //Test multiple payment processes in three consecutive months for account
    @Test
    void fullThreeMonths() throws Exception {
        //Create new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Deposit of account for first month
        acc.deposit(acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()), acc);

        /*
        Self calculation for Month 1:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        */

        //Check account details are updated accordingly
        assertEquals(99124.69, acc.getBalance());
        Thread.sleep(3000);
        //Deposit of account for second month
        acc.deposit(acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()), acc);

        /*
        Self calculation for Month 2:
        monthly payment (via the formula) is 868.37
        interest = 0.0008... * 868.37 = 0.72
        new amount = 868.37 - 0.72 = 867.65
        balance = 99124.69 - 867.65 = 98257.04
        */

        //Check account details are updated accordingly
        assertEquals(98257.04, acc.getBalance());
        Thread.sleep(3000);
        //Deposit of account for third month
        acc.deposit(acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()), acc);

        /*
        Self calculation for Month 5:
        monthly payment (via the formula) is 860.77
        interest = 0.0008... * 860.77 = 0.72
        new amount = 860.77 - 0.72 = 860.05
        balance = 98257.04 - 860.05 = 97396.99
        */

        //Check account details are updated accordingly
        assertEquals(97396.99, acc.getBalance());
        acc.printDetails();
    }

    //Test what happens if there is an overpayment for the mortgage account
    //Using singlePayment, we can look to check for a balance difference
    @Test
    void normalOverpayment () throws Exception {
        //Create new mortgage account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //carry out two consecutive deposits into mortgage account
        acc.deposit(876.04, acc);
        acc.deposit(200.0, acc);

        /*
        Self calculation for asserts:
        interest = 0.0008... * 876.04 = 0.73
        new amount = 876.04 - 0.73 = 875.31
        balance = 100,000 - 875.31 = 99124.69
        balance = 99124.69 - 200 = 98924.69
        */

        //Check account details are updated accordingly
        assertEquals(98924.69, acc.getBalance());
        acc.printDetails();
    }
}