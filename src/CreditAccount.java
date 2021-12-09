import java.time.LocalDate;
import java.util.Objects;
/*
Author: Nuoxu Li
CreditAccount class is to simulate real life credit card.
This class keep
 */

//Bank account type which is used for customers to store credit
public class CreditAccount extends Account  {

    //The maximum amount of credit the account can hold
    private double creditLimit;
    //The credit currently stored in the account
    private double availableCredit;
    //Annual Percentage Rate - interest per year
    private double APR;
    //The date for which a payment must be made within the date
    private final int paymentDueDate;
    //The date of the payment
    LocalDate paymentDate;

    //Declare a new credit account with parameters to set the account number, initial credit and initial APR
    public CreditAccount(String accountNo, double openingCredit, double APR) {
        //Set account number and opening balance via superclass constructor. As the account is more dependent on credit than balance, the account opens with no balance
        super(accountNo, 0.00);

        //The credit the account starts with, which is also the maximum credit, is equal to the second parameter
        this.creditLimit = openingCredit;
        this.availableCredit = openingCredit;
        //Set the initial APR to the third parameter
        this.APR = APR;
        //All credit accounts for the bank have payment due on the 15th of the month
        paymentDueDate = 15;
        //Set the payment date equal to when the account was created
        paymentDate = LocalDate.now();
    }


    //Set Credit limit, rarely used in real life
    public synchronized void setCreditLimit(double newCredit){
        creditLimit = newCredit;
    }

    //Set up new APR, usually managed by employee rather than account holder
    public synchronized void setAPR(double newAPR){
        APR = newAPR;
    }

    //Update payment date
    public synchronized void setPaymentDate(LocalDate paymentDate){
        this.paymentDate = paymentDate;
    }

    //Get the credit limit of the account
    public synchronized double getCreditLimit(){ return creditLimit; }

    //Get the current amount of credit in the account
    public synchronized double getAvailableCredit(){ return availableCredit; }

    //Get the APR of the account
    public synchronized double getAPR(){ return APR; }

    //Get the monthly interest of the account by converting APR from yearly rate to monthly rate
    public synchronized double monthlyInterest(){ return APR/12; }

    //Function to determine how much money should be paid based on the date of the month in which the payment is made.
    //If the payment is made on or before the paymentDueDate, then no interest is charged.
    //If it is made after the paymentDueDate then we charge an overdue interest on the balance.
    //The interest is calculated monthly.
    public synchronized void monthlyPayment(){

        if(paymentDate.getDayOfMonth() > paymentDueDate){
            double payableAmount = this.getBalance()+(this.getBalance()*monthlyInterest()) ;
            this.setBalance(payableAmount);
        }
    }

    //Pay in money from another Current Account
    //The sender must NOT be another CreditAccount, since it is illegal to use credit to pay credit
    //The payment can be in full or partially.
    //The payment can be on the due day, before due day or after due day
    @Override
    public synchronized void deposit(Double amount, Account sender) throws Exception {
        //Check the account has made its monthly payment first
        monthlyPayment();

        //Check the sender object is not a credit card account
        if(Objects.equals(sender.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use other credit card to pay this credit card bill!");
        //Check that the sender has enough balance to deposit to this account
        }else if(Math.abs(sender.getBalance()) < amount){
            throw new Exception("Sorry, insufficient fund.");
        //Check that the account has enough for the payment
        }else if(amount > Math.abs(this.getBalance())){
            throw new Exception("you can't pay more than you have spent!");
        }else {

            //Set the balance equal to the deposited amount
            setBalance(getBalance()+amount);
            //The credit should be re-received from the account in case any changes are made
            double AC = getAvailableCredit();
            //Ensure that the new credit does not go over the credit limit of the account
            if((AC + amount) >getCreditLimit()){
                availableCredit = creditLimit;
            }else{
                availableCredit += amount;
            }

        }

    }

    //Pay credit money to another account
    //Balance starts from 0.00, and every payment should decrease the balance
    //creditLimit should never be changed, as it represents how much you can spend per month
    //Update availableCredit field, as it is used to pay for the balance withdrawal
    @Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {

        //Check that the receiver object is not a credit account
        if(Objects.equals(receiver.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use this credit card to pay another credit card!");
        //Check if we still have enough credit to pay for this transaction.
        }else if(availableCredit < amount){
            throw new Exception("Sorry, insufficient fund.");
        }else{
            //Subtract the withdrawal amount from the credit and balance of this account
            availableCredit -= amount;
            setBalance(getBalance()-amount);
        }
    }

    /*
    Pay in money from another Current Account
    the sender must BE SAME PERSON'S CURRENT ACCOUNT.
    the payment can be in full or partially.
    the payment can be on the due day, before due day or after due day
    */
    public synchronized void transfer (double amount, Account supplyAccount) throws Exception {

        monthlyPayment();

        if(Objects.equals(supplyAccount.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use other credit card to pay this credit card bill!");
        }else if(Math.abs(supplyAccount.getBalance())< amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else if(amount > Math.abs(this.getBalance())){
            throw new ArithmeticException("you can't pay more than you have spent!");
        }else if(Objects.equals(getType(), "")){
            throw new Exception("you may wish to use deposit if you paying for someone else");
        }else{
            setBalance(getBalance()+amount);
            if((availableCredit+=amount)>getCreditLimit()){
                availableCredit = creditLimit;
            }else{
                availableCredit += amount;
            }
        }
    }

    //Print the details of this account into a terminal
    @Override
    public void printDetails(){
        System.out.println("CC Account Number: " +this.getAccountNumber());
        System.out.println("Total Credit Limit: " +getCreditLimit());
        System.out.println("Available Credit of This Month: " + getAvailableCredit());
        System.out.println("New Balance: "+this.getBalance());
    }

    //Print the details of this account to a string
    @Override
    public String getDetails(){
        String result = "CC Account Number: " +this.getAccountNumber()+", credit: " +getCreditLimit()+", " +
                "available Credit: " + getAvailableCredit()+", balance: "+this.getBalance()+ ".";
        return result;
    }

    //Return the account type
    @Override
    public String getType() {
        return "Credit Card Account";
    }
}