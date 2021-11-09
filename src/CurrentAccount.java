import java.util.ArrayList;

//Account type that's used for everyday life services
public class CurrentAccount extends Account{

    //Initiate current account with all fields filled in
    public CurrentAccount(String accountNo, Double openingBalance) {
        super(accountNo, openingBalance);
    }

    //Deposit a set amount from another account into this account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void deposit(Double amount, Account sender) {
        setBalance(getBalance() + amount);
        addToTransaction(new Transaction(amount, sender, this));
    }

    //Withdraw a set amount from this account into another account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void withdraw(Double amount, Account receiver) {
        setBalance(getBalance() - amount);
        addToTransaction(new Transaction(amount, this, receiver));
    }

    //Show that the type of the account is a current account
    public String getType() {
        return "Current Account";
    }

}
