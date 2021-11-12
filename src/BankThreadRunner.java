/**
 * This program runs threads that perform operations on the banking system
 */
public class BankThreadRunner {
    public static void main(String[] args) {
        CurrentAccount acc1 = new CurrentAccount("11111111", 50.00);
        CurrentAccount acc2 = new CurrentAccount("22222222", 50.00);

        //Implement Runnable objects here: Objects that would call methods of other objects at delayed occassions
        //DISCUSS: We need to have the runnable objects ready for this. These would be classess where methods are called at regular intervals.
        //Current thoughts: Create new classes that take an account and call a function in a basic method "run()"
        //This may result in lots of classes being called, however

        //Declare Thread objects as threads based on runnable objects

        //Call the start method of each thread object

    }
}
