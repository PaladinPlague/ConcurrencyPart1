//An account holder deposits money from a different account
public class DepositRunnable implements Runnable {
    private static final int DELAY = 1;
    private AccountHolder accHolder;
    private int accIndex;
    private double amount;

    public DepositRunnable(AccountHolder holder, int index, double depoAmount) {
        accHolder = holder;
        accIndex = index;
        amount = depoAmount;
    }

    //Thread process of this method
    public void run() {
        try
        {
            //Get balance from account
            accHolder.deposit(accIndex, amount, new CurrentAccount("00000000", amount));
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {} catch (Exception e) {
            e.printStackTrace();
        }
    }
}
