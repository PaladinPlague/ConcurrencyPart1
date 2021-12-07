//An account holder can either deposit or withdraw an amount from a thread
public class DepositWithdrawRunnable implements Runnable {
    private static final int DELAY = 1;
    private BankSystem bank;
    private int holderIndex;
    private int accIndex;
    private double amount;
    private boolean isWithdraw;

    public DepositWithdrawRunnable(BankSystem system, int holder, int account, double depoAmount, boolean withdraw) {
        bank = system;
        holderIndex = holder;
        accIndex = account;
        amount = depoAmount;
        isWithdraw = withdraw;
    }

    //Thread process of this method - depending on value of boolean parameter, process is deposit (if false) or withdraw (if true)
    public void run() {
        try
        {
            if (isWithdraw) {
                bank.withdraw(holderIndex, accIndex, amount);
            } else {
                bank.deposit(holderIndex, accIndex, amount);
            }
            Thread.sleep(DELAY);
        } catch (Exception exception) {

        }
    }
}
