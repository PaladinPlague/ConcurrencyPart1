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
        String action = "request";
        account.deposit(50.00, secondAcc, action);

        assertEquals(200,account.getBalance());
        assertEquals(650,secondAcc.getBalance());
    }

    @Test
    void failedToMakeWithdraw(){
        String action = "request";
        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(1700.00,secondAcc, action));
        String expectedErMsg = "Sorry, insufficient fund.";
        String erMsg = ex.getMessage();
        assertEquals(expectedErMsg, erMsg);

    }

    @Test
    void makeAnOverdraftWithdraw(){
        String action = "request";
        try {
            account.withdraw(1300.00, secondAcc, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-1150.00, account.getBalance());
        assertEquals(2000.00,secondAcc.getBalance());
        assertTrue(account.isOverdrafted());

    }

    @Test
    void makeWithdraw()  {
        String action = "request";
        try {
            account.withdraw(30.00, secondAcc, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(120.00, account.getBalance());
        assertEquals(730.00,secondAcc.getBalance());
    }

    @Test
    void makeDepositForOverdraft(){
        String action = "request";
        try {
            account.withdraw(1300.00, secondAcc, action);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-1150.00, account.getBalance());
        assertEquals(2000.00,secondAcc.getBalance());

        assertTrue(account.isOverdrafted());

        account.deposit(1300.00, secondAcc, action);

        assertEquals(150,account.getBalance());
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

        assertEquals("Student Account Number: 93391634, The Arranged Overdraft amount is 1500.0, Overdraft: true balance: -150.0, Transactions：[From: 93391634 To: 30569309 Amount: 100.0, From: 93391634 To: 30569309 Amount: 200.0].",account.getDetails());
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