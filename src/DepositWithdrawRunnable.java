//Runnable instance for an account holder depositing to or withdrawing from one of their accounts
public class DepositWithdrawRunnable implements Runnable {
    //The bank system whose concurrent method is called
    private BankSystem bank;
    //The index of the account holder from the bank used in the method
    private int holderIndex;
    //The index of the account held by the account holder
    private int accIndex;
    //The amount of money transferred in the deposit/withdraw method
    private double amount;
    //Flag indicating if the process is a deposit (false) or withdraw (true)
    private boolean isWithdraw;

    //Initialise the runnable object, with values of all fields received from parameters
    public DepositWithdrawRunnable(BankSystem system, int holder, int account, double depoAmount, boolean withdraw) {
        bank = system;
        holderIndex = holder;
        accIndex = account;
        amount = depoAmount;
        isWithdraw = withdraw;
    }

    //Runnable thread process of chosen method
    public void run() {
        try
        {
            //If isWithdraw field is equal to false, deposit into account. Otherwise, withdraw from the account
            if (isWithdraw) {
                bank.withdraw(holderIndex, accIndex, amount);
            } else {
                bank.deposit(holderIndex, accIndex, amount);
            }
            //The thread sleeps for a specified amount of time in milliseconds
            Thread.sleep(1);
        //If a thread for this method is interrupted, catch the exception and display an error message
        } catch (InterruptedException exception) {
            System.out.println("Could not complete customer transfer of account due to thread interrupted exception. ERROR CODE: " + exception.getMessage());
        //If another type of exception occurs, catch the exception and display an error message
        } catch (Exception exception) {
            System.out.println("Could not complete customer transfer of account due to exception. ERROR CODE: " + exception.getMessage());
        }
    }
}
