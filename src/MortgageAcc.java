import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

//Bank account type which is used for customers with mortgage
public class MortgageAcc extends Account {

    /*
    Notes for a Mortgage Account:
        - The account is opened with a large balance with is to be PAID OFF
        - There is no PIN with this type of account
        - Annual interest is divvied up between the 12 months
        - Monthly payments are calculated using a complicated formula
        - Total amount paid is calculated with the TOTAL monthly payments
        - When the balance reaches 0 the account is closed, the INTEREST of a monthly payment is the monthly % times the BALANCE
        - Best way to deal with monthly payments is probably add the funds throughout the entire month and then deal with the total at the end of the month

        - Add transactions to the deposit method using something like a helper
     */

    //Annual interest rate for adding to the balance - should be entered as a %
    private double intAnnual;
    //Doubly containing the monthly payment amount
    private double monthlyPayment;
    //Years the account should be open for
    private final int years;
    //Constantly changed variable to store the amount still to be paid for the current month
    private double currMonthPay;
    //Time an account was opened since 'monthly' payments aren't testable and to be used as an anchor for checking times
    private LocalTime start;

    //Constructor specific to a mortgage account - these accounts are opened
    //With an already high balance and an additional interest rate. This balance
    //Is then paid down over the lifetime of the account.
    public MortgageAcc(String acc, Double bal, double interest, int year) {
        //Set account number and opening balance via superclass constructor.
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
        this.start = LocalTime.now();
    }

    //Returns the annual interest rate
    public synchronized double getAnnInterest() {
        return this.intAnnual;
    }

    //Returns the opening time of the account
    public synchronized LocalTime getTime() {
        return this.start;
    }

    //Returns the interest rate for monthly payments (which is one twelfth of interest rate for annual payments)
    public synchronized double getMonthInterest() {
        return (this.intAnnual / 12);
    }

    //Updates the interest rate
    public synchronized void updateInterest(double newInt) {
        //We convert parameter input from percentage number to whole number, e.g. if newInt = 2, as in 2%, set interest rate to 0.02
        this.intAnnual = newInt * 0.01;
        //Since the interest was updated, we also need to update the monthly payment
        this.monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
    }

    //Returns the appropriate account type
    @Override
    public synchronized String getType() {
        return "Mortgage Account";
    }

    //Simulate the effects of monthly payment conditions resetting
    public synchronized void nextMonth() {
        //Recalculates the monthlyPayment to check for any changes
        monthlyPayment = calcMonthly(this.getBalance(), this.intAnnual);
        currMonthPay = monthlyPayment;
        //Resets the time for checking monthly payments
        this.start = LocalTime.now();
    }

