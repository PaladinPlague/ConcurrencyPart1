import java.time.LocalDate;

//Bank account type which is used to top deposit money which increases with interest
public class SavingAccount extends Account {

    /*
    SAVING ACCOUNT
    - A saving account is an account that allows you top deposit money and let that money grow in value, this growth is called interest
    - Our saving account will have a minimum £1 deposit, otherwise you are unable to open an account
    - Our saving account will be instant access, meaning you can add and take out money any time
    - Our saving account will have an fixed interest rate of 0.2% every year (The first day every year)
    - This entire account is deemed Invalid if the balance is < than minimum deposit
    - This class overrides mostly everything in the abstract class as this notation of an account been invalid and interest, must be dealt in most of the methods
    - Our account does not require an Pin (Pin used for physical cash), because when you withdraw from an saving account, it must go to another account
     */

    //Checks whether the account is valid or not
    private Boolean invalid = true;
    //Minimum deposit required
    private int fixedValue = 1;
    //Interest Rate that is added to savings yearly
    private double interestRate = 0.2;
    //Changes date everytime the account interest addition occurs
    LocalDate everyYear;
    //Current money stored in account
    private double currentBalance;

    //This constructor uses super on account number and initial deposit parameters, and the account will only be valid if deposit has minimum £1
    public SavingAccount(String AccNumber, double deposit) {
        //Set account number and opening balance via superclass constructor
        super(AccNumber,deposit);

        //If deposit amount is valid, set balance to this amount
        if(checkBalance(deposit)){
            currentBalance = deposit;
            setBalance(currentBalance);
            System.out.println(this.getAccountNumber() + " is valid!");
        }
        //Otherwise, set balance as 0
        else{
            System.out.println(this.getAccountNumber() + " is invalid!");
            currentBalance = 0;
            setBalance(0.0);
        }
        //Set date variable to current date
        everyYear = LocalDate.now();
    }

    //Function that checks whether this account is valid
    public synchronized boolean checkBalance(double balance) {

        //If balance is less than one, account is invalid. Otherwise, it is valid
        //For each case, update invalid status of account and return validity of account
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
    public synchronized String getType() {
        return "Saving Account";
    }

    //Returns the balance but before returning the balance, it checks whether interest could of been added
    @Override
    public synchronized double getBalance() {
        //If account is not valid, show message and return current balance value
        if(invalid) {
            System.out.println("This account is not valid as we require an £1 minimum deposit");
            return currentBalance;
        }

        //Otherwise, check if interest to account should be added and return balance
        else{
            addInterest(currentBalance);
            return currentBalance;
        }
    }

    //Adds the balance with the account and then adds the interest if eligible.
    @Override
    public synchronized void deposit(Double amount, Account sender) throws Exception {
        //Check if interest is to be added to balance
        addInterest(getBalance());
        //Add amount to balance and ensure it is rounded to two decimal places
        currentBalance += amount;
        currentBalance = Math.round(currentBalance * 100.0) / 100.0;
        //If balance amount is valid, print its value
        if(checkBalance(currentBalance)){
            System.out.println("Total balance: " + currentBalance);
        }
        //Otherwise, set balance to 0 and who message
        else{
            System.out.println("This account is not valid as we require an £1 minimum deposit");
            currentBalance = 0.0;
            setBalance(0.0);
        }
    }
    // Withdraws the balance with the amount if possible, then adds the interest if eligible.
    @Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        //Check if account balance is valid before carrying out withdraw process
        if(checkBalance(currentBalance)){

            //Check if interest is to be added to balance
            addInterest(currentBalance);
            //Subtract amount from balance and ensure it is rounded to two decimal places
            double withdraw = currentBalance -= amount;
            withdraw = Math.round(withdraw * 100.0) / 100.0;
            //If withdraw total is negative, stop process and show error message
            if(withdraw < 0){
                System.out.println("Unable to withdraw this amount of money!");
                //Otherwise, complete withdrawal process
            } else {
                //If balance amount is invalid, set its value to 0 and show message
                if(!checkBalance(currentBalance)){
                    System.out.println("Account is now invalid, minimum £1 required in this account!");
                    currentBalance = 0.0;
                    setBalance(0.0);
                    //Otherwise, set balance to value when subtracted by withdrawal amount
                } else {
                    currentBalance = withdraw;
                    setBalance(currentBalance);
                }
            }
        }
    }

    //Add the interest to the balance, it only adds it when checkBalance() or updateBalance() is called
    //The interest is not automatically added every year, it checks how many difference of years they have been since the account had been created or after its first addInterest addition. If it was 2 years, then it adds 2 years of interest value to the balance and vice versa
    private synchronized void addInterest(double beforeBalance) {

        //Check if amount is valid
        if (checkBalance(beforeBalance)) {
            //Save current date
            LocalDate currentDate = LocalDate.now();
            //Compare difference between year account was created and current year
            int createdYear = everyYear.getYear();
            int currentYear = currentDate.getYear();
            int difference = createdYear - currentYear;
            //For every year, add total interest to account by interest rate
            double addInterest = 0.0;
            for (int i = 0; i < difference; i++) {
                addInterest = addInterest + interestRate;
            }
            //If there was an update in interest, save current date and increase balance based on total interest
            if (addInterest != 0) {
                everyYear = LocalDate.now();
                currentBalance = (beforeBalance * addInterest/100) + beforeBalance;
                setBalance(currentBalance);
            }
        }
    }

    //This is used for Junit purposes, showing if interest function works
    public synchronized void changeDate(LocalDate change){
        everyYear = change;
    }

    //Update interest rate of account
    public synchronized void changeInterest(double interest) {
        this.interestRate = interest;
    }

    //Get interest rate of account
    public synchronized double getInterestRate() {
        return this.interestRate;
    }
}