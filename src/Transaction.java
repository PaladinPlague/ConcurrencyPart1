//Stores a single transaction made for a bank account
public class Transaction {

    //The amount put in or taken out of the account, e.g. -2.99 for payment
    private double amount;
    //The source of where the payment came from, e.g. which store a purchase was made
    private String source;

    public Transaction(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }

    //Return the amount of the payment
    public Double getAmount() {
        return this.amount;
    }

    //Return the transaction source
    public String getSource() {
        return this.source;
    }
}
