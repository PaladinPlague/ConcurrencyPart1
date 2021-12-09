//An abstract class used to represent all accounts in bank
public abstract class Account {

    //ID for account, e.g. 01234567
    //We can assume that whatever account number is passed to this is valid
    private String accountNumber;
    //Money stored within account
    private double balance;

    //Constructor of class, which takes in account's number and opening balance
    public Account(String accountNo, Double openingBalance) {
        this.accountNumber = accountNo;
        this.balance = openingBalance;
    }

    //Return the account number of the account
    public synchronized String getAccountNumber() {
        return this.accountNumber;
    }

    //Return the current balance of the account
    public synchronized double getBalance() {
        return this.balance;
    }

    //Return the account type e.g. if savings account, return "Saving Account"
    public abstract String getType();

    //Carry out depositing methods of this account
    public abstract void deposit(Double amount, Account sender) throws Exception;

    //Carry out the withdrawal methods of this account
    public abstract void withdraw(Double amount, Account receiver) throws Exception;

    //Print the details of this account into a terminal
    //If a specific account needs to share more details, override method in subclass
    public synchronized void printDetails() {
        System.out.println("Account number: " + this.getAccountNumber());
        //Format balance so that it has 2 decimal places
        //Reference used for formatting decimal number in output: https://stackoverflow.com/questions/2538787/how-to-print-a-float-with-2-decimal-places-in-java
        System.out.println("Balance: " + String.format("%.2f", this.getBalance()));
        System.out.println("Account Type: " + this.getType());
    }

    //Print the details of this account to a string
    //If a specific account needs to share more details, override method in subclass
    public synchronized String getDetails() {
        String result = "";
        result += "Account number: " + this.getAccountNumber() + "\n";
        //Format balance so it has 2 decimal places
        result += "Balance: " + String.format("%.2f", this.getBalance()) + "\n";
        result += "Account Type: " + this.getType() + "\n";
        return result;
    }

    //Change the current balance to a new value, ensuring it is at 2 decimal places
    public synchronized void setBalance(Double newBalance) {
        //Math.round function used to ensure value is at 2 decimal places
        this.balance = ((double) (Math.round(newBalance * 100))) / 100;
    }
}