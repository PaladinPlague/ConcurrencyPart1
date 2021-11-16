import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CurrentAccountTest {

    CurrentAccount account = new CurrentAccount("56357771", 500.00);
    CurrentAccount secondAcc = new CurrentAccount("30569309", 700.00);

    @Test
    void generateNewCurrentAccount(){
        assertNotNull(account);
        assertEquals("56357771",account.getAccountNumber());
        assertEquals("Current Account",account.getType());
        assertEquals(500.00,account.getBalance());
    }

    @Test
    void makeDeposit(){
        String action = "request";
        account.deposit(50.00, secondAcc, action);

        assertEquals(550,account.getBalance());
        assertEquals(650,secondAcc.getBalance());
    }

    @Test
    void failedToMakeWithdraw(){
        String action = "request";
        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(600.00,secondAcc, action));
        String expectedErMsg = "Sorry, insufficient fund.";
        String erMsg = ex.getMessage();
        assertEquals(expectedErMsg, erMsg);

    }

    @Test
    void makeWithdraw()  {

        String action = "request";
        try {
            account.withdraw(300.00, secondAcc, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(200.00, account.getBalance());
        assertEquals(1000.00,secondAcc.getBalance());
    }

    @Test
    void printStatement(){
        String action = "request";
        try {
            account.withdraw(100.00, secondAcc, action);
            account.withdraw(200.00, secondAcc, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Current Account Number: 56357771, balance: 200.0Transactions：[From: 56357771 To: 30569309 Amount: 100.0, From: 56357771 To: 30569309 Amount: 200.0].",account.getDetails());
    }


}
