import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class SavingAccount extends Account {

    /*
    CREATED BY SULEMAN AKHTER
    An saving account is an account that allows you top deposit money and let that money grow in value, this growth is called interest
    Our saving account will have a minimum £1 deposit, otherwise you are unable to open an account
    Our saving account will be instant access, meaning you can add and take out money any time
    Our saving account will have an fixed interest rate of 0.2% every year
    This entire account is deemed Invalid if the balance is < than minimum deposit
    This class overrides everything in the abstract class as this notation of an account been invalid, must be dealt  in every method

    */

    /*
    FIELDS:

    invalid:  checks whether this account is valid or not
    fixedValue: Minimum deposit required
    interestRate: The interest Rate that is added to savings yearly
    everyYear: This changes date everytime the account interest addition occurs
    Balance: The current money
    AccountNumber: The users Account ID
    Transaction: All of the current Transactions

     */

    private Boolean invalid = true;
    private int fixedValue = 1;
    private double interestRate = 0.2;
    LocalDate everyYear;
    double balance;
    String accountNumber;
    private ArrayList<Transaction> transactions;

    //This constructor takes the deposit and AccNumber, and the account will only be valid if deposit has minimum £1
    public SavingAccount(String AccNumber, double deposit) {

        updateAccountNo(AccNumber);

        if (checkBalance()) {
            balance = deposit;
        }
        else{
            balance = 0;
        }

        everyYear = LocalDate.now();
    }
    //Helper function that checks whether this account is valid
    private boolean checkBalance() {

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
            return balance;

        }
        else{
            addInterest();
            return this.balance;
        }
    }
    //Returns the transactions only if the account is valid
    @Override
    public ArrayList<Transaction> getTransactions() {
        if(!invalid) {
            return this.transactions;
        }
        else{
            return null;
        }
    }

    // Before updating the balance, it checks whether interest can be added. After the transaction occurs it checks whether the account is still valid
    @Override
    public synchronized void updateBalance(Double amount, String source) {
        this.balance += amount;
        if(checkBalance()){
            addInterest();
            //transactions.add(new Transaction(amount, source));
            this.balance += amount;
        }
        else{
            System.out.println("This account is not valid as we require an £1 minimum deposit");
            balance = 0;
        }
    }
    /*
    This functions adds the interest to the balance, it only adds it when checkBalance() or updateBalance() is called,
    The interest is not automatically added every year, it checks how many difference of years they have been since the account had been created or
    after its first addInterest addition. If it was 2 years, then it adds 2 years of interest value to the balance and vice versa

     */

    private void addInterest() {

        if (checkBalance()) {

            LocalDate currentDate = LocalDate.now();

            int createdYear = everyYear.getYear();
            int currentYear = currentDate.getYear();
            int difference = currentYear - createdYear;
            double addInterest = 0.0;

            for (int i = 0; i < difference; i++) {
                addInterest = addInterest + interestRate;
            }

            if (currentYear > createdYear) {
                everyYear = LocalDate.now();
                balance = (balance * interestRate) + balance;
            }
        }
    }
}




