import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class SavingAccount extends Account {

    /*
    SAVING ACCOUNT
    Created by Suleman Akhter

    - A saving account is an account that allows you top deposit money and let that money grow in value, this growth is called interest
    - Our saving account will have a minimum £1 deposit, otherwise you are unable to open an account
    - Our saving account will be instant access, meaning you can add and take out money any time
    - Our saving account will have an fixed interest rate of 0.2% every year (The first day every year)
    - This entire account is deemed Invalid if the balance is < than minimum deposit
    - This class overrides mostly everything in the abstract class as this notation of an account been invalid and interest, must be dealt in most of the methods
    - Our account does not require an Pin (Pin used for physical cash), because when you withdraw from an saving account, it must go to another account

    FIELDS:

    invalid:  checks whether this account is valid or not
    fixedValue: Minimum deposit required
    interestRate: The interest Rate that is added to savings yearly
    everyYear: This changes date everytime the account interest addition occurs
    currentBalance: The current money
    transactions: Arraylist of transaction type
    Transaction: All of the current Transactions

     */

    private Boolean invalid = true;
    private int fixedValue = 1;
    private double interestRate = 0.2;
    LocalDate everyYear;
    private double currentBalance;

    //This constructor uses super on account number and initial deposit parameters, and the account will only be valid if deposit has minimum £1
    public SavingAccount(String AccNumber, double deposit) {

        super(AccNumber,deposit);

        if(checkBalance(deposit)){
            currentBalance = deposit;
            setBalance(currentBalance);
            System.out.println(this.getAccountNumber() + " is valid!");
        }
        else{
            System.out.println(this.getAccountNumber() + " is invalid!");
            currentBalance = 0;
            setBalance(0.0);
        }
        everyYear = LocalDate.now();
    }

    //Function that checks whether this account is valid
    public boolean checkBalance(double balance) {

        if(balance < 1){
            invalid = true;
            return false;
        }
        else{
            invalid = false;
            return true;
        }
    }

    //Returns the type of account: "Saving Account"
    @Override
    public String getType() {
        return "Saving Account";
    }
    //Returns the balance but before returning the balance, it checks whether interest could of been added
    @Override
    public double getBalance() {

        if(invalid) {
            System.out.println("This account is not valid as we require an £1 minimum deposit");
            return currentBalance;

        }
        else{
            addInterest(currentBalance);
            return currentBalance;
        }
    }

    //Returns the transactions only if the account is valid
    @Override
    public ArrayList<Transaction> getTransactions() {
        if(!invalid) {
            return super.getTransactions();
        }
        else{
            return new ArrayList<>();
        }
    }

    // Adds the balance with the account and then adds the interest if eligible.
    @Override
    public synchronized void deposit(Double amount, Account sender) throws Exception {
        addInterest(getBalance());
        currentBalance += amount;
        currentBalance = Math.round(currentBalance * 100.0) / 100.0;
        if(checkBalance(currentBalance)){
            System.out.println("Total balance: " + currentBalance);
            addToTransaction(new Transaction(amount, sender, this));
        }
        else{
            System.out.println("This account is not valid as we require an £1 minimum deposit");
            currentBalance = 0.0;
            setBalance(0.0);
        }
    }
    // Withdraws the balance with the amount if possible, then adds the interest if eligible.
    //@Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        if(checkBalance(currentBalance)){

            addInterest(currentBalance);
            double withdraw = currentBalance -= amount;
            withdraw = Math.round(withdraw * 100.0) / 100.0;
            if(withdraw < 0){
                System.out.println("Unable to withdraw this amount of money!");
            }
            else{
                if(!checkBalance(currentBalance)){
                    System.out.println("Account is now invalid, minimum £1 required in this account!");
                    currentBalance = 0.0;
                    setBalance(0.0);
                }
                else {
                    currentBalance = withdraw;
                    setBalance(currentBalance);
                }
                addToTransaction(new Transaction(amount, this, receiver));
            }
        }

    }

    /*
    This functions adds the interest to the balance, it only adds it when checkBalance() or updateBalance() is called,
    The interest is not automatically added every year, it checks how many difference of years they have been since the account had been created or
    after its first addInterest addition. If it was 2 years, then it adds 2 years of interest value to the balance and vice versa

     */

    private void addInterest(double beforeBalance) {

        if (checkBalance(beforeBalance)) {

            LocalDate currentDate = LocalDate.now();

            int createdYear = everyYear.getYear();
            int currentYear = currentDate.getYear();
            int difference = createdYear - currentYear;
            double addInterest = 0.0;

            for (int i = 0; i < difference; i++) {
                addInterest = addInterest + interestRate;
            }

            if (addInterest != 0) {
                everyYear = LocalDate.now();
                currentBalance = (beforeBalance * addInterest/100) + beforeBalance;
                setBalance(currentBalance);
            }
        }
    }
    // This is uses for Junit purposes, allows me to see if interest function works
    public void changeDate(LocalDate change){
        everyYear = change;
    }

    //ADDED BY SCOTT -- UPDATES INTEREST.
    //If not applicable, this will also have to be changed in the Bank Employees
    public synchronized void changeInterest(double interest) {
        this.interestRate = interest;
    }

    public double getInterestRate() {
        return this.interestRate;
    }
}