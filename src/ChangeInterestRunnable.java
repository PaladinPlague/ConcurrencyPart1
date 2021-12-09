//Runnable instance for a bank employee changing the interest rate for an account of an account holder
public class ChangeInterestRunnable implements Runnable {
    //The bank system whose concurrent method is called
    private BankSystem bank;
    //The index of the bank employee from the bank used in the method
    private int employeeIndex;
    //The index of the account holder from the bank used in the method
    private int holderIndex;
    //The index of the account held by the account holder
    private int accountIndex;
    //The interest that the employee is trying to set for the account
    private double interest;

    //Initialise the runnable object, with values of all fields received from parameters
    public ChangeInterestRunnable(BankSystem system, int employee, int holder, int account, double newInterest) {
        bank = system;
        employeeIndex = employee;
        holderIndex = holder;
        accountIndex = account;
        interest = newInterest;
    }

    //Runnable thread process of this method
    public void run() {
        try
        {
            //Execute the change interest method for the bank involving an employee, account holder and account
            bank.changeInterest(employeeIndex, holderIndex, accountIndex, interest);
            //The thread sleeps for a specified amount of time in milliseconds
            Thread.sleep(1);
        //If a thread for this method is interrupted, catch the exception and display an error message
        } catch (InterruptedException exception) {
            System.out.println("ERROR: Could not change interest of account due to thread interrupted exception. ERROR CODE: " + exception.getMessage());
        //If another type of exception occurs, catch the exception and display an error message
        } catch (Exception exception) {
            System.out.println("ERROR: Could not change interest of account due to exception. ERROR CODE: " + exception.getMessage());
        }
    }
}