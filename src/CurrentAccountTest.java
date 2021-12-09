import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//Current account testing class
class CurrentAccountTest {

    //Variable used for testing various methods of current account class
    CurrentAccount account = new CurrentAccount("56357771", 500.00);
    //Second credit account used to test interactions between current account and other accounts
    CurrentAccount secondAcc = new CurrentAccount("30569309", 700.00);

    //Test field declaration of current account and the detail-returning methods of current account
    @Test
    void generateNewCurrentAccount(){
        //Assert that the credit account exists after the field for the object is declared
        assertNotNull(account);
        //Ensure that the getter methods return the correct details of the CurrentAccount variable
        assertEquals("56357771",account.getAccountNumber());
        assertEquals("Current Account",account.getType());
        assertEquals(500.00,account.getBalance());
    }

    //Test the deposit method of the current account and assert that its balance is properly updated
    @Test
    void makeDeposit(){
        //Deposit to the first current account from the second current account
        account.deposit(50.00, secondAcc);
        //Check that the balance of the account is increased
        assertEquals(550,account.getBalance());
    }

    //Test the withdraw method of the current account when the amount of the withdrawal is higher than the account's balance
    @Test
    void failedToMakeWithdraw(){
        //Assert that withdrawing with a high balance throws an exception, and store this in a variable
        Exception ex  = assertThrows(Exception.class, ()->account.withdraw(600.00,secondAcc));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String erMsg = ex.getMessage();
        assertEquals("ERROR: insufficient funds.", erMsg);

    }

    //Test the withdraw method of the current account when the amount of the withdrawal is less/equal to the account's balance
    @Test
    void makeWithdraw()  {
        //Carry out the account's withdrawal method on the second current account, catching any possible exceptions
        try {
            account.withdraw(300.00, secondAcc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is decreased
        assertEquals(200.00, account.getBalance());
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
        assertEquals("Current Account Number: 56357771, balance: 200.0.",account.getDetails());
    }
}