import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//Current account testing class
//Made by
class StudentAccountTest {

    //Variable used for testing various methods of student account class
    StudentAccount account = new StudentAccount("93391634",100.00);
    //Current account used to test interactions between student account and other accounts
    CurrentAccount secondAcc = new CurrentAccount("30569309", 700.00);

    //Test field declaration of current account and the detail-returning methods of current account
    @Test
    void generateNewStudentAccount(){
        //Ensure that the getter methods return the correct details of the CurrentAccount variable
        assertEquals("93391634",account.getAccountNumber());
        assertEquals("Student Account",account.getType());
        assertEquals(150.00,account.getBalance());
    }

    //Test the deposit method of the student account and assert that its balance is properly updated
    @Test
    void makeDeposit(){
        //Deposit to the student account from the current account
        account.deposit(50.00, secondAcc);
        //Check that the balance of the account is increased
        assertEquals(200,account.getBalance());
    }

    @Test
    void failedToMakeWithdraw(){
        //Assert that withdrawing with a high balance throws an exception, and store this in a variable
        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(1700.00,secondAcc));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String erMsg = ex.getMessage();
        assertEquals("Sorry, insufficient fund.", erMsg);

    }

    //Test the withdraw method of the student account when the amount of the withdrawal is less/equal to the account's balance
    @Test
    void makeWithdraw()  {
        //Carry out the account's withdrawal method on the current account, catching any possible exceptions
        try {
            account.withdraw(30.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is decreased
        assertEquals(120.00, account.getBalance());
    }

    //Make a bank overdraft after withdrawing a high amount to a current account
    @Test
    void makeAnOverdraftWithdraw(){
        //Carry out the account's withdrawal method on the current account, catching any possible exceptions
        try {
            account.withdraw(1300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is decreased
        assertEquals(-1150.00, account.getBalance());
        //Check that, as the balance of the account is now less than 0, the status of the overdraft is true
        assertTrue(account.isOverdrafted());
    }

    //Make a bank overdraft after withdrawing a high amount to a current account, then deposit an amount that makes the account no longer overdrafted
    @Test
    void makeDepositForOverdraft(){
        //Carry out the account's withdrawal method on the current account, catching any possible exceptions
        try {
            account.withdraw(1300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is decreased
        assertEquals(-1150.00, account.getBalance());
        //Check that, as the balance of the account is now less than 0, the status of the overdraft is true
        assertTrue(account.isOverdrafted());

        //Deposit an amount of money into the bank
        account.deposit(1300.00, secondAcc);
        //Check that, as the balance of the account is no longer less than 0, the status of the overdraft is false
        assertEquals(150,account.getBalance());
    }

    //Check that, after making a purchase, the updated value of a credit account returns the correct details
    @Test
    void printStatement(){
        //Make a purchase of 300 from the account to the second current account, catching any possible exceptions
        try {
            account.withdraw(300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the message gotten from printing a method's details is equal to what is expected
        assertEquals("Student Account Number: 93391634, The Arranged Overdraft amount is 1500.0, Overdraft: true balance: -150.0.",account.getDetails());
    }

    //Test the method to change the student bank's overdraft limit
    @Test
    void ableToChangeOverdraftLimit() {
        //Set the overdraft of the student bank of a new value, catching any possible exceptions
        try {
            account.setOverdraft(2000.00);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the overdraft of the account has been updated
        assertEquals(2000,account.getOverdraft());
    }

    //Test the failure condition of the student account's change overdraft method (being that the new value is equal to the old value)
    @Test
    void ChangeOverdraftLimitFailed() {
        //Check that the changeInterest method throws an exception, and store this in a variable
        Exception ex  = assertThrows(Exception.class, ()->account.setOverdraft(1500.00));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String erMsg = ex.getMessage();
        assertEquals("Sorry we can't set your required overdraft limit to be same as the original limit.", erMsg);

    }
}