import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//Savings account testing class
//Made by Suleman Akhter
class SavingAccountTest {

    //Variable used for testing various methods of savings account class
    SavingAccount saving = new SavingAccount("SA000", 20.0);
    //Second savings account used for testing various methods of savings account class when declared with initially invalid value (less than 1)
    SavingAccount saving2 = new SavingAccount("SA002", 0.5);
    //Current account used to test interactions between savings account and other types of accounts
    CurrentAccount test = new CurrentAccount("ZA000", 20.0);

    //Checks whether returnBalance() with valid values work
    @Test
    void returnBalance(){
        assertEquals(20,saving.getBalance());
    }

    //Checks whether deposit() with valid values work
    @Test
    void depositCheck() throws Exception {
        saving.deposit(40.0, test);
        //Check if appropriate update to balance of account was made
        assertEquals(60.0, saving.getBalance());
    }

    //Checks whether withdraw() works with valid values work
    @Test
    void withdrawDrawCheck() throws Exception {
        saving.withdraw(10.0,test);
        //Check if appropriate update to balance of account was made
        assertEquals(10, saving.getBalance());
    }

    //Checks whether returnBalance() with invalid values work
    @Test
    void returnBalanceInvalid(){
        //0 is result if balance is invalid (less than 1)
        assertEquals(0,saving2.getBalance());
    }
    //Checks whether deposit() with invalid values work
    @Test
    void depositCheckInvalid() throws Exception {
        saving2.deposit(0.2, test);
        //0 is result if balance is invalid (less than 1)
        assertEquals(0, saving2.getBalance());
    }
    //Checks whether deposit() with an invalid initial balance will cause the account to be valid when u deposit an valid amount
    @Test
    void depositCheckValid() throws Exception {
        saving2.deposit(2.0, test);
        //As balance is now greater than 1, it should be the output of the method
        assertEquals(2.0, saving2.getBalance());
    }

    //Checks whether withdraw() with an valid initial balance will cause the account to be invalid  when u withdraw the balance to be <1
    @Test
    void withdrawCheckInvalid() throws Exception {
        saving.withdraw(19.5,test);
        //0 is result if balance is invalid (less than 1)
        assertEquals(0, saving.getBalance());
    }

    //Checks if interest works for 1 year in the getBalance method
    @Test
    void checkInterest() throws Exception {
        //Set date forward by 1 year
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+1,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        //Interest should be added to balance based on number of years passed
        assertEquals(20.04,saving.getBalance());
    }

    //Checks if interest works for 2 years in the getBalance method
    @Test
    void checkInterest2() throws Exception {
        //Set date forward by 2 years
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+3,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        //Interest should be added to balance based on number of years passed
        assertEquals(20.12,saving.getBalance());
    }

    //Checks if interest works for 2 years in the deposit method
    @Test
    void checkInterestDeposit() throws Exception {
        //Set date forward by 2 years
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        //Deposit amount of money to account, which should also be updated based on interst from year passed
        saving.deposit(30.0, test);
        //Check that added interest and deposited money have been added to account
        assertEquals(50.08,saving.getBalance());
    }

    //Checks if interest works for 2 years in the withdraw method
    @Test
    void checkInterestWithdraw() throws Exception {
        //Set date forward by 2 years
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        //Withdraw amount of money from account, which should also be updated based on interst from year passed
        saving.withdraw(10.0,test);
        //Check that added interest and deposited money have been added to account
        assertEquals(10.08,saving.getBalance());
    }
}