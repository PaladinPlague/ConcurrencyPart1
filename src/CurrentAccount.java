//Account type that's used for everyday life services
public class CurrentAccount extends Account{

    private AccountHolder cardHolder;

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
        return "Current Account Number: " +this.getAccountNumber()+ ", balance: "+this.getBalance()+
                "Transactions："+this.getTransactions()+".";
    }

    //Show that the type of the account is a current account
    public String getType() {
        return "Current Account";
    }

}
