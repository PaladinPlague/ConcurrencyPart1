/**
 * This program performs the tasks involving thread listed in the specifications
 */
public class Driver {

    public static void main(String[] args) {

        //Implement Runnable objects here: Objects that would call methods of other objects at delayed occassions
        //DISCUSS: We need to have the runnable objects ready for this. These would be classess where methods are called at regular intervals.
        //Current thoughts: Create new classes that take an account and call a function in a basic method "run()"
        //This may result in lots of classes being called, however

        //Declare Thread objects as threads based on runnable objects

        //Call the start method of each thread object

        //Call each of the 6 tests listed
        simultaneousBalanceCheck();
        checkAndChangeBalance();
        simultaneousChangeAndCheck();
        changeCheckAndTransfer();
        insufficientFunds();
        simultaneousModification();
    }

    //Two account holders are trying to check the balance simultaneously
    public static void simultaneousBalanceCheck() {
        AccountHolder accHolder1 = new AccountHolder("Alice", 20);
        AccountHolder accHolder2 = new AccountHolder("Bob", 22);

        CurrentAccount acc = new CurrentAccount("11111111", 25.00);
        accHolder1.addAccount(acc);
        accHolder2.addAccount(acc);

        CheckBalanceRunnable check1 = new CheckBalanceRunnable(accHolder1, 0);
        CheckBalanceRunnable check2 = new CheckBalanceRunnable(accHolder2, 0);

        Thread accH1T = new Thread(check1);
        Thread accH2T = new Thread(check2);

        accH1T.start();
        accH2T.start();
    }

    //One account holder is trying to check the balance while the other is depositing/withdrawing money.
    public static void checkAndChangeBalance() {

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
