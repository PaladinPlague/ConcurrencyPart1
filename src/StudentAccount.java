import java.util.ArrayList;
import java.time.LocalDate;

public class StudentAccount extends Account{
    /* Based on Saving's Account | Updated by Mark
    A Student's Account works similar to a normal Current account, Student
    */

    /*
    FIELDS:

     */

    //private Person cardHolder;
    private double overdraft;
    private boolean overdrafted;

    //Initiate current account with all fields filled in
    public StudentAccount(String accountNo, Double openingBalance) {
        super(accountNo, (openingBalance+50));
        this.overdraft = 1500;
        overdrafted = false;
    }

    public void setOverdraft(double newOverdraftLimit) throws Exception {
        if(newOverdraftLimit == overdraft){
            throw new Exception("Sorry we can't set your required overdraft limit to be same as the original limit.");
        }else{
            overdraft = newOverdraftLimit;
        }
    }

    public double getOverdraft(){
        return overdraft;
    }

    public boolean isOverdrafted(){ return overdrafted; }

    //Deposit a set amount from another account into this account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void deposit(Double amount, Account sender) {
        setBalance(getBalance() + amount);
        addToTransaction(new Transaction(amount, sender, this));
    }

    //Withdraw a set amount from this account into another account and save it to list of transactions
    //Due to editing information, declare the class as being synchronized
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        if((this.getBalance()+overdraft)<amount){
            throw new Exception("Sorry, insufficient fund.");
        }else{
            setBalance(getBalance() - amount);
            if(Double.compare(getBalance(), 0.0) < 0) overdrafted = true;
            addToTransaction(new Transaction(amount, this, receiver));
        }

    }


    @Override
    public void printDetails(){
        System.out.println("Student Account Number: " +this.getAccountNumber());
        System.out.println("New Balance: "+this.getBalance());
        System.out.println("The Arranged Overdraft amount is "+ this.getOverdraft());
        System.out.println("List of Transactions: " + this.getTransactions());
    }

    @Override
    public String getDetails(){
        ArrayList<Transaction> transactions = this.getTransactions();
        String result = "Student Account Number: " +this.getAccountNumber()+ ", " +
                        "The Arranged Overdraft amount is "+ this.getOverdraft()+", Overdraft: "+isOverdrafted()+" balance: "+this.getBalance()+
                        ", Transactions：" + "[";
        for (int i = 0; i < transactions.size(); i++) {
            result += "From: " + transactions.get(i).getSource().getAccountNumber();
            result += " To: " + transactions.get(i).getReceiver().getAccountNumber();
            result += " Amount: " + transactions.get(i).getAmount();
            if (i < transactions.size() - 1) {
                result += ", ";
            } else {
                result += "]";
            }
        }

        result += ".";

        return result;
    }

    //Show that the type of the account is a current account
    public String getType() {
        return "Student Account";
    }

}

