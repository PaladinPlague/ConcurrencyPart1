import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MortgageTests {

    @Test
    void testCreatedAccount() {
        //Ensures all values are as they should be
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        assertEquals(acc.getAccountNumber(), "123456");
        assertEquals(acc.getBalance(), 100000.00);
        assertEquals(acc.getAnnInterest(), 0.01);
        assertEquals(acc.getMonthInterest(), 0.01 / 12);
        assertEquals(acc.getType(), "Mortgage Account");
        //System.out.println("Test 1 details: ");
        //acc.printDetails();
    }

    @Test
    void testInterestChange() {
        //The interest values should be changing accordingly
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        assertEquals(acc.getAnnInterest(), 0.01);
        assertEquals(acc.getMonthInterest(), 0.01 / 12);
        acc.updateInterest(2.5);
        assertEquals(acc.getAnnInterest(), 0.025);
        assertEquals(acc.getMonthInterest(), 0.025 / 12);
        //System.out.println("Test 2 details: ");
        //acc.printDetails();
    }

    @Test
    void singlePaymentTest() throws InterruptedException {
        //One single payment which *should* affect the balance
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        acc.deposit(acc, 876.04);
        Thread.sleep(3000);
        //System.out.println("Test 3 details: ");
        acc.printDetails();
    }

    @Test
    void latePaymentTest() throws InterruptedException {
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Should print a warning about late payments and NOT accept it
        Thread.sleep(5000);
        acc.deposit(acc, 876.04);
        assertEquals(acc.getBalance(), 100000);
        //System.out.println("Test 4 details: ");
        //acc.printDetails();
    }

    @Test
    void lumpSum() throws InterruptedException {
        //Should close the account
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        acc.deposit(acc,102000.0);
        Thread.sleep(2000);
        assertTrue(acc.getBalance() < 0);
        //On trying to deposit again it should say the account is now closed
    }

    @Test
    void fullMonthPayment() throws InterruptedException {
        //Should print a "full payment" message
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        acc.deposit(acc, 800.0);
        acc.deposit(acc, 70.0);
        acc.deposit(acc,6.04);
        Thread.sleep(2000);
    }

    @Test
    //Should print three "full payment messages"
    void fullThreeMonths() throws InterruptedException {
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        //Month 1
        acc.deposit(acc, acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()));
        Thread.sleep(2500);
        //Month 2
        acc.deposit(acc, acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()));
        Thread.sleep(2500);
        //Month 3
        acc.deposit(acc, acc.calcMonthly(acc.getBalance(), acc.getMonthInterest()));
        Thread.sleep(2000);
        acc.printDetails();
    }

    @Test
    //Using singlePayment, we can look to check for a balance difference
    void normalOverpayment () throws InterruptedException {
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        acc.deposit(acc, 876.04);
        Thread.sleep(2000);
        acc.deposit(acc, 200.0);
        //System.out.println("Test 3 details: ");
        acc.printDetails();
    }
}
