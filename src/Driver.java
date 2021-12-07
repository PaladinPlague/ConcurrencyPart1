//This program performs the tasks involving thread listed in the specifications
public class Driver {
    //run driver class before RSK
    //Do RSK
    //rerun diver class after RSK
    //analysis

    public static void main(String[] args) {

        //Declare the bank system where all operations are carried out
        BankSystem bank = new BankSystem();

        //Call each of the 6 tests listed
        simultaneousBalanceCheck(bank);
        checkAndChangeBalance(bank);
        simultaneousChangeAndCheck(bank);
        changeCheckAndTransfer(bank);
        insufficientFunds(bank);
        simultaneousModification(bank);
    }

    //Two account holders are trying to check the balance simultaneously
    public static void simultaneousBalanceCheck(BankSystem bank) {

        //Declare the account holders then add them to the bank
        AccountHolder holder1 = new AccountHolder("Alice", 20);
        AccountHolder holder2 = new AccountHolder("Bob", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders
        CurrentAccount acc = new CurrentAccount("11111111", 25.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the check balance Runnable case for both account holders on the only account in their list of accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 0, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 1, 0);

        //Declare threads for the runnable cases
        Thread h1C = new Thread(check1);
        Thread h2C = new Thread(check2);

        //Start the process of the threads
        h1C.start();
        h2C.start();

    }

    public static void checkAndChangeBalance(BankSystem bank) {

        //Declare the account holders before adding them to the bank
        AccountHolder holder1 = new AccountHolder("Charlie", 20);
        AccountHolder holder2 = new AccountHolder("David", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders
        CurrentAccount acc = new CurrentAccount("22222222", 25.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the Runnable case for the first account holder on checking its balance while the 2nd account holder deposits £10 into the account
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 2, 0);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 3, 0, 10.00, false);

        //Declare threads for the runnable cases
        Thread h1C = new Thread(check1);
        Thread h2D = new Thread(deposit2);

        //Start the process of the threads
        h1C.start();
        h2D.start();

    }

    //The two account holders are trying simultaneously to deposit/withdraw money & check the balance.
    public static void simultaneousChangeAndCheck(BankSystem bank) {

        //Declare the account holders before adding them to the bank
        AccountHolder holder1 = new AccountHolder("Eric", 20);
        AccountHolder holder2 = new AccountHolder("Fred", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders
        //CurrentAccount acc = new CurrentAccount("33333333", 25.00);
        CreditAccount acc = new CreditAccount("33333333",3000.00,12 );
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the Runnable cases for the account holders checking the balance of their accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 4, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 5, 0);
        //Set up the Runnable case for the 1st account depositing an amount of money while the 2nd account holder withdraws a different amount of money

        DepositWithdrawRunnable withdraw2 = new DepositWithdrawRunnable(bank, 5, 0, 7.00, true);
        DepositWithdrawRunnable deposit1 = new DepositWithdrawRunnable(bank, 4, 0, 5.00, false);

        //Set up the Runnable case for the


        //Declare threads for the runnable cases
        Thread h1C = new Thread(check1);
        Thread h2C = new Thread(check2);

        Thread h1D = new Thread(deposit1);
        Thread h2W = new Thread(withdraw2);

        //Start the process of the threads
        h1C.start();
        h2C.start();

        h2W.start();
        h1D.start();


    }

    //Same as previous test, but at the same time a bank employee is in the process of completing a money transfer in/out the account
    public static void changeCheckAndTransfer(BankSystem bank) {

        //Declare the account holders before adding them to the bank
        AccountHolder holder1 = new AccountHolder("Greg", 20);
        AccountHolder holder2 = new AccountHolder("Harry", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders
        CreditAccount acc = new CreditAccount("4444444444",3000.00,12 );
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Declare a bank employee before adding it to the bank, then add the account holder to the bank owner's accounts
        BankEmployee employee1 = new BankEmployee("Zander", "0");
        bank.addBankEmployee(employee1);
        employee1.addAccount(holder1, holder1.getAccount(0));
        employee1.addAccount(holder2, holder2.getAccount(0));

        //Set up the Runnable cases for the account holders checking the balance of their accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 6, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 7, 0);
        //Set up the Runnable case for the 1st account depositing an amount of money while the 2nd account holder withdraws a different amount of money
        DepositWithdrawRunnable deposit1 = new DepositWithdrawRunnable(bank, 6, 0, 3.50, false);
        DepositWithdrawRunnable withdraw2 = new DepositWithdrawRunnable(bank, 7, 0, 8.15, true);
        //Set up the Runnable case for the employee completing a money transfer into the account
        EmployeeTransferRunnable empDepo1 = new EmployeeTransferRunnable(bank, 0, 6, 0, 4.70, false);

        //Declare threads for the runnable cases
        Thread h1C = new Thread(check1);
        Thread h2C = new Thread(check2);
        Thread h1D = new Thread(deposit1);
        Thread h2W = new Thread(withdraw2);
        Thread e1D = new Thread(empDepo1);

        //Start the process of the threads
        h1C.start();
        h2C.start();
        h1D.start();
        h2W.start();
        e1D.start();

    }

    //There are insufficient funds to complete a withdrawal.
    public static void insufficientFunds(BankSystem bank) {

        //Declare the account holders before adding them to the bank
        AccountHolder holder1 = new AccountHolder("Isla", 20);
        AccountHolder holder2 = new AccountHolder("John", 20);
        bank.addAccountHolder(holder1);
        bank.addAccountHolder(holder2);

        //Declare an account that is held by both account holders, which has a very low opening balance
        StudentAccount acc = new StudentAccount("55555555", 2.00);
        holder1.addAccount(acc);
        holder2.addAccount(acc);

        //Set up the Runnable case for the first account holder withdrawing more than the opening balance while the second account holder deposits enough for the withdrawal to take place
        DepositWithdrawRunnable withdraw1 = new DepositWithdrawRunnable(bank, 8, 0, 3.00, true);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 9, 0, 5.00, false);

        //Declare threads for the runnable cases
        Thread h1W = new Thread(withdraw1);
        Thread h2D = new Thread(deposit2);

        //Start the process of the threads
        h2D.start();
        h1W.start();

    }

    //Two bank employees are trying simultaneously to modify the details of a bank account.
    public static void simultaneousModification(BankSystem bank) {
        //Declare the account holder before adding them it the bank
        AccountHolder holder1 = new AccountHolder("Kyle", 20);
        bank.addAccountHolder(holder1);

        //Declare an account held by the account holder as a mortgage account, which uses changeInterest method from bank employee
        Account acc1 = new MortgageAcc("66666666", 50.00, 50.00, 1);
        holder1.addAccount(acc1);

        //Declare the bank employees before adding them to the bank, then add the account holder to the bank employees
        BankEmployee employee1 = new BankEmployee("Yousef", "2");
        BankEmployee employee2 = new BankEmployee("Xena", "3");
        bank.addBankEmployee(employee1);
        bank.addBankEmployee(employee2);
        employee1.addAccount(holder1, holder1.getAccount(0));
        employee2.addAccount(holder1, holder1.getAccount(0));

        //Set up the Runnable cases for the employees changing the details of the account
        ChangeInterestRunnable modify1 = new ChangeInterestRunnable(bank, 1, 10, 0, 12);
        ChangeInterestRunnable modify2 = new ChangeInterestRunnable(bank, 2, 10, 0, 48);

        //Declare threads for the runnable cases
        Thread e1M = new Thread(modify1);
        Thread e2M = new Thread(modify2);

        //Start the process of the threads
        e1M.start();
        e2M.start();

    }
}