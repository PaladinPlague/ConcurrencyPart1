import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StudentAccountTest {

    StudentAccount account = new StudentAccount("93391634",100.00);
    CurrentAccount secondAcc = new CurrentAccount("30569309", 700.00);

    @Test
    void generateNewStudentAccount(){

        assertEquals("93391634",account.getAccountNumber());
        assertEquals("Student Account",account.getType());
        assertEquals(150.00,account.getBalance());

    }

    @Test
    void makeDeposit(){
        account.deposit(50.00, secondAcc);

        assertEquals(200,account.getBalance());
    }

    @Test
    void failedToMakeWithdraw(){

        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(1700.00,secondAcc));
        String expectedErMsg = "Sorry, insufficient fund.";
        String erMsg = ex.getMessage();
        assertEquals(expectedErMsg, erMsg);

    }

    @Test
    void makeAnOverdraftWithdraw(){

        try {
            account.withdraw(1300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-1150.00, account.getBalance());
        assertTrue(account.isOverdrafted());

    }

    @Test
    void makeWithdraw()  {

        try {
            account.withdraw(30.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(120.00, account.getBalance());
    }

    @Test
    void makeDepositForOverdraft(){

        try {
            account.withdraw(1300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-1150.00, account.getBalance());

        assertTrue(account.isOverdrafted());

        account.deposit(1300.00, secondAcc);

        assertEquals(150,account.getBalance());
    }

    @Test
    void printStatement(){
        try {
            account.withdraw(100.00, secondAcc);
            account.withdraw(200.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("Student Account Number: 93391634, The Arranged Overdraft amount is 1500.0, Overdraft: true balance: -150.0.",account.getDetails());
    }

    @Test
    void ableToChangeOverdraftLimit() {

        try {
            account.setOverdraft(2000.00);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(2000,account.getOverdraft());
    }

    @Test
    void ChangeOverdraftLimitFailed() {

        Exception ex  = assertThrows(Exception.class, ()->account.setOverdraft(1500.00));
        String expectedErMsg = "Sorry we can't set your required overdraft limit to be same as the original limit.";
        String erMsg = ex.getMessage();
        assertEquals(expectedErMsg, erMsg);

    }
}