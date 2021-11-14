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

    //Carry out depositing methods of this account
    public abstract void deposit(Double amount, Account sender) throws Exception;

    //Carry out the withdraw methods of this account
    public abstract void withdraw(Double amount, Account receiver) throws Exception;

    //Print the details of this account into a terminal
    //If an account needs to share more details, override
    public void printDetails() {
        System.out.println("Account number: " + this.getAccountNumber());
        //Format balance so it has 2 decimal places
        System.out.println("Balance: " + String.format("%.2f", this.getBalance()));
    }

    //Print the details of this account to a string (for possible GUI implementation)
    //If an account needs to share more details, override
    public String getDetails() {
        String result = "";
        result += "Account number: " + this.getAccountNumber() + "\n";
        //Format balance so it has 2 decimal places
        result += "Balance: " + String.format("%.2f", this.getBalance()) + "\n";
        result += "Account Type: " + this.getType() + "\n";
        return result;
    }

    //Change the current balance to a new value, ensuring it is at 2 decimal places
    //Due to editing information, declare the class as being synchronized
    //Methods can't be abstract and synchronized, so you classes must override this method
    public synchronized void setBalance(Double newBalance) {
        //Math.round function used to ensure value is at 2 decimal places
        this.balance = ((double) (Math.round(newBalance * 100))) / 100;
    }

    //Add a new transaction to the account's current transactions
    public synchronized void addToTransaction(Transaction transact) {
        transactions.add(transact);
    }
}
