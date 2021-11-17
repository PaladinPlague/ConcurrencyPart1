//An account holder checks the balance of one of their accounts
public class ChangeInterestRunnable implements Runnable {
    private static final int DELAY = 1;
    private BankSystem bank;
    private int employeeIndex;
    private int holderIndex;
    private int accountIndex;
    private double interest;

    public ChangeInterestRunnable(BankSystem system, int employee, int holder, int account, double newInterest) {
        bank = system;
        employeeIndex = employee;
        holderIndex = holder;
        accountIndex = account;
        interest = newInterest;
    }

    //Thread process of this method
    public void run() {
        try
        {
            bank.changeInterest(employeeIndex, holderIndex, accountIndex, interest);
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {}
    }
}
