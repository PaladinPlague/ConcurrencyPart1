import java.util.ArrayList;

//Account type that's used for everyday life services
public class CurrentAccount extends Account{

    //Holds the number of transactions stored in the account;
    private ArrayList<Transaction> transactions;
    //Identification number of account used to confirm transactions
    private int PIN;

    //Initiate current account with all fields filled in
    public CurrentAccount(String accountNo, Double openingBalance, int pin) {
        super(accountNo, openingBalance);
        //Declare Transactions initially as empty list
        this.transactions = new ArrayList<>();
        this.PIN = pin;
    }

    //Allow validation of a payment from account holder to take place via PIN
    //Due to editing information, declare the class as being synchronized
    public synchronized boolean confirmPayment(Account target, Double amount, int input) {
        //If input PIN matches account PIN, allow transaction to take place
        if (input == PIN) {
            this.withdraw(target, amount);
            return true;
        }
        //Otherwise, show this was unsuccessful by returning false
        return false;
    }

    //Deposit a set amount from another account into this account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void deposit(Account sender, Double amount) {
        setBalance(getBalance() + amount);
        transactions.add(new Transaction(amount, sender, this));
    }

    //Withdraw a set amount from this account into another account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void withdraw(Account receiver, Double amount) {
        setBalance(getBalance() - amount);
        transactions.add(new Transaction(amount, this, receiver));
    }

    //Currently unsure how to approach transfer methods for this account
    public synchronized void transfer() {

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

    //Return the list of transactions
    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    //Return the account's PIN number
    public int getPIN() {
        return this.PIN;
    }
}
