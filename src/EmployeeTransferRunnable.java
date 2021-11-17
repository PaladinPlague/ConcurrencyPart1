//An account holder can either deposit or withdraw an amount from a thread
public class EmployeeTransferRunnable implements Runnable {
    private static final int DELAY = 1;
    private BankSystem bank;
    private int employeeIndex;
    private int holderIndex;
    private int accIndex;
    private double amount;
    private boolean isWithdraw;

    public EmployeeTransferRunnable(BankSystem system, int employee, int holder, int account, double depoAmount, boolean withdraw) {
        bank = system;
        employeeIndex = employee;
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
                bank.withdraw(employeeIndex, holderIndex, accIndex, amount);
            } else {
                bank.deposit(employeeIndex, holderIndex, accIndex, amount);
            }
            Thread.sleep(DELAY);
        }
        catch (InterruptedException exception) {

        } catch (Exception e) {}
    }
}
