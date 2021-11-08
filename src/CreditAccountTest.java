import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreditAccountTest {

    CreditAccount account = new CreditAccount("344880224040782", 0.00,3000.00, 1234,.24);
    CreditAccount CCAccount = new CreditAccount("349974796399430", 0.00,3000.00, 4321,.24);
    CurrentAccount dummy =  new CurrentAccount("12345674", 500.00, 9876);

    @Test
    void generateNewCreditAccount(){

        assertNotNull(account);
        assertEquals("344880224040782",account.getAccountNumber());
        assertEquals("Credit Card Account",account.getType());
        assertEquals(3000,account.getCreditLimit());
        assertEquals(3000,account.getAvailableCreditCredit());
        assertEquals(0.00,account.getBalance());
    }

    @Test
    void makePurchase(){
        try {
            account.withdraw(159.00, dummy, 1234);
        } catch (Exception e) {
            System.out.println("Error occurred ");
        }

        assertEquals(3000, account.getCreditLimit());
        assertEquals(2841.00, account.getAvailableCreditCredit());
        assertEquals(-159.00, account.getBalance());
        //assertEquals(359.00, dummy.getBalance());
    }

    @Test
    void makePurchaseWithWrongPin(){
        Exception ex = assertThrows(Exception.class,()->account.withdraw(159.00, dummy, 4321));
        String expectedExMsg = "Denied Access: Incorrect Pin";
        String exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);
    }

    @Test
    void makePurchaseToAnotherCreditAccount(){
        Exception ex = assertThrows(Exception.class,()->account.withdraw(159.00, CCAccount, 1234));
        String expectedExMsg = "Sorry， You can't use this credit card to pay another credit card!";
        String exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);
    }


    @Test
    void makePurchaseWithoutEnoughMoney(){
        Exception ex = assertThrows(Exception.class,()->account.withdraw(15900.00, dummy, 1234));
        String expectedExMsg = "Sorry, insufficient fund.";
        String exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);
    }


    @Test
    void updatePinFailed(){
        Exception ex = assertThrows(Exception.class,()->account.updatePin(4321, 2140));
        String expectedExMsg = "Sorry error occurs, failed to update your PIN.";
        String exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);
    }

    @Test
    void updatePinSuccess(){

        try {
            account.updatePin(1234, 2140);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(2140, account.getPin());
    }


    @Test
    void printStatement(){
        try {
            account.withdraw(100.00, dummy, 1234);
            account.withdraw(200.00, dummy, 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals("CC Account Number: 344880224040782, credit: 3000.0, available Credit: 2700.0, balance: -300.0, Transactions：[].",account.toString());
    }

    private void purchaseOfTheMonth() {
        try {
            account.withdraw(100.00, dummy, 1234);
            account.withdraw(200.00, dummy, 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    void depositSuccessWithoutOverdueAndPaidFully(){

        purchaseOfTheMonth();
        account.setPaymentDate(LocalDate.of(2021,11,8));
        try {
            account.deposit(300.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0.00, account.getBalance());

    }


    @Test
    void depositSuccessWithoutOverdueAndPaidPartially(){

        purchaseOfTheMonth();

        account.setPaymentDate(LocalDate.of(2021,11,8));
        try {
            account.deposit(200.00, dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-100.00, account.getBalance());

    }


    @Test
    void depositSuccessWithOverdueAndPaidFully(){

        purchaseOfTheMonth();

        account.setPaymentDate(LocalDate.of(2021,11,18));
        double expectedAmount = account.getBalance() + (account.getBalance() * account.monthlyInterest());

        System.out.println(Math.abs(expectedAmount));
        try {
            account.deposit(Math.abs(expectedAmount), dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(0.00, account.getBalance());

    }

    @Test
    void depositSuccessWithOverdueAndPaidPartially(){

        purchaseOfTheMonth();

        account.setPaymentDate(LocalDate.of(2021,11,18));
        double expectedAmount = account.getBalance() + (account.getBalance() * account.monthlyInterest());

        System.out.println(Math.abs(expectedAmount + 100));
        try {
            account.deposit(Math.abs(expectedAmount + 100), dummy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(-100.00, account.getBalance());

    }



    @Test
    void depositFailed(){

        purchaseOfTheMonth();

        account.setPaymentDate(LocalDate.of(2021,11,18));
        Exception ex = assertThrows(Exception.class,()->account.deposit(account.getBalance(), CCAccount));
        String expectedExMsg = "Sorry， You can't use other credit card to pay this credit card bill!";
        String exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);

        ex = assertThrows(Exception.class,()->account.deposit(900.00, dummy));
        expectedExMsg = "Sorry, insufficient fund.";
        exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);


        ex = assertThrows(Exception.class,()->account.deposit(400.00, dummy));
        expectedExMsg = "you can't pay more than you have spent!";
        exMsg = ex.getMessage();
        assertEquals(expectedExMsg, exMsg);

    }



}