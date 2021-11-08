import java.util.*;

//This is an abstract class used to represent all accounts in bank
public abstract class Account {

    //ID for account, e.g. 01234567
    //We can assume that whatever account number is passed to this is valid unless yous want to impose restrictions on this.
    private String accountNumber;
    //Money stored within account
    private double balance;
    //Holds the number of transactions stored in the account;
    private ArrayList<Transaction> transactions;

    /* NOTE FROM JACKSON: As of now, general account details are very basic.
    This works for our project, but if we wanted to add more details to make
    the bank account work closer to a more realistic bank account, additional
    details we could add may include sort code, email address and card details,
    then add more corresponding methods similar to those performed on the account
    number. */

    //Constructor of class, which takes in account's number and opening balance
    public Account(String accountNo, Double openingBalance) {
        //Declare account number from string parameter
        this.accountNumber = accountNo;
        //Declare opening balance of account from double parameter
        this.balance = openingBalance;
        //Declare Transactions initially as empty list
        this.transactions = new ArrayList<>();
    }

    //Return the account number of the account
    public String getAccountNumber() {
        return this.accountNumber;
    }

    //Return the current balance of the account
    public double getBalance() {
        return this.balance;
    }

    //Return the list of transactions
    public ArrayList<Transaction> getTransactions() {
        return this.transactions;
    }

    //Return the current account type e.g. if savings account, return "Savings Account"
    public abstract String getType();

    //Update the current balance of the class and store the transaction for this
    //Due to editing information, declare the class as being synchronized
    //Use protected so that balance cannot be changed directly from other accounts, rather via methods
    protected synchronized void updateBalance(Transaction transact) {
        this.balance += transact.getAmount();
        transactions.add(transact);
    }

    //Deposit a set amount from another account into this account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void deposit(Double amount, Account sender) throws Exception {
        this.balance += amount;
        transactions.add(new Transaction(amount, sender, this));
    }

    //Withdraw a set amount from this account into another account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void withdraw(Double amount, Account receiver, int pin) throws Exception {
        this.balance -= amount;
        transactions.add(new Transaction(amount, this, receiver));
    }

    //Change the current balance to a new value
    //Due to editing information, declare the class as being synchronized
    public synchronized void setBalance(Double newBalance) {
        this.balance = newBalance;
    }


    public synchronized void addToTransaction(Transaction newTransaction){}

}
