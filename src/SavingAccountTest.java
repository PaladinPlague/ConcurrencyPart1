import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/*
TESTING SAVING ACCOUNT
Made by Suleman Akhter
 */


class SavingAccountTest {


    SavingAccount saving = new SavingAccount("SA000", 20.0); // Initial Valid Saving Account
    CurrentAccount test = new CurrentAccount("ZA000", 20.0); // Withdraws to this account

    //Checks whether returnBalance() with valid values work
    @Test
    void returnBalance(){
        assertEquals(20,saving.getBalance());
    }

    //Checks whether deposit() with valid values work
    @Test
    void depositCheck() throws Exception {
        saving.deposit(40.0, test);
        assertEquals(60.0, saving.getBalance());
    }

    //Checks whether withdraw() works with valid values work
    @Test
    void withdrawDrawCheck() throws Exception {
        saving.withdraw(10.0,test);
        assertEquals(10, saving.getBalance());
    }

    //----------------------------------------------------------

    SavingAccount saving2 = new SavingAccount("SA002", 0.5);

    //Checks whether returnBalance() with invalid values work
    @Test
    void returnBalanceInvalid(){
        assertEquals(0,saving2.getBalance());
    }
    //Checks whether deposit() with invalid values work
    @Test
    void depositCheckInvalid() throws Exception {
        saving2.deposit(0.2, test);
        assertEquals(0, saving2.getBalance());
    }
    //Checks whether deposit() with an invalid initial balance will cause the account to be valid when u deposit an valid amount
    @Test
    void depositCheckValid() throws Exception {
        saving2.deposit(2.0, test);
        assertEquals(2.0, saving2.getBalance());
    }
    //Checks whether withdraw() with an valid initial balance will cause the account to be invalid  when u withdraw the balance to be <1
    @Test
    void withdrawCheckInvalid() throws Exception {
        saving.withdraw(19.5,test);
        assertEquals(0, saving.getBalance());
    }

    //----------------------------------------------------------

    //Checks if interest works for 1 year in the getBalance method
    @Test
    void checkInterest() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+1,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        assertEquals(20.04,saving.getBalance());
    }
    //Checks if interest works for 2 years in the getBalance method
    @Test
    void checkInterest2() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+3,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        assertEquals(20.12,saving.getBalance());
    }
    //Checks if interest works for 2 years in the deposit method
    @Test
    void checkInterestDeposit() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        saving.deposit(30.0, test);
        assertEquals(50.08,saving.getBalance());
    }
    //Checks if interest works for 2 years in the withdraw method
    @Test
    void checkInterestWithdraw() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        saving.withdraw(10.0,test);
        assertEquals(10.08,saving.getBalance());
    }
}