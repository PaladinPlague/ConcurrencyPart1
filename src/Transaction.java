//Stores a single transaction made for a bank account
public class Transaction {

    //The amount put in or taken out of the account, e.g. 2.99 for payment
    private double amount;
    //The source of where the payment came from, e.g. which store a purchase was made
    private Account source;
    //The account which received the payment
    private Account receiver;

    public Transaction(double amount, Account source, Account receiver) {
        this.amount = amount;
        this.source = source;
        this.receiver = receiver;
    }

    //Return the amount of the payment
    public Double getAmount() {
        return this.amount;
    }

    //Return the transaction source
    public Account getSource() {
        return this.source;
    }

    //Return the receiver of the transaction
    public Account getReceiver() { return this.receiver; }
    @Override
    public String toString(){
        return "From: " + source.getAccountNumber()+ " To: "+ receiver.getAccountNumber()+" Amount: " + amount ;
    }

}