    //Pay in money from another account
    //Sender must not be credit account, as it is illegal to use credit to pay mortgage
    //Payments to this account must be at least £100
    @Override
    public synchronized void deposit(Double amount, Account sender) throws Exception {

        //Mortgage payments should not be accepted from a credit account
        if (Objects.equals(sender.getType(), "Credit Card Account")) {
            System.out.println("A mortgage payment CANNOT be paid by a credit account.");
            return;
        }

        //Only allows a payment of at least £100
        if (amount > 99) {

            //Since working in months isn't good for testing, it's easier to working in seconds with a month being in roughly 30-60 range
            LocalTime curr = LocalTime.now();
            //Once the duration difference is found, we need to store the seconds as an int
            double diff = (double) start.until(curr, ChronoUnit.SECONDS);

            //Check the diff to see if the current monthly payment period had passed.
            //If so, print messages accordingly whether it was under/over/fully paid.
            if (diff > 2.0 && currMonthPay > 0) {
                System.out.println("The payment for the passing month was NOT fulfilled.");
                System.out.println("Please ensure you are keeping up with mortgage payments.");
                nextMonth();
                diff = 0;
            }
            //If the monthly payment has been paid fully, it will change the balance and then move to the next month
            else if (diff >= 2.0 && currMonthPay == 0) {
                //Splits the payment into interest and balance
                //System.out.println("The payment for the passing month was fulfilled.");
                nextMonth();
                diff = 0;
            }
            //If the monthly payment is overpaid then it sends the excess straight into the balance
            else if (diff >= 2.0 && currMonthPay < 0) {
                System.out.println("You paid extra this month - any excess has gone straight to your balance");
                nextMonth();
                diff = 0;
            }

            //After the new month is set up, the amount the user WANTED to deposit should now be used in the current month. We can do this by manipulating diff and setting it to 0.

            //Otherwise, if the 'month' has not passed, we deal with the amount and pay it into the account
            if (diff < 2.0) {

                //If the monthly payment is already paid, the money goes straight to balance
                if (currMonthPay == 0) {
                    System.out.println("This month's payment has already been fulfilled - this amount will affect your balance DIRECTLY");
                    double bal = getBalance();
                    double balanceDiff = bal - amount;
                    setBalance(balanceDiff);
                    return;
                }
                //Otherwise, it goes through the process as normal
                else {

                    //When paid in, we need to update the current monthly pay balance to determine what to do with the amount
                    this.currMonthPay = this.currMonthPay - amount;
                    this.currMonthPay = Math.round(this.currMonthPay * 100);
                    this.currMonthPay = this.currMonthPay / 100;

                    //If there is still money to be paid, the number is printed
                    if (this.currMonthPay > 0) {
                        this.depositHelper(amount);
                        System.out.println("Payment left for this month: £" + this.currMonthPay);
                    }
                    //If the number is exactly paid, this fact is printed
                    else if (this.currMonthPay == 0) {
                        System.out.println("The payment for this month has been fulfilled.");
                        this.depositHelper(amount);
                    }
                    //Otherwise, if MORE than the monthly payment is paid, this extra goes straight to the balance
                    else {
                        //Store the extra money
                        double extra = Math.abs(currMonthPay);
                        //We need to take this extra off of amount since interest isn't applied to it
                        this.depositHelper(amount - extra);
                        //This extra is then taken directly off of balance
                        double bal = getBalance();
                        double balanceDiff = bal - extra;
                        setBalance(balanceDiff);
                    }
                }
            }
        }
        //Print an error if less than the minimum is deposited
        else {
            System.out.println("You need to pay more than £100 to make an impact on your mortgage");
        }

        //If the balance is 0 then the mortgage has been paid
        if (getBalance() <= 0) {
            System.out.println("This mortgage has now been paid off and requires no additional payments - the account will now be closed");
            System.out.println("In addition, any funds that were overpaid will also be refunded accordingly.");
            return;
        }
    }

    //While all accounts require withdraw method, we cannot withdraw from a Mortgage account, so show this in output
    @Override
    public synchronized void withdraw(Double amount, Account receiver) throws Exception {
        System.out.println("Cannot withdraw from mortgage account");
    }

    //Method used to calculate the monthly payments to be made into the account
    public synchronized double calcMonthly(double bal, double interest) {
        //Use monthly interest to calculate monthly payments
        double monthlyInt = getMonthInterest();
        //Formula for calculating monthly payments: P [ i(1 + i)^n ] / [ (1 + i)^n – 1]
        //Had to split the formula into part to get it to seem somewhat readable
        double part1 = (monthlyInt * Math.pow((1 + monthlyInt),(this.years * 12)));
        double part2 = Math.pow((1 + monthlyInt),(this.years * 12)) - 1;
        double payment = getBalance() * (part1 / part2);
        //return the monthly payment rounded to 2 d.f
        payment = Math.round(payment * 100);
        return payment / 100;
    }

    //Helper function to split the monthly payment between the interest and the balance
    public synchronized void depositHelper(double deposit) {
        //intPaid should store the amount of the payment dedicated to the interest
        double intPaid = deposit * getMonthInterest();
        //This interest needs to be rounded for monetary use
        intPaid = Math.round(intPaid * 100);
        intPaid = intPaid / 100;
        //This is then taken away from the deposit and the balance is then changed accordingly
        double bal = getBalance();
        double balanceDiff = bal - (deposit - intPaid);
        setBalance(balanceDiff);
    }

    //Prints the necessary details of the account i.e. the current balance and the interest rate
    @Override
    public void printDetails() {
        System.out.println("Current outstanding balance: £" + this.getBalance());
        System.out.println("Current annual interest rate: " + getAnnInterest() / 0.01 + "%");
        System.out.println("Recent monthly payment: £" + this.monthlyPayment);
    }
}