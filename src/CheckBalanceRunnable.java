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
            accHolder.getBalance(accIndex);
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {}
    }
}
