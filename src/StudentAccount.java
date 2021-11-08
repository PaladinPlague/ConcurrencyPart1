import java.util.ArrayList;
import java.time.LocalDate;

public class StudentAccount extends Account{
    /* Based on Saving's Account | Updated by Mark
    A Student's Account works similar to Saving's Account but the Student's account is not required to pay fees for the account
    
    */

    /*
    FIELDS:

    invalid:  checks whether this account is valid or not
    fixedValue: Minimum deposit required
    interestRate: The interest Rate that is added to students yearly
    everyYear: This changes date everytime the account interest addition occurs
    Balance: The current money
    AccountNumber: The users Account ID
    Transaction: All of the current Transactions
    Locked: Checks if the User's Bank Card has been locked

     */
    private Boolean invalid = true;
    private double interestRate = 0.2;
    LocalDate everyYear;
    double balance;
    String accountNumber;
    private ArrayList<Transaction> transactions;
    private Boolean locked = false;

    public StudentAccount(String AccNumber, double balance, Boolean locking) {
        super(AccNumber, balance);
        this.locked = locking;
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


    //Returns the type of account: "Student Account"
    @Override
    public String getType() {
        return "Student Account";
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
    protected synchronized void updateBalance(Transaction amount) {
        this.balance += amount.getAmount();
        if(checkBalance()){
            addInterest();
            //transactions.add(new Transaction(amount, source));
            // If the Card is locked and the Transaction is taking money out, it will stop the user
            if(locked && amount.getAmount() < 0){
                System.out.println("This Account is locked, please try again later");
            }else{
            this.balance += amount.getAmount();
            }
        }else{
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
