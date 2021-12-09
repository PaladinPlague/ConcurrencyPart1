//The driver class which is called to execute test cases involving concurrent objects of the bank
public class Driver {

    //The method that must be called initially to run the code and test the bank
    public static void main(String[] args) {

        //Declare the bank system that tests are carried out on
        BankSystem bank = new BankSystem();

        //Tests are defined via their own methods using the bank system as a reference
        //With the exception of the "insufficientFunds" test, all bank accounts constructed have a starting balance of 25.00
        simultaneousBalanceCheck(bank);
        checkAndChangeBalance(bank);
        simultaneousChangeAndCheck(bank);
        changeCheckAndTransfer(bank);
        insufficientFunds(bank);
        simultaneousModification(bank);
        depositMultipleTimes(bank);
    }

    //Two account holders are trying to check the balance simultaneously
    public static void simultaneousBalanceCheck(BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Alice", 20);
        AccountHolder holder2 = new AccountHolder("Bob", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare a mortgage account that is held by both account holders
        MortgageAcc acc = new MortgageAcc("11111111", 25.00, 1, 10);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the check balance runnable cases for both account holders checking their account's balance
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 0, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 1, 0);

        //Start the process of the threads which use the runnable cases
        (new Thread(check1)).start();
        (new Thread(check2)).start();
    }

    //One account holder is trying to check the balance while the other is depositing/withdrawing money
    public static void checkAndChangeBalance(BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Charlie", 20);
        AccountHolder holder2 = new AccountHolder("David", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare a student account that is held by both account holders
        //NOTE: as student account initially deposits 50.00 for all instances, starting balance will be 75.00
        StudentAccount acc = new StudentAccount("22222222", 25.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the runnable cases. The first account holder checks the account while the second account holder deposits £10 into the account
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 2, 0);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 3, 0, 10.00, false);

        //Start the process of the threads which use the runnable cases
        (new Thread(check1)).start();
        (new Thread(deposit2)).start();

    }

