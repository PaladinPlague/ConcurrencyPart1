import java.util.ArrayList;

//A class that represents the entire set of processes within the bank used to test concurrency
public class BankSystem {

    //List of account holders for the bank
    ArrayList<AccountHolder> accHolders;
    //Waiting for bank holder system to be complete before adding relevant methods
    //ArrayList<BankHolder> bankHolders;

    public BankSystem() {
        accHolders = new ArrayList<>();
        //bankHolders = new ArrayList<>();
    }

    //Add a new account holder to the system
    public synchronized void addAccountHolder(AccountHolder holder) {
        accHolders.add(holder);
    }

    //Add a new bank holder to the system
    /*public synchronized void addBankHolder(BankHolder holder) {
        bankHolders.add(holder);
    }*/

    //A customer tries to check the balance of their account
    public synchronized void checkBalance(int i, int j) {
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(i).getName() + " checking balance of account " + accHolders.get(i).getAccount(j).getAccountNumber());
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account is: " + accHolders.get(i).getAccount(j).getBalance());
    }

    //A customer tries to deposit an amount of money into their account
    public synchronized void deposit(int i, int j, double amount) throws Exception{
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(i).getName() + " depositing amount " + amount + " into account " + accHolders.get(i).getAccount(j).getAccountNumber());
        accHolders.get(i).getAccount(j).deposit(amount, new CurrentAccount("00000000", amount));
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account is: " + accHolders.get(i).getAccount(j).getBalance());
    }

    //A customer tries to withdraw an amount of money from their account
    public synchronized void withdraw(int i, int j, double amount) throws Exception{
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(i).getName() + " withdrawing amount " + amount + " from account " + accHolders.get(i).getAccount(j).getAccountNumber());
        accHolders.get(i).getAccount(j).withdraw(amount, new CurrentAccount("00000000", 0.0));
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account is: " + accHolders.get(i).getAccount(j).getBalance());
    }

}
