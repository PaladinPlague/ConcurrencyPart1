import java.util.ArrayList;

public class MortgageAcc extends Account {

    //Interest rate for adding to the balance
    private double intRate;

    //Constructor specific to a mortgage account - these accounts are opened
    //With an already high balance and an additional interest rate. This balance
    //Is then paid down over the lifetime of the account.
    public MortgageAcc(String acc, Double bal, double interest) {
        super(acc, bal);
        this.intRate = interest;
    }

    //Returns the current account type - in this case "Mortgage Account"
    @Override
    public String getType() {
        return "Mortgage Account";
    }

    //Returns the current interest rate
    public double getInterest() {
        return this.intRate;
    }

    //Updates the interest rate
    public void updateInterest(double newInt) {
        this.intRate = newInt;
    }

    //Update the current balance of the account based on the current interest rate
    //Due to editing information, declare the class as being synchronized
    public synchronized void addInterest() {
        double addedInt = super.getBalance() * this.intRate;
        //Round the added interest amount to 2 decimal places
        addedInt = Math.round(addedInt * 100.0) / 100.0;
        Transaction trans = new Transaction(addedInt, "Interest");
        updateBalance(trans);
    }

    //Update the current balance of the class and store the transaction for this
    //Due to editing information, declare the class as being synchronized
    //Additional this haS to pass in negative payment as it's paying off the
    //Mortgage.
    public synchronized void payMortgage(double payment) {
        Transaction trans = new Transaction(-payment, "Repayment");
        super.updateBalance(trans);
    }
}
