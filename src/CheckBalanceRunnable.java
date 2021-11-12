public class CheckBalanceRunnable implements Runnable {
    private static final int DELAY = 1;
    private AccountHolder accHolder;
    private int accIndex;


    public CheckBalanceRunnable(AccountHolder holder, int index) {
        accHolder = holder;
        accIndex = index;
    }

    public void run() {
        try
        {
            double balance = accHolder.getAccount(accIndex).getBalance();
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolder.getName() + " gets balance " + balance);
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {}
    }
}
