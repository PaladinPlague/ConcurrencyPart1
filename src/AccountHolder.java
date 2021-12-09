//ArrayList is used as the datatype for list objects in system, such as list of holder's accounts, due to flexible size and useful methods for working with items at specific indexes of list
import java.util.ArrayList;

//A class for the people working with accounts
public class AccountHolder {

    //Holds list of accounts
    private ArrayList<Account> accounts;
    //The name of the person
    private String name;
    //The age of the person
    private int age;

    //Declare person as new account holder with initially empty list of accounts, and get person's name and age
    public AccountHolder(String personName, int age) {
        accounts = new ArrayList<>();
        name = personName;
        this.age = age;
    }

    //Get the details of an account based on index
    public synchronized Account getAccount(int index) {
        //If index is out of list range, return nothing
        if (index < 0 || index >= accounts.size()) {
            return null;
        }
        //Otherwise, return the account at this index
        return accounts.get(index);
    }

    //Searches a customer's accounts for the account number and returns the match if present
    public synchronized Account getAccount(String acc) {
        for (int i = 0; i < this.getSize(); i++) {
            if (acc.equals(this.getAccount(i).getAccountNumber())) {
                return this.getAccount(i);
            }
        }
        //If there was no match, returns null
        return null;
    }

    //Create an account to add to the person's list
    public synchronized boolean addAccount(Account acc) {
        //If this account is already managed by person, do not add it and return false
        if (accounts.contains(acc)) {
            return false;
        }
        // For all accounts you need to be 18 or over to create one
        if(age < 18){
            return false;
        }
        //If age is greater than or equal to 18, add this account to list of accounts and return true
        this.accounts.add(acc);
        return true;
    }

    //Remove an account based on index
    public synchronized boolean deleteAccount(int index) {
        //Validate account's existence through helper method
        Account acc = getAccount(index);
        //If index is out of list range, do not remove an account and return false
        if (acc == null) {
            return false;
        }
        //Otherwise, remove the account at this index and return true
        accounts.remove(index);
        return true;
    }

    //Remove an account via the account itself as a parameter
    public synchronized boolean deleteAccount(Account acc) {
        //If this account does not exist in list, do not remove an account and return false
        if (!accounts.contains(acc)) {
            return false;
        }
        //Otherwise, remove this account and return true
        accounts.remove(acc);
        return true;
    }

    //Returns the name of the person
    public synchronized String getName(){ return name; }

    //Returns the age of the person
    public synchronized int getAge(){ return age; }

    //Returns the size of the account array
    public synchronized int getSize(){
        return accounts.size();
    }
}