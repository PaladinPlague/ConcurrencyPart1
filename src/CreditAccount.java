import java.time.LocalDate;
import java.util.Objects;
import java.util.ArrayList;
/*
Author: Nuoxu Li
CreditAccount class is to simulate real life credit card.
This class keep
 */

public class CreditAccount extends Account  {

    private double creditLimit;
    private double availableCredit;
    private double APR;
    private final int paymentDueDate;
    LocalDate paymentDate;
    //private Person cardHolder;

    public CreditAccount(String accountNo, Double openingBalance, double openingCredit, double APR) {
        super(accountNo, openingBalance);

        this.creditLimit = openingCredit;
        this.availableCredit = openingCredit;
        this.APR = APR;
        paymentDueDate = 15;
    }


    /*
    getter and setter methods for all fields
     */

    //set Credit, rarely used in real life
    public void setCreditLimit(double newCredit){
        creditLimit = newCredit;
    }

    //set up new APR, rarely used in real life
    public void setAPR(double newAPR){
        APR = newAPR;
    }

    public void setPaymentDate(LocalDate paymentDate){

        /*
        setPaymentDate( new LocalDate ())
         */
        this.paymentDate = paymentDate;
    }

    //get credit return the credit
    public double getCreditLimit(){
        return creditLimit;
    }

    //get credit return the current available credit
    public double getAvailableCredit(){
        return availableCredit;
    }

    /*
    we also need a get balance class but that has been defined in abstract class
     */

    //get APR, return the current APR
    public double getAPR(){
        return APR;
    }

    public double monthlyInterest (){
        return APR/12;
    }

    /*
    helper function to determine how much money should be paid
    based on the date of the month in which the payment is made.

    if the payment is made on or before the paymentDueDate the no interest is charged.
    if it is made after the paymentDueDate then we charge an overdue interest on the balance.
    the interest is calculated monthly.
     */
    public void monthlyPayment(){

        if(paymentDate.getDayOfMonth() <= paymentDueDate){
            System.out.println("You should pay: " + Math.abs(this.getBalance()));
        }else{
            double payableAmount = this.getBalance()+(this.getBalance()*monthlyInterest()) ;
            this.setBalance(payableAmount);
            System.out.println("Your monthly Interest is "+monthlyInterest()+" you should pay: " + Math.abs(this.getBalance()));
        }
    }

    /*
    Pay in money from another Current Account
    the sender must NOT be another CreditAccount, since it is illegal to use credit to pay credit
    the payment can be in full or partially.
    the payment can be on the due day, before due day or after due day
     */
    @Override
    public void deposit(Double amount, Account sender) throws Exception {
        monthlyPayment();

        if(Objects.equals(sender.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use other credit card to pay this credit card bill!");
        }else if(Math.abs(sender.getBalance()) < amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else if(amount > Math.abs(this.getBalance())){
            throw new ArithmeticException("you can't pay more than you have spent!");
        }else{

            Transaction transaction = new Transaction(amount, sender, this);
            setBalance(getBalance()+amount);
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
    public void withdraw(Double amount, Account receiver) throws Exception {

        if(Objects.equals(receiver.getType(), "Credit Card Account")){
            throw new Exception("Sorry， You can't use this credit card to pay another credit card!");
            //check if we still have enough credit to pay for this transaction.
        }else if(availableCredit < amount){
            throw new ArithmeticException("Sorry, insufficient fund.");
        }else{
            //make a new transaction goes from this credit account to any other account
            Transaction transaction = new Transaction(amount,this,receiver);
            availableCredit -= amount;
            setBalance(getBalance()-amount);
            addToTransaction(transaction);
        }

    }

    /*
    Pay in money from another Current Account
    the sender must BE SAME PERSON'S CURRENT ACCOUNT.
    the payment can be in full or partially.
    the payment can be on the due day, before due day or after due day
    */
    public void transfer (double amount, Account supplyAccount) throws Exception {

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

            Transaction transaction = new Transaction(amount, supplyAccount, this);
            setBalance(getBalance()+amount);
            if((availableCredit+=amount)>getCreditLimit()){
                availableCredit = creditLimit;
            }else{
                availableCredit += amount;
            }
            addToTransaction(transaction);
        }
    }

    @Override
    public void printDetails(){
        System.out.println("CC Account Number: " +this.getAccountNumber());
        System.out.println("Total Credit Limit: " +getCreditLimit());
        System.out.println("Available Credit of This Month: " + getAvailableCredit());
        System.out.println("New Balance: "+this.getBalance());
        System.out.println("List of Transactions: " + this.getTransactions());
    }

    @Override
    public String getDetails(){
        ArrayList<Transaction> transactions = this.getTransactions();
        String result = "CC Account Number: " +this.getAccountNumber()+", credit: " +getCreditLimit()+", " +
                "available Credit: " + getAvailableCredit()+", balance: "+this.getBalance()+ ", " +
                "Transactions：" + "[";
        for (int i = 0; i < transactions.size(); i++) {
            result += "From: " + transactions.get(i).getSource().getAccountNumber();
            result += " To: " + transactions.get(i).getReceiver().getAccountNumber();
            result += " Amount: " + transactions.get(i).getAmount();
            if (i < transactions.size() - 1) {
                result += ", ";
            } else {
                result += "]";
            }
        }

        result += ".";

        return result;
    }

    @Override
    public String getType() {
        return "Credit Card Account";
    }
}