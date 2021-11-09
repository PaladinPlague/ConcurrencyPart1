import java.util.*;

//This is an abstract class used to represent all accounts in bank
public abstract class Account {

    //ID for account, e.g. 01234567
    //We can assume that whatever account number is passed to this is valid unless yous want to impose restrictions on this.
    private String accountNumber;
    //Money stored within account
    private double balance;

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
    }

    //Return the account number of the account
    public String getAccountNumber() {
        return this.accountNumber;
    }

    //Return the current balance of the account
    public double getBalance() {
        return this.balance;
    }

    //Return the current account type e.g. if savings account, return "Savings Account"
    public abstract String getType();

    //Carry out depositing methods of this account
    public abstract void deposit(Account sender, Double amount);

    //Carry out the withdraw methods of this account
    public abstract void withdraw(Account receiver, Double amount);

    //Carryout the transfer methods of this account
    public abstract void transfer();

    //Change the current balance to a new value, ensuring it is at 2 decimal places
    //Due to editing information, declare the class as being synchronized
    //Methods can't be abstract and synchronized, so you classes must override this method
    public synchronized void setBalance(Double newBalance) {
        this.balance = ((double) (Math.round(newBalance * 100))) / 100;
    }
}