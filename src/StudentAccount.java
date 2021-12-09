//Bank account type which is held by university students
//A Student's Account works similar to a current account
public class StudentAccount extends Account{

    //The maximum amount of money the bank account can have less than 0
    private double overdraft;
    //Status of whether or not the balance is less than 0
    private boolean overdrafted;

    //Initiate student account with all fields filled in
    public StudentAccount(String accountNo, Double openingBalance) {
        //Set account number and opening balance via superclass method, with all students receiving 50 extra when opening account
        super(accountNo, (openingBalance+50));
        //Set overdraft to the bank's default value of 1500, and set overdrafted status to false
        this.overdraft = 1500;
        overdrafted = false;
    }

    //Change the overdraft amount
    public synchronized void setOverdraft(double newOverdraftLimit) throws Exception {
        //If new overdraft is equal to old overdraft, throw an exception
        if(newOverdraftLimit == overdraft){
            throw new Exception("Sorry we can't set your required overdraft limit to be same as the original limit.");
        //Otherwise, set the account overdraft to the new overdraft
        }else{
            overdraft = newOverdraftLimit;
        }
    }

    //Get the overdraft value of the account
    public synchronized double getOverdraft(){
        return overdraft;
    }

    //Get the overdraft status of the account
    public synchronized boolean isOverdrafted(){ return overdrafted; }

    //Deposit a set amount from another account into this account
    @Override
    public synchronized void deposit(Double amount, Account sender) {
        setBalance(getBalance() + amount);
    }

    //Withdraw a set amount from this account into another account
    @Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        //If the amount combined with the overdraft value is more than what is stored in the bank, throw an exception with relevant message
        if((this.getBalance()+overdraft)<amount){
            throw new Exception("Sorry, insufficient fund.");
        //Otherwise, decrease the specified amount from this account's balance
        }else{
            setBalance(getBalance() - amount);
            //If the balance is less than 0, set the overdraft status as true
            if(Double.compare(getBalance(), 0.0) < 0) overdrafted = true;
        }
    }

    //Print the details of this account into a terminal
    @Override
    public synchronized void printDetails(){
        System.out.println("Student Account Number: " +this.getAccountNumber());
        System.out.println("New Balance: "+this.getBalance());
        System.out.println("The Arranged Overdraft amount is "+ this.getOverdraft());
    }

    //Print the details of this account to a string
    @Override
    public synchronized String getDetails(){
        String result = "Student Account Number: " +this.getAccountNumber()+ ", " +
                "The Arranged Overdraft amount is "+ this.getOverdraft()+", Overdraft: "+isOverdrafted()+" balance: "+this.getBalance()+".";
        return result;
    }

    //Return the account type
    @Override
    public synchronized String getType() {
        return "Student Account";
    }
}