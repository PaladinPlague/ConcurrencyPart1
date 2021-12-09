import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//Credit account testing class
class CreditAccountTest {

    //Variable used for testing various methods of credit account class
    CreditAccount account = new CreditAccount("344880224040782", 3000.00, .24);
    //Second credit account used to test interactions between two credit accounts
    CreditAccount CCAccount = new CreditAccount("349974796399430", 3000.00, .24);
    //Current account used to test interactions between credit accounts and other types of accounts
    CurrentAccount dummy =  new CurrentAccount("12345674", 500.00);

    //Test field declaration of credit account and the detail-returning methods of credit account
    @Test
    void generateNewCreditAccount(){
        //Assert that the credit account exists after the field for the object is declared
        assertNotNull(account);
        //Ensure that the getter methods return the correct details of the CreditAccount variable
        assertEquals("344880224040782",account.getAccountNumber());
        assertEquals("Credit Card Account",account.getType());
        assertEquals(3000,account.getCreditLimit());
        assertEquals(3000,account.getAvailableCredit());
        assertEquals(0.00,account.getBalance());
    }

    //Test the process of a credit account making a purchase
    @Test
    void makePurchase(){
        //Have a credit account making a purchase from a non-credit account, catching any possible exceptions
        try {
            account.withdraw(159.00, dummy);
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        //Test that, after the purchase, the account's details have been updated appropriately
        assertEquals(3000, account.getCreditLimit());
        assertEquals(2841.00, account.getAvailableCredit());
        assertEquals(-159.00, account.getBalance());
    }

    //Test the process of a credit account making a purchase from another credit account, which should throw an exception
    @Test
    void makePurchaseToAnotherCreditAccount(){
        //Check that the withdraw method throws an exception, and store this in a variable
        Exception ex = assertThrows(Exception.class,()->account.withdraw(159.00, CCAccount));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String exMsg = ex.getMessage();
        assertEquals("Sorry， You can't use this credit card to pay another credit card!", exMsg);
    }

    //Test the process of a credit account making a purchase from another credit account while it has insufficient funds
    @Test
    void makePurchaseWithoutEnoughMoney(){
        //Check that the withdraw method throws an exception, and store the exception in a variable
        Exception ex = assertThrows(Exception.class,()->account.withdraw(15900.00, dummy));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String exMsg = ex.getMessage();
        assertEquals("Sorry, insufficient fund.", exMsg);
    }

    //Check that, after making a purchase, the updated value of a credit account returns the correct details
    @Test
    void printStatement(){
        //Make a purchase of 300 from the credit account to the current account, catching any possible exceptions
        try {
            account.withdraw(300.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the message gotten from printing a method's details is equal to what is expected
        assertEquals("CC Account Number: 344880224040782, credit: 3000.0, available Credit: 2700.0, balance: -300.0.",account.getDetails());
    }

    //Helper function used to simulate two concurrent withdraws from this credit account to a non-credit account
    private void purchaseOfTheMonth() {
        //Make two purchases from the credit account to the current account, catching any possible exceptions
        try {
            account.withdraw(100.00, dummy);
            account.withdraw(200.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Check the account making a full return payment when the date is not overdue
    @Test
    void depositSuccessWithoutOverdueAndPaidFully(){
        //Use helper function to simulate the account making purchases
        purchaseOfTheMonth();
        //Change the payment date to be before the overdue date (the 15th of the month)
        account.setPaymentDate(LocalDate.of(2021,11,8));
        //Simulate the account receiving all its paid money back from the current account, catching any possible exceptions
        try {
            account.deposit(300.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is updated to its proper value
        assertEquals(0.00, account.getBalance());
    }

    //Check the account making a partial return payment when the date is not overdue
    @Test
    void depositSuccessWithoutOverdueAndPaidPartially(){
        //Use helper function to simulate the account making purchases
        purchaseOfTheMonth();
        //Change the payment date to be before the overdue date (the 15th of the month)
        account.setPaymentDate(LocalDate.of(2021,11,8));
        //Simulate the account receiving all its paid money back from the current account, catching any possible exceptions
        try {
            account.deposit(200.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is updated to its proper value
        assertEquals(-100.00, account.getBalance());
    }

    //Check the account making a full return payment when the date is overdue
    @Test
    void depositSuccessWithOverdueAndPaidFully(){
        //Use helper function to simulate the account making purchases
        purchaseOfTheMonth();
        //Change the payment date to be after the overdue date (the 15th of the month)
        account.setPaymentDate(LocalDate.of(2021,11,18));
        //Calculate the amount expected from the return based on the overdue monthly pay for the account and display this in terminal
        double expectedAmount = account.getBalance() + (account.getBalance() * account.monthlyInterest());
        System.out.println(Math.abs(expectedAmount));
        //Deposit the expected amount into the current account, catching any possible exceptions
        try {
            account.deposit(Math.abs(expectedAmount), dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is updated to its proper value
        assertEquals(0.00, account.getBalance());
    }

    //Check the account making a partial return payment when the date is overdue
    @Test
    void depositSuccessWithOverdueAndPaidPartially(){
        //Use helper function to simulate the account making purchases
        purchaseOfTheMonth();
        //Change the payment date to be after the overdue date (the 15th of the month)
        account.setPaymentDate(LocalDate.of(2021,11,18));
        //Calculate the amount expected from the return based on the overdue monthly pay for the account and display an edited amount of this to terminal
        double expectedAmount = account.getBalance() + (account.getBalance() * account.monthlyInterest());
        System.out.println(Math.abs(expectedAmount + 100));
        //Deposit a value different to the expected amount into the current account, catching any possible exceptions
        try {
            account.deposit(Math.abs(expectedAmount + 100), dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Check that the balance of the account is updated to its proper value
        assertEquals(-100.00, account.getBalance());
    }

    //Test the process of a credit account making a deposit from another credit account, which should throw an exception
    @Test
    void depositFailed(){
        //Use helper function to simulate the account making purchases
        purchaseOfTheMonth();

        //Change the payment date to be after the overdue date (the 15th of the month)
        account.setPaymentDate(LocalDate.of(2021,11,18));
        //Check that the deposit method throws an exception if it is used on another current account, and store the exception in a variable
        Exception ex = assertThrows(Exception.class,()->account.deposit(account.getBalance(), CCAccount));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        String exMsg = ex.getMessage();
        assertEquals("Sorry， You can't use other credit card to pay this credit card bill!", exMsg);

        //Check that the deposit method throws an exception if the other class does not have enough funds, and store the exception in a variable
        ex = assertThrows(Exception.class,()->account.deposit(900.00, dummy));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        exMsg = ex.getMessage();
        assertEquals("Sorry, insufficient fund.", exMsg);

        //Check that the deposit method throws an exception if it is trying to pay more than what it has spent before, and store the exception in a variable
        ex = assertThrows(Exception.class,()->account.deposit(400.00, dummy));
        //Store the message gotten from the exception and make sure it is what is expected from the method call
        exMsg = ex.getMessage();
        assertEquals("you can't pay more than you have spent!", exMsg);
    }
}