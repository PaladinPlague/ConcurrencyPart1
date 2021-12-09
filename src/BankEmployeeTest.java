import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Bank employee testing class
public class BankEmployeeTest {

    //Creates one of each type of account and prints its details
    @Test
    public void getAccDetails() {
        //Construct the account holder
        AccountHolder persAcc = new AccountHolder("John", 30);
        //Construct the current account and add it to the list of the accounts of the account holder
        CurrentAccount curAcc = new CurrentAccount("1", 400.00);
        persAcc.addAccount(curAcc);
        //Construct the mortgage account and add it to the list of the accounts of the account holder
        MortgageAcc morAcc = new MortgageAcc("2", 100000.00, 1.0, 10);
        persAcc.addAccount(morAcc);
        //Construct the savings account and add it to the list of the accounts of the account holder
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);
        //Construct the credit account and add it to the list of the accounts of the account holder
        CreditAccount credAcc = new CreditAccount("4",  10.00, 1.0);
        persAcc.addAccount(credAcc);
        //Construct the student account and add it to the list of the accounts of the account holder
        StudentAccount studAcc = new StudentAccount("5", 1000.00);
        persAcc.addAccount(studAcc);
        //Check that the person holder has 5 accounts (one of each type)
        assertEquals(5, persAcc.getSize());

        //Construct the bank employee, add the account holder to it and allow it to print details of all accounts held by the account owner
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        bankEmp.addAccountHolder(persAcc);
        bankEmp.printAllAccounts(persAcc);
        //For an account not overseen by the employee, an error should be displayed
        bankEmp.printAllAccounts(new AccountHolder("James", 30));
    }

    //Creates a saving account under a customer and prints the details
    @Test
    public void printSingleAcc() {
        //Construct an account holder, who will hold a savings account
        AccountHolder persAcc = new AccountHolder("John", 30);
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);

        //Construct a bank employee to hold the details of the customer, then print the details of the account they hold
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        bankEmp.printOneAccount(persAcc, "3");
    }

    //Creates a saving account BUT should not print any details
    @Test
    public void searchFail() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a savings account to their list of accounts
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        SavingAccount savAcc = new SavingAccount("3", 7000.00);
        persAcc.addAccount(savAcc);

        //The account does not exist and should print a message regarding this
        assertEquals(null, bankEmp.getCustAccount(persAcc).getAccount("7"));
        bankEmp.printOneAccount(persAcc, "7");
    }

    //Ensures the deposit with an AccountHolder works
    @Test
    public void depositAccount() throws Exception {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a savings account to their list of accounts
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        persAcc.addAccount(curr);

        //Check that the methods of the bank employee on the account provide expected results
        bankEmp.printOneAccount(persAcc, "1234");
        assertEquals(1000, curr.getBalance());

        //Deposit a specified amount of money into the account, and ensure that the results are updated as expected
        bankEmp.getCustAccount(persAcc).getAccount("1234").deposit(100.0, curr);
        assertEquals(1100, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //Ensures the withdrawal with an AccountHolder works
    @Test
    public void withdrawAccount() throws Exception {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a current account to their list of accounts
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        persAcc.addAccount(curr);

        //Check that the methods of the bank employee on the account provide expected results
        bankEmp.printOneAccount(persAcc, "1234");
        assertEquals(1000, curr.getBalance());

        //Withdraw a specified amount of money into the account, and ensure that the results are updated as expected
        bankEmp.getCustAccount(persAcc).getAccount("1234").withdraw(100.0, curr);
        assertEquals(900, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //Adds multiple account to an Account Holder
    @Test
    public void addingAccounts() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        //Construct an account holder
        AccountHolder persAcc = bankEmp.createCustAccount("John", 30);
        //Check that the holder currently holds no accounts
        assertEquals(0,persAcc.getSize());

        //Create a current account, then allow the employee to add this to the list of accounts of the account holder,
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(persAcc, curr);
        //Check that there is now one account held by the account holder
        assertEquals(1, persAcc.getSize());

        //Create a savings account, then allow the employee to add this to the list of accounts of the account holder,
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        bankEmp.addAccount(persAcc, sav);
        //Check that there are now two accounts held by the account holder
        assertEquals(2, persAcc.getSize());
    }

    //Ensures the account is not added if the account holder is not old enough
    @Test
    public void underageAcc() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and try adding a current account to their list of accounts
        AccountHolder persAcc = new AccountHolder("Billy", 13);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(persAcc, curr);
        //As the holder is underage, they should not have the account added to their list of accounts
        assertEquals(0, persAcc.getSize());
    }

    //Checks to make sure the account that a bank employee creates actually exist
    @Test
    public void creatingAccounts() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a current account to their list of accounts
        AccountHolder acc1 = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.addAccount(acc1, curr);

        //Construct a second account holder that is created by the employee, and add a savings account and a student account to their list of accounts
        AccountHolder acc2 = bankEmp.createCustAccount("Teddy", 19);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        StudentAccount stud = new StudentAccount("5", 1000.00);
        bankEmp.addAccount(acc2, sav);
        bankEmp.addAccount(acc2, stud);

        //Check that the first account holder holds one account while the second account holder holds two accounts
        assertEquals(1, acc1.getSize());
        assertEquals(2, acc2.getSize());
    }

    //Removes multiple accounts from a user
    @Test
    public void removeBankAccounts() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a current account and a savings account to their list of accounts
        AccountHolder acc = bankEmp.createCustAccount("John", 30);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        bankEmp.addAccount(acc, curr);
        bankEmp.addAccount(acc, sav);
        //Check that the holder currently holds two accounts
        assertEquals(2, acc.getSize());

        //Delete the current account from the holder's list of accounts, and check that it only holds one account afterwards
        bankEmp.deleteAccount(acc, curr);
        assertEquals(1, acc.getSize());

        //Delete the savings account from the holder's list of accounts, and check that it does not hold any accounts afterwards
        bankEmp.deleteAccount(acc, sav);
        assertEquals(0, acc.getSize());
    }

    //Deletes a customer's account
    @Test
    public void singleDelete() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        //Allow the employee to create a new account holder object
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        //As the employee oversees the account holder, the delete method should be successful
        assertTrue(bankEmp.deleteCustAccount(acc));
    }

    //Delete an account with some bank account contained in it
    @Test
    public void fullDelete() {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder that is created by the employee, and add a savings account, current account and credit account to their list of accounts
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);
        SavingAccount sav = new SavingAccount("2345", 1000.00);
        acc.addAccount(sav);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        acc.addAccount(curr);
        CreditAccount cred = new CreditAccount("4", 10.00, 1.0);
        acc.addAccount(cred);

        //Check that the account holder holds three accounts, and that the delete process on the account holder is successful
        assertEquals(3, acc.getSize());
        assertTrue(bankEmp.deleteCustAccount(acc));
    }

    //Changes the interest rates of the three applicable account and then checks for a failure
    @Test
    public void updateInterest() {
        //Construct an employee and an account holder created by the employee
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
        //Construct an employee and an account holder created by the employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");
        AccountHolder acc = bankEmp.createCustAccount("Jim", 52);

        //Change the student account overdraft
        StudentAccount stud = new StudentAccount("5", 1000.00);
        acc.addAccount(stud);
        assertEquals(1500, stud.getOverdraft());
        bankEmp.changeOverdraftLimit(stud, 2000);
        assertEquals(2000, stud.getOverdraft());

        //Attempting to change overdraft limit of a current account should print an error
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        acc.addAccount(curr);
        bankEmp.changeOverdraftLimit(curr, 2000);
    }

    //Ensures the deposit with an Account Number works
    @Test
    public void depositString() throws Exception {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder created by the employee, and add this to the list of accounts by the owner via the employee
        AccountHolder persAcc = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.getCustAccount(persAcc).addAccount(curr);
        //Check the details of the account held by the customer
        assertEquals(1000, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");

        //Deposit 100 into the account held by the customer via employee, and check that the relevant variables are appropriately updated
        bankEmp.deposit(persAcc, "1234", 100);
        assertEquals(1100, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
    }

    //Ensures the withdrawal with an Account Number works
    @Test
    public void withdrawString() throws Exception {
        //Construct an employee
        BankEmployee bankEmp = new BankEmployee("Tim", "98765");

        //Construct an account holder created by the employee, and add this to the list of accounts by the owner via the employee
        AccountHolder persAcc = bankEmp.createCustAccount("Jim", 52);
        CurrentAccount curr = new CurrentAccount("1234", 1000.00);
        bankEmp.getCustAccount(persAcc).addAccount(curr);
        //Check the details of the account held by the customer
        assertEquals(1000, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");

        //Withdraw 100 from the account held by the customer via employee, and check that the relevant variables are appropriately updated
        bankEmp.withdraw(persAcc, "1234", 100);
        assertEquals(900, curr.getBalance());
        bankEmp.printOneAccount(persAcc, "1234");
    }
}