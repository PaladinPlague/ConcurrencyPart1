//An account holder checks the balance of one of their accounts
public class CheckBalanceRunnable implements Runnable {
    private static final int DELAY = 1;
    private BankSystem bank;
    private int holderIndex;
    private int accIndex;

    public CheckBalanceRunnable(BankSystem system, int holder, int account) {
        bank = system;
        holderIndex = holder;
        accIndex = account;
    }

    //Thread process of this method
    public void run() {
        try
        {
            bank.checkBalance(holderIndex, accIndex);
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {}
    }
}
