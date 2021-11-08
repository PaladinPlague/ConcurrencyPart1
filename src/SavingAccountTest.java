import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SavingAccountTest {

    SavingAccount saving = new SavingAccount("SA000", 20.0);
    CurrentAccount test = new CurrentAccount("ZA000", 20.0, 0);

    @Test
    void returnBalance(){
        assertEquals(20,saving.getBalance());
    }

    @Test
    void depositCheck() throws Exception {
        saving.deposit(40.0, test);
        assertEquals(60.0, saving.getBalance());
    }

    @Test
    void withdrawDrawCheck() throws Exception {
        saving.withdraw(10.0,saving, 24);
        assertEquals(10, saving.getBalance());
    }

    //----------------------------------------------------------

    SavingAccount saving2 = new SavingAccount("SA000", 0.5);

    @Test
    void returnBalanceInvalid(){
        assertEquals(0,saving2.getBalance());
    }
    @Test
    void depositCheckInvalid() throws Exception {
        saving2.deposit(0.2, test);
        assertEquals(0, saving2.getBalance());
    }
    @Test
    void depositCheckValid() throws Exception {
        saving2.deposit(2.0, test);
        assertEquals(2.0, saving2.getBalance());
    }
    @Test
    void withdrawCheckInvalid() throws Exception {
        saving.withdraw(19.5,saving, 24);
        assertEquals(0, saving.getBalance());
    }

    //----------------------------------------------------------

    @Test
    void checkInterest() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+1,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        assertEquals(20.04,saving.getBalance());
    }
    @Test
    void checkInterest2() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+3,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        assertEquals(20.12,saving.getBalance());
    }

    @Test
    void checkInterestDeposit() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        saving.deposit(30.0, test);
        assertEquals(50.08,saving.getBalance());
    }

    @Test
    void checkInterestWithdraw() throws Exception {
        saving.changeDate(LocalDate.of(saving.everyYear.getYear()+2,saving.everyYear.getMonthValue(),saving.everyYear.getDayOfMonth()));
        saving.withdraw(10.0,saving, 24);
        assertEquals(10.08,saving.getBalance());
    }


}