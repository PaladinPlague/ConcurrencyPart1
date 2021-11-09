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
        System.out.println("Test 1 details: ");
        acc.printDetails();
    }

    @Test
    void testInterestChange() {
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        assertEquals(acc.getAnnInterest(), 0.01);
        assertEquals(acc.getMonthInterest(), 0.01 / 12);
        acc.updateInterest(2.5);
        assertEquals(acc.getAnnInterest(), 0.025);
        assertEquals(acc.getMonthInterest(), 0.025 / 12);
        System.out.println("Test 2 details: ");
        acc.printDetails();
    }

    @Test
    void singlePaymentTest() throws InterruptedException {
        MortgageAcc acc = new MortgageAcc("123456", 100000.00, 1, 10);
        acc.deposit(acc, 876.04);
        Thread.sleep(3000);
        acc.deposit(acc, 0.0);
        System.out.println("Test 3 details: ");
        acc.printDetails();
    }
/*
    @Test
    public void paymentsTest() {
        MortgageAcc acc = new MortgageAcc("01234567", 1000.00,0.03);
        acc.payMortgage(200.00);
        assert(acc.getInterest() == 0.03);
        acc.addInterest();
        assert(acc.getBalance() == 824);
        acc.updateInterest(0.05);
        assert(acc.getInterest() == 0.05);
        acc.payMortgage(100.00);
        acc.addInterest();
        acc.addInterest();
        acc.addInterest();
        assert(acc.getBalance() == 838.12);
        assert(acc.getTransactions().size() == 6);
    }
     */

}
