import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class MortgageAcc extends Account {

    /*Notes for a Mortgage Account:
        - The account is opened with a large balance with is to be PAID OFF
        - There is no PIN with this type of account
        - Annual interest is divvied up between the 12 months
        - Monthly payments are calculated using a complicated formula
        - Total amount paid is calculated with the TOTAL monthly payments
        - When the balance reaches 0 the account is closed
        - the INTEREST of a monthly payment is the monthly % times the BALANCE
        - Best way to deal with monthly payments is probably add the funds throughout the entire month and
            then deal with the total at the end of the month
    */

    //Annual interest rate for adding to the balance - should be entered as a %
    private double intAnnual;
    //Doubly containing the monthly payment amount
    private double monthlyPayment;
    //Years the account should be open for
    private int years;
    //Constantly changed variable to store the amount STILL TO BE PAID this month
    private double currMonthPay;
    //Time an account was opened since 'monthly' payments aren't testable
    private LocalTime opened;
    //Separate balance used to avoid any miscalculations with interest
    private double monthStartBal;

    //Constructor specific to a mortgage account - these accounts are opened
    //With an already high balance and an additional interest rate. This balance
    //Is then paid down over the lifetime of the account.
    public MortgageAcc(String acc, Double bal, double interest, int year) {
        super(acc, bal);
        //Stores the interest as its decimal number
        this.intAnnual = interest * 0.01;
        //Stores the number of years equivalent to the length of the account lifetime
        this.years = year;
        //Call the calcMonthly method to get the monthly payments necessary for this particular account
        this.monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
        //Amount to be paid in total should be the number of actual payments multiplied by the monthly payments
        this.currMonthPay = this.monthlyPayment;
        //Exact time this account was opened
        this.opened = LocalTime.now();
        //The monthStartBal should be set to the original balance
        this.monthStartBal = bal;
    }

    //Returns the annual interest rate
    public double getAnnInterest() {
        return this.intAnnual;
    }

    //Returns the opening time of the account
    public LocalTime getTime() {
        return this.opened;
    }

    //Returns the interest rate for monthly payments
    public double getMonthInterest() {
        return (this.intAnnual / 12);
    }

    //Updates the interest rate
    public void updateInterest(double newInt) {
        this.intAnnual = newInt * 0.01;
        //Since the interest was updated, we also need to update the monthly payment
        this.monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
    }

    @Override
    //Returns the appropriate account type
    public String getType() {
        return "Mortgage Account";
    }

    @Override
    public void deposit(Account sender, Double amount) {

        if (amount > 0) {

            //If the balance is 0 then the mortgage has been paid off
            if (getBalance() <= 0) {
                System.out.println("This mortgage has been paid off and requires no additional payments - the account will now be closed");
                System.out.println("In addition, any funds that were overpaid will also be refunded accordingly.");
                return;
            }

            //If being paid in, then this should affect the amount left to pay this month
            this.currMonthPay = this.currMonthPay - amount;
            this.currMonthPay = Math.round(this.currMonthPay * 100);
            this.currMonthPay = this.currMonthPay / 100;

            //Since working in months isn't good for testing, it's easier to working in seconds with a month being
            //Represented by something like 30/60s
            LocalTime curr = LocalTime.now();
            //Once the duration difference is found, we need to store the seconds as an int
            int diff = (int) opened.until(curr, ChronoUnit.SECONDS);
            //If the payment is late it will move onto the next month
            if (diff > 2 /*this being for a test*/) {
                System.out.println("This is a LATE payment and will not affect the previous month's payments.");
                System.out.println("Please ensure you are keeping up with mortgage payments");
                //Recalculates the monthlyPayment to check for are any changes
                monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
                currMonthPay = monthlyPayment;
                //Resets the time
                this.opened = LocalTime.now();
            } else if (diff == 2 /*this being for a test*/) {
                //If there's still some left to pay, it should warn them the payment isn't received
                if (this.currMonthPay > 0) {
                    System.out.println("Warning! Full payment has not been received for this month - this will extend the life of your mortgage");
                }
                //If the monthly amount has been paid it must now split the payment between the interest and balance
                else if (this.currMonthPay == 0) {
                    System.out.println("Full payment received this month");
                    this.depositHelper();
                }
                //If more than the monthly payment has been deposited, this will affect the balance directly
                else {
                    System.out.println("You've paid extra this month - the excess will go straight to your balance");
                    double extra = Math.abs(currMonthPay);
                    //Calls the helper
                    this.depositHelper();
                    //Once the helper is called, the additional money has to directly lower the balance
                    double bal = getBalance();
                    double balanceDiff = bal - extra;
                    setBalance(balanceDiff);
                }
                //Recalculates the monthlyPayment to check for are any changes
                monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
                currMonthPay = monthlyPayment;
                //Resets the time
                this.opened = LocalTime.now();
                System.out.println("The following will now refer to the new month's payments");
            }
            //If the 'month' is not up, then it just takes the amount off the balance
            else {
                double bal = getBalance();
                //Take the interest away from the payment and then that number should be taken from the balance
                double paymentLessInterest = amount - (amount * getMonthInterest());
                //Inefficient but still working on this
                paymentLessInterest = Math.round(paymentLessInterest * 100);
                paymentLessInterest = paymentLessInterest / 100;
                double balanceDiff = bal - paymentLessInterest;
                setBalance(balanceDiff);
            }

            //If there's still some left to pay, it should say as much
            if (this.currMonthPay > 0) {
                System.out.println("Payment left for this month: £" + this.currMonthPay);
                System.out.println();
            }
        }
        else {
            System.out.println("You need to pay more than £0");
        }
    }

    @Override
    public void withdraw(Account receiver, Double amount) {
        //CANNOT WITHDRAW
    }

    @Override
    public void transfer() {
        //CANNOT TRANSFER
    }

    //Method used to calculate the monthly payments to be made into the account
    public double calcMonthly(double bal, double interest) {
        double monthlyInt = getMonthInterest();
        //Super messy formula for calculating monthly payments
        //P [ i(1 + i)^n ] / [ (1 + i)^n – 1]
        //Had to split the formula into part to get it to seem somewhat readable
        //Had to split the formula into part to get it to seem somewhat readable
        double part1 = (monthlyInt * Math.pow((1 + monthlyInt),(this.years * 12)));
        double part2 = Math.pow((1 + monthlyInt),(this.years * 12)) - 1;
        double payment = getBalance() * (part1 / part2);
        //return the monthly payment rounded to 2 d.f
        payment = Math.round(payment * 100);
        return payment / 100;
    }

    //Helper function to split the monthly payment between the interest and the balance
    public synchronized void depositHelper() {
        //intPaid should store the amount of the payment dedicated to the interest
        double intPaid = this.getBalance() * getMonthInterest();
        //This is then taken away from the balance and the balance is then changed accordingly
        double bal = getBalance();
        double balanceDiff = bal - (monthlyPayment - intPaid);
        setBalance(balanceDiff);
    }

    //Prints the necessary details of the account i.e. the current balance and the interest rate
    public synchronized void printDetails() {
        System.out.println("Current outstanding balance: £" + this.getBalance());
        System.out.println("Current annual interest rate: " + getAnnInterest() / 0.01 + "%");
        System.out.println("Current monthly payments: £" + this.monthlyPayment);
        System.out.println();
    }
}
