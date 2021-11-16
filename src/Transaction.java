import java.util.Objects;

//Stores a single transaction made for a bank account
public class Transaction {

    //The amount put in or taken out of the account, e.g. 2.99 for payment
    private double amount;
    //The source of where the payment came from, e.g. which store a purchase was made
    private Account source;
    //The account which received the payment
    private Account receiver;
    //The String variable to keep track of status of transaction(request/response )
    private boolean success;

    public Transaction(double amount, Account source, Account receiver) {
        this.amount = amount;
        this.source = source;
        this.receiver = receiver;
        success = true;
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


    public boolean getSuccess(){
        return success;
    }

    public void setSuccess(boolean status){
        success = status;
    }

    @Override
    public String toString(){
        return "From: " + source.getAccountNumber()+ " To: "+ receiver.getAccountNumber()+" Amount: " + amount ;
    }


    public void processTransaction(String transType){


        if(Objects.equals(transType, "Withdraw from account")){

            System.out.println("we should deposit into destination account: "+receiver.getAccountNumber());
            try {
                //success = true;
                String action = "response";
                receiver.deposit(amount,source,action);
            } catch (Exception e) {
                success = false;
                e.printStackTrace();
            }
        }else if(Objects.equals(transType, "deposit to account")){

            System.out.println("we should withdraw from account: "+source.getAccountNumber());
            try{
                //success = true;
                String action = "response";
                source.withdraw(amount,receiver,action);
            }catch (Exception e){
                success = false;
                e.printStackTrace();
            }
        }

    }

}
