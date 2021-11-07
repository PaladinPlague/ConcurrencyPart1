import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MortgageTests {

    @Test
    void checkCreatedAccount() {
        //Ensures all values are as should be
        MortgageAcc acc = new MortgageAcc("123456", 2000.00, 0.03);
        assertEquals(acc.getAccountNumber(), "123456");
        assertEquals(acc.getBalance(), 2000.00);
        assertEquals(acc.getInterest(), 0.03);
        assertEquals(acc.getType(), "Mortgage Account");
    }

    @Test
    void getUpdateAccountNo() {
        //Check if updating the account number works
        MortgageAcc acc = new MortgageAcc("123456", 2000.00, 0.03);
        assertEquals(acc.getAccountNumber(), "123456");

        acc.updateAccountNo("234567");
        assertEquals(acc.getAccountNumber(), "234567");

    }

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

}
