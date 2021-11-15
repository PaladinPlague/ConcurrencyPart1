//An account holder checks the balance of one of their accounts
public class CheckBalanceRunnable implements Runnable {
    private static final int DELAY = 1;
    private AccountHolder accHolder;
    private int accIndex;

    public CheckBalanceRunnable(AccountHolder holder, int index) {
        accHolder = holder;
        accIndex = index;
    }

    //Thread process of this method
    public void run() {
        try
        {
            //Get balance from account
            double balance = accHolder.getAccount(accIndex).getBalance();
            //Show the balance along with the name of the account holder for this thread
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolder.getName() + " gets balance " + balance);
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {}
    }
}
