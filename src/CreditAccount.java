import java.time.LocalDate;
import java.util.Objects;
/*
Author: Nuoxu Li
CreditAccount class is to simulate real life credit card.
This class keep
 */

public class CreditAccount extends Account  {

    private double creditLimit;
    private double availableCredit;
    private int pin;
    private double APR;
    private final int paymentDueDate;
    private Person cardHolder;

    public CreditAccount(String accountNo, Double openingBalance, double openingCredit, int pin, double APR ) {
        super(accountNo, openingBalance);

        this.creditLimit = openingCredit;
        this.availableCredit = openingCredit;
        this.pin = pin;
        this.APR = APR;
        paymentDueDate = 15;
    }


    /*
    getter and setter methods for all fields
     */

    //set Credit, rarely used in real life
    public  synchronized void setCreditLimit(double newCredit){
        creditLimit = newCredit;
    }

    //set up new pin for pin update service
    public synchronized void setPin(int newPin){
        pin =  newPin;
    }

    //set up new APR, rarely used in real life
    public synchronized void setAPR(double newAPR){
        APR = newAPR;
    }

    //get credit return the credit
    public synchronized double getCreditLimit(){
        return creditLimit;
    }

    //get credit return the current available credit
    public synchronized double getAvailableCreditCredit(){
        return availableCredit;
    }

    /*
    we also need a get balance class but that has been defined in abstract class
     */

    //get APR, return the current APR
    public synchronized double getAPR(){
        return APR;
    }

    //get Pin return pin, might be used to check the pin.
    public synchronized int getPin(){
        return pin;
    }

    public synchronized double monthlyInterest (){
        return APR/12;
    }

    /*
    helper function to determine how much money should be paid
    based on the date of the month in which the payment is made.

    if the payment is made on or before the paymentDueDate the no interest is charged.
    if it is made after the paymentDueDate then we charge an overdue interest on the balance.
    the interest is calculated monthly.
     */
    public synchronized void monthlyPayment(){

        LocalDate paymentDate = LocalDate.now();

        if(paymentDate.getDayOfMonth() <= paymentDueDate){
            System.out.println("You should pay: " + this.getBalance());
        }else if(paymentDate.getDayOfMonth() > paymentDueDate){

             double payableAmount = this.getBalance()+(this.getBalance()*monthlyInterest()) ;
             this.setBalance(payableAmount);
            System.out.println("you should pay: " + payableAmount);
        }
    }

    /*
    Pay in money from another Current Account
    the sender must NOT be another CreditAccount, since it is illegal to use credit to pay credit
    the payment can be in full or partially.
    the payment can be on the due day, before due day or after due day
     */
    @Override
    public synchronized void deposit(Double amount, Account sender) throws Exception {
        monthlyPayment();

        if(Objects.equals(sender.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use other credit card to pay this credit card bill!");
        }else if(sender.getBalance()< amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else if(amount > this.getBalance()){
            throw new ArithmeticException("you can't pay more than you have spent!");
        }else{

            Transaction transaction = new Transaction(amount, sender, this);
            double newBalance = this.getBalance();
            newBalance +=amount;
            this.setBalance(newBalance);
            if((availableCredit+=amount)>getCreditLimit()){
                availableCredit = creditLimit;
            }else{
                availableCredit += amount;
            }
            this.addToTransaction(transaction);

        }

    }

    /*
    pay credit money to somewhere else
    balance start from 0.00, every penny spent balance should get lower and lower
    creditLimit should never be changed, that is how much money you CAN spend per month
    update availableCredit field, as more money spent it should get lower.
     */
    @Override
    public synchronized void withdraw(Double amount, Account receiver, int pin) throws Exception {

        if(pin != getPin()){
            throw new Exception("Denied Access: Incorrect Pin");
            //check if the receiving account is not a credit account, you can't use credit account pay another credit account
        }else if(Objects.equals(receiver.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use this credit card to pay another credit card!");
            //check if we still have enough credit to pay for this transaction.
        }else if(availableCredit < amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else{
            //make a new transaction goes from this credit account to any other account
            Transaction transaction = new Transaction(amount,this,receiver);
            double balance =  this.getBalance();
            balance -= amount;
            availableCredit -= amount;
            this.setBalance(balance);
            this.addToTransaction(transaction);
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
        }else if(supplyAccount.getBalance()< amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else if(amount > this.getBalance()){
            throw new ArithmeticException("you can't pay more than you have spent!");
        }else if(123 != 123 ){
            throw new Exception("you may wish to use deposit if you paying for someone else");
        }else{

            Transaction transaction = new Transaction(amount, supplyAccount, this);
            double newBalance = this.getBalance();
            newBalance +=amount;
            this.setBalance(newBalance);
            if((availableCredit+=amount)>getCreditLimit()){
                availableCredit = creditLimit;
            }else{
                availableCredit += amount;
            }
            this.addToTransaction(transaction);
        }
    }

    @Override
    public String toString(){
        return "CC Account Number: " +this.getAccountNumber()+", credit: " +getCreditLimit()+", " +
                "available Credit: " + getAvailableCreditCredit()+", balance: "+this.getBalance()+ ", " +
                "Transactions："+this.getTransactions()+".";
    }

    @Override
    public String getType() {
        return "Credit Card Account";
    }
}
