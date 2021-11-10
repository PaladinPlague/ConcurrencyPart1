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
        account.deposit(50.00, secondAcc);

        assertEquals(550,account.getBalance());
    }

    @Test
    void failedToMakeWithdraw(){

        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(600.00,secondAcc));
        String expectedErMsg = "Sorry, insufficient fund.";
        String erMsg = ex.getMessage();
        assertEquals(expectedErMsg, erMsg);

    }

    @Test
    void makeWithdraw()  {

        try {
            account.withdraw(300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(200.00, account.getBalance());
    }

    @Test
    void printStatement(){
        try {
            account.withdraw(100.00, secondAcc);
            account.withdraw(200.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Current Account Number: 56357771, balance: 200.0Transactions：[From: 56357771 To: 30569309 Amount: 100.0, From: 56357771 To: 30569309 Amount: 200.0].",account.getDetails());
    }


}
