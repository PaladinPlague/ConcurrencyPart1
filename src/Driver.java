//This program performs the tasks involving thread listed in the specifications
public class Driver {

    public static void main(String[] args) {

        //Declare the bank system where all operations are carried out
        BankSystem bank = new BankSystem();

        //Declare the account holders before adding them to the bank
        AccountHolder accHolder1 = new AccountHolder("Alice", 20);
        AccountHolder accHolder2 = new AccountHolder("Bob", 20);
        bank.addAccountHolder(accHolder1);
        bank.addAccountHolder(accHolder2);

        //Declare an account that is held by both account holders
        CurrentAccount acc = new CurrentAccount("11111111", 25.00);
        accHolder1.addAccount(acc);
        accHolder2.addAccount(acc);

        //Call each of the 6 tests listed
        simultaneousBalanceCheck(bank);
        checkAndChangeBalance(bank);
        //simultaneousChangeAndCheck();
        //changeCheckAndTransfer();
        //insufficientFunds();
        //simultaneousModification();
    }

    //Two account holders are trying to check the balance simultaneously
    public static void simultaneousBalanceCheck(BankSystem bank) {


        //Set up the Runnable case for both account holders on the only account in their list of accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 0, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(bank, 1, 0);

        //Declare threads for the runnable cases
        Thread accH1T = new Thread(check1);
        Thread accH2T = new Thread(check2);

        //Use synchronized block so that the order of these threads are isolated from threads in other methods
        synchronized (bank) {
            //Start the process of the threads
            accH1T.start();
            accH2T.start();
        }

    }

    public static void checkAndChangeBalance(BankSystem bank) {

        //Set up the Runnable case for both account holders on the only account in their list of accounts
        CheckBalanceRunnable check1 = new CheckBalanceRunnable(bank, 0, 0);
        DepositWithdrawRunnable deposit2 = new DepositWithdrawRunnable(bank, 1, 0, 10.00, false);

        //Declare threads for the runnable cases
        Thread accH1T = new Thread(check1);
        Thread accH2D = new Thread(deposit2);

        //Use synchronized block so that the order of these threads are isolated from threads in other methods
        synchronized (bank) {
            //Start the process of the threads
            accH1T.start();
            accH2D.start();
        }

    }

    //The two account holders are trying simultaneously to deposit/withdraw money & check the balance.
    public static void simultaneousChangeAndCheck() {

    }

    //Same as previous test, but at the same time a bank employee is in the process of completing a money transfer in/out the account
    public static void changeCheckAndTransfer() {

    }

    //There are insufficient funds to complete a withdraw.
    public static void insufficientFunds() {

    }

    //Two bank employees are trying simultaneously to modify the details of a bank account.
    public static void simultaneousModification() {

    }
}