    //The two account holders are trying simultaneously to deposit/withdraw money & check the balance.
    public static void simultaneousChangeAndCheck(BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Eric", 20);
        AccountHolder holder2 = new AccountHolder("Fred", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare a savings account that is held by both account holders
        SavingAccount acc = new SavingAccount("33333333",25.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the runnable cases for the account holders checking the balance of their accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 4, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 5, 0);

        //Set up the runnable cases for the account holders transferring money to/from the bank, where the first holder deposits 5.00 while the second holder withdraws 7.00
        DepositWithdrawRunnable deposit1 = new DepositWithdrawRunnable(bank, 4, 0, 5.00, false);
        DepositWithdrawRunnable withdraw2 = new DepositWithdrawRunnable(bank, 5, 0, 7.00, true);

        //Start the process of the threads which use the runnable cases
        (new Thread(check1)).start();
        (new Thread(check2)).start();
        (new Thread(deposit1)).start();
        (new Thread(withdraw2)).start();


    }

    //Same as previous test, but at the same time a bank employee is in the process of completing a money transfer in/out the account
    public static void changeCheckAndTransfer(BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Greg", 20);
        AccountHolder holder2 = new AccountHolder("Harry", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare a credit account that is held by both account holders
        CreditAccount acc = new CreditAccount("44444444",25.00, .24);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Declare a bank employee, add it to the account and add each account holder to the list of holders for the employee
        BankEmployee employee1 = new BankEmployee("Zander", "0");
        bank.addBankEmployee(employee1);
        employee1.addAccount(holder1, holder1.getAccount(0));
        employee1.addAccount(holder2, holder2.getAccount(0));

        //Set up the runnable cases for the account holders checking the balance of their accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 6, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 7, 0);

        //Set up the runnable cases for the account holders transferring money to/from the bank, where the first holder deposits 3.50 while the second holder withdraws 8.15
        DepositWithdrawRunnable deposit1 = new DepositWithdrawRunnable(bank, 6, 0, 3.50, false);
        DepositWithdrawRunnable withdraw2 = new DepositWithdrawRunnable(bank, 7, 0, 8.15, true);

        //Set up the runnable case for the employee completing a money transfer of 4.70 deposited into the account
        EmployeeTransferRunnable empDepo1 = new EmployeeTransferRunnable(bank, 0, 6, 0, 4.70, false);

        //Start the process of the threads which use the runnable cases
        (new Thread(check1)).start();
        (new Thread(check2)).start();
        (new Thread(deposit1)).start();
        (new Thread(withdraw2)).start();
        (new Thread(empDepo1)).start();
    }

    //There are insufficient funds to complete a withdrawal.
    public static void insufficientFunds(BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Isla", 20);
        AccountHolder holder2 = new AccountHolder("John", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders, which has a very low opening balance of 2.00
        CurrentAccount acc = new CurrentAccount("55555555",2.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the runnable cases for the account holders transferring money to/from the bank, where the first holder withdraws 3.00 (more than the account started with) while the second holder deposits 5.00
        DepositWithdrawRunnable withdraw1 = new DepositWithdrawRunnable(bank, 8, 0, 3.00, true);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 9, 0, 5.00, false);

        //Start the process of the threads which use the runnable cases
        (new Thread(withdraw1)).start();
        (new Thread(deposit2)).start();

    }

    //Two bank employees are trying simultaneously to modify the details of a bank account.
    public static void simultaneousModification(BankSystem bank) {
        //Declare an account holder and add it to the bank
        AccountHolder holder = new AccountHolder("Kyle", 20);
        bank.addAccountHolder(holder);

        //Declare an account held by the account holder as a mortgage account, which uses the changeInterest method from the bank employee class
        MortgageAcc acc = new MortgageAcc("66666666",25.00, 1, 10);
        holder.addAccount(acc);

        //Declare the bank employees, add them to the bank and add the account holder to the list of holders of each employee
        BankEmployee employee1 = new BankEmployee("Yousef", "2");
        BankEmployee employee2 = new BankEmployee("Xena", "3");
        bank.addBankEmployee(employee1);
        bank.addBankEmployee(employee2);
        employee1.addAccount(holder, holder.getAccount(0));
        employee2.addAccount(holder, holder.getAccount(0));

        //Set up the runnable cases for the employees changing the interest of the account to different values
        ChangeInterestRunnable modify1 = new ChangeInterestRunnable(bank, 1, 10, 0, 12);
        ChangeInterestRunnable modify2 = new ChangeInterestRunnable(bank, 2, 10, 0, 48);

        //Start the process of the threads which use the runnable cases
        (new Thread(modify1)).start();
        (new Thread(modify2)).start();

    }

    //Three account holders are simultaneously trying to check the balance of an account and deposit money into it
    //This testing method is not related to a testing scenario given in the document, but was included for testing mutation operators
    public static void depositMultipleTimes (BankSystem bank) {

        //Declare the account holders and add them to the bank
        AccountHolder holder1 = new AccountHolder("Sonic", 20);
        AccountHolder holder2 = new AccountHolder("Knuckles", 20);
        AccountHolder holder3 = new AccountHolder("Tails", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);
        bank.addAccountHolder(holder3);

        //Declare a current account that is held by all account holders in test
        CurrentAccount acc = new CurrentAccount("123456789",100.0);
        holder1.addAccount(acc);
        holder2.addAccount(acc);
        holder3.addAccount(acc);

        //Set up the runnable cases for the account holders checking the balance of the
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 11, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 12, 0);
        CheckBalanceRunnable check3 = new CheckBalanceRunnable(bank, 13, 0);

        //Set up the runnable cases for the account holders depositing different amounts of money into the account
        DepositWithdrawRunnable deposit1 = new DepositWithdrawRunnable(bank, 11, 0, 20, false);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 12, 0, 30, false);
        DepositWithdrawRunnable deposit3 = new DepositWithdrawRunnable(bank, 13, 0, 40, false);

        //Start the process of the threads which use the runnable cases
        (new Thread(check1)).start();
        (new Thread(check2)).start();
        (new Thread(check3)).start();
        (new Thread(deposit1)).start();
        (new Thread(deposit2)).start();
        (new Thread(deposit3)).start();
    }
}