//Account type that's used for everyday life services
public class CurrentAccount extends Account{

    //Identification number of account used to confirm transactions
    private int PIN;

    //Initiate current account with all fields filled in
    public CurrentAccount(String accountNo, Double openingBalance, int pin) {
        super(accountNo, openingBalance);
        this.PIN = pin;
    }

    //Allow validation of a payment from account holder to take place via PIN
    //Due to editing information, declare the class as being synchronized
    public synchronized boolean confirmPayment(Transaction transaction, int input) {
        //If input PIN matches account PIN, allow transaction to take place
        if (input == PIN) {
            this.updateBalance(transaction);
            return true;
        }
        //Otherwise, show this was unsuccessful by returning false
        return false;
    }

    //Allow user to change PIN if they can successfully enter the current number
    //Due to editing information, declare the class as being synchronized
    public synchronized boolean updatePIN(int currentPin, int newPin) {
        //If input for old PIN matches account PIN and is not the same as new PIN, update account PIN
        if (PIN == currentPin && currentPin != newPin) {
            this.PIN = newPin;
            return true;
        }
        //Otherwise, show that the operation is unsuccessful by returning false
        return false;
    }

    //Show that the type of the account is a current account
    public String getType() {
        return "Current Account";
    }
}
