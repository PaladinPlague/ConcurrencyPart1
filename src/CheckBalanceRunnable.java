//Runnable instance for an account holder checking the balance of one of their accounts
public class CheckBalanceRunnable implements Runnable {
    //The bank system whose concurrent method is called
    private BankSystem bank;
    //The index of the account holder from the bank used in the method
    private int holderIndex;
    //The index of the account held by the account holder
    private int accIndex;

    //Initialise the runnable object, with values of all fields received from parameters
    public CheckBalanceRunnable(BankSystem system, int holder, int account) {
        bank = system;
        holderIndex = holder;
        accIndex = account;
    }

    //Runnable thread process of this method
    public void run() {
        try {
            //Execute the check balance method for the bank involving an account holder and one of their accounts
            bank.checkBalance(holderIndex, accIndex);
            //The thread sleeps for a specified amount of time in milliseconds
            Thread.sleep(1);
        //If a thread for this method is interrupted, catch the exception and display an error message
        } catch (InterruptedException exception) {
            System.out.println("ERROR: Could not check balance of account due to thread interrupted exception. ERROR CODE: " + exception.getMessage());
        //If another type of exception occurs, catch the exception and display an error message
        } catch (Exception exception) {
            System.out.println("ERROR: Could not check balance of account due to exception. ERROR CODE: " + exception.getMessage());
        }
    }
}
