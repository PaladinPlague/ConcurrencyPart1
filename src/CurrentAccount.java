//Bank account type which is used for everyday services
public class CurrentAccount extends Account{

    //Declare a new current account with parameters to set account number and opening balance via superclass method
    public CurrentAccount(String accountNo, Double openingBalance) {
        super(accountNo, openingBalance);
    }

    //Deposit a set amount from another account into this account
    @Override
    public synchronized void deposit(Double amount, Account sender) {
        setBalance(getBalance() + amount);
    }

    //Withdraw a set amount from this account into another account
    @Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        //If the amount is more than what is stored in the bank, throw an exception with relevant message
        if(this.getBalance()<amount){
            throw new Exception("ERROR: insufficient funds.");
        //Otherwise, decrease the specified amount from this account's balance
        }else{
            setBalance(getBalance() - amount);
        }
    }

    //Print the details of this account into a terminal
    @Override
    public synchronized void printDetails(){
        System.out.println("CC Account Number: " +this.getAccountNumber());
        System.out.println("New Balance: "+this.getBalance());
    }

    //Print the details of this account to a string
    @Override
    public synchronized String getDetails(){
        String result = "";
        result = "Current Account Number: " +this.getAccountNumber()+ ", balance: "+this.getBalance()+".";
        return result;
    }

    //Return the account type
    @Override
    public synchronized String getType() {
        return "Current Account";
    }
}