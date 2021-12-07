import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankEmployeeTest {

    //Creates one of each type of account and prints its details
    @Test
    public void getAccDetails() {
        AccountHolder persAcc = new AccountHolder("John", 30);
        CurrentAccount curAcc = new CurrentAccount("1", 400.00);
        persAcc.addAccount(curAcc);
        MortgageAcc morAcc = new MortgageAcc("2", 100000.00, 1.0, 10);
        persAcc.addAccount(morAcc);
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);
        CreditAccount credAcc = new CreditAccount("4",  10.00, 1.0);
        persAcc.addAccount(credAcc);
        StudentAccount studAcc = new StudentAccount("5", 1000.00);
        persAcc.addAccount(studAcc);
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        assertEquals(5, persAcc.getSize());
        bankEmp.printAllAccounts(persAcc);
    }

    //Creates a saving account under a customer and prints the details
    @Test
    public void printSingleAcc() {
        AccountHolder persAcc = new AccountHolder("John", 30);
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        bankEmp.printOneAccount(persAcc, "3");
    }

    //Again, creates a saving account BUT should not print any details
    @Test
    public void searchFail() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);
        //The account does not exist and should print as such
        assertEquals(null, bankEmp.getCustAccount(persAcc).getAccount("7"));
        bankEmp.printOneAccount(persAcc, "7");
    }

    //Ensures the deposit with an AccountHolder works
    @Test
    public void depositAccount() throws Exception {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        persAcc.addAccount(curr);
        bankEmp.printOneAccount(persAcc, "1234");
        assertEquals(1000, curr.getBalance());
        bankEmp.getCustAccount(persAcc).getAccount("1234").deposit(100.0, curr);
        assertEquals(1100, curr.getBalance());
        System.out.println();
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //Ensures the withdrawal with an AccountHolder works
    @Test
    public void withdrawAccount() throws Exception {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        persAcc.addAccount(curr);
        bankEmp.printOneAccount(persAcc, "1234");
        assertEquals(1000, curr.getBalance());
        bankEmp.getCustAccount(persAcc).getAccount("1234").withdraw(100.0, curr);
        assertEquals(900, curr.getBalance());
        System.out.println();
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //NOTE: THE ABOVE TESTS WERE WRITTEN AND USED BEFORE BANK EMPLOYEE COULD ADD ACCOUNTS

    //Adds multiple account to an Account Holder
    @Test
    public void addingAccounts() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        assertEquals(0,persAcc.getSize());
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(persAcc, curr);
        assertEquals(1, persAcc.getSize());
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        bankEmp.addAccount(persAcc, sav);
        assertEquals(2, persAcc.getSize());
    }

    //Ensures the account is not added if the account holder is not old enough
    @Test
    public void underageAcc() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = new AccountHolder("Billy", 13);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(persAcc, curr);
        assertEquals(0, persAcc.getSize());
    }

    //Checks to make sure the account that a bank employee creates actually exist
    @Test
    public void creatingAccounts() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc1 = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(acc1, curr);
        AccountHolder acc2 = bankEmp.createCustAccount("Teddy", 19);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        StudentAccount stud = new StudentAccount("5", 1000.00);
        bankEmp.addAccount(acc2, sav);
        bankEmp.addAccount(acc2, stud);
        assertEquals(1, acc1.getSize());
        assertEquals(2, acc2.getSize());
    }

    //Removes multiple accounts from a user
    @Test
    public void removeBankAccounts() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        bankEmp.addAccount(acc, curr);
        bankEmp.addAccount(acc, sav);
        assertEquals(2, acc.getSize());
        bankEmp.deleteAccount(acc, curr);
        assertEquals(1, acc.getSize());
        bankEmp.deleteAccount(acc, sav);
        assertEquals(0, acc.getSize());
    }

    //Deletes a customer's account
    @Test
    public void singleDelete() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        assertTrue(bankEmp.deleteCustAccount(acc));
    }

    //Delete an account with some bank account contained in it
    @Test
    public void fullDelete() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        acc.addAccount(sav);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        acc.addAccount(curr);
        CreditAccount cred = new CreditAccount("4", 10.00, 1.0);
        acc.addAccount(cred);
        assertEquals(3, acc.getSize());
        assertTrue(bankEmp.deleteCustAccount(acc));
    }

    //Changes the interest rates of the three applicable account and then checks for a failure
    @Test
    public void updateInterest() {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        //Change saving account interest rate
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        bankEmp.addAccount(acc, sav);
        assertEquals(0.2, sav.getInterestRate());
        bankEmp.changeInterest(sav, 1.0);
        assertEquals(1, sav.getInterestRate());
        //Change saving account interest rate
        CreditAccount cred = new CreditAccount("4", 10.00, 1.0);
        bankEmp.addAccount(acc, cred);
        assertEquals(1.0, cred.getAPR());
        bankEmp.changeInterest(cred, 1.5);
        assertEquals(1.5, cred.getAPR());
        //Change saving account interest rate
        MortgageAcc morAcc = new MortgageAcc("2", 100000.00, 1.0, 10);
        bankEmp.addAccount(acc, morAcc);
        //Due to the way Mortgage is set up, the interests are considered as % so much be treated as such
        assertEquals(0.01, morAcc.getAnnInterest());
        bankEmp.changeInterest(morAcc, 1.5);
        assertEquals(0.015, morAcc.getAnnInterest());
        //Try to change saving account interest rate
        StudentAccount stud = new StudentAccount("5", 1000.00);
        bankEmp.addAccount(acc, stud);
        bankEmp.changeInterest(stud, 1.0);
    }

    //Changes the overdraft of a student account and checks for an error
    @Test
    public void changeOverdraft() throws Exception {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        //Change the student account overdraft
        StudentAccount stud = new StudentAccount("5", 1000.00);
        acc.addAccount(stud);
        assertEquals(1500, stud.getOverdraft());
        bankEmp.changeOverdraftLimit(stud, 2000);
        assertEquals(2000, stud.getOverdraft());
        //Should print an error
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        acc.addAccount(curr);
        bankEmp.changeOverdraftLimit(curr, 2000);
    }

    //Ensures the deposit with an Account Number works
    @Test
    public void depositString() throws Exception {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.getCustAccount(persAcc).addAccount(curr);
        assertEquals(1000, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
        bankEmp.deposit(persAcc, "1234", 100);
        assertEquals(1100, curr.getBalance());
        System.out.println();
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //Ensures the withdrawal with an Account Number works
    @Test
    public void withdrawString() throws Exception {
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder persAcc = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.getCustAccount(persAcc).addAccount(curr);
        assertEquals(1000, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
        bankEmp.withdraw(persAcc, "1234", 100);
        assertEquals(900, curr.getBalance());
        System.out.println();
        bankEmp.printOneAccount(persAcc, "1234");
    }

}
