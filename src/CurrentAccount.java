import java.util.ArrayList;

//Account type that's used for everyday life services
public class CurrentAccount extends Account{

    //private Person cardHolder;

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
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        if(this.getBalance()<amount){
            throw new Exception("Sorry, insufficient fund.");
        }else{
            setBalance(getBalance() - amount);
            addToTransaction(new Transaction(amount, this, receiver));
        }

    }

    @Override
    public void printDetails(){
        System.out.println("CC Account Number: " +this.getAccountNumber());
        System.out.println("New Balance: "+this.getBalance());
        System.out.println("List of Transactions: " + this.getTransactions());
    }

    @Override
    public String getDetails(){
        ArrayList<Transaction> transactions = this.getTransactions();
        String result = "";
        result = "Current Account Number: " +this.getAccountNumber()+ ", balance: "+this.getBalance()+
                "Transactions：" + "[";
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
        return "Current Account";
    }
}