import java.util.*;

//An abstract class for the people working with accounts
public class AccountHolder {
    /*
     accounts -> hold lists of accounts
     name -> the name of the person
     */
    private ArrayList<Account> accounts;
    private String name;
    private int age;

    //Declare person as new object with initially empty list of accounts and also gets the person's name
    public AccountHolder(String personName, int age) {
        accounts = new ArrayList<>();
        name = personName;
        this.age = age;
    }

    //Get the details of an account based on index
    public Account getAccount(int index) {
        //If index is out of list range, return nothing
        if (index < 0 || index >= accounts.size()) {
            return null;
        }
        //Otherwise, return the account at this index
        return accounts.get(index);
    }

    //Create an account to add to the person's list
    public boolean addAccount(Account acc) {
        //If this account is already managed by person, do not add it and return false
        if (accounts.contains(acc)) {
            return false;
        }
        // For all accounts you need to be 18 or over to create one
        if(age < 18){
            return false;
        }
        //Otherwise, add this account to list of accounts and return true
        this.accounts.add(acc);
        return true;
    }

    //Remove an account based on index
    public boolean deleteAccount(int index) {
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
    public boolean deleteAccount(Account acc) {
        //If this account does not exist in list, do not remove an account and return false
        if (!accounts.contains(acc)) {
            return false;
        }
        //Otherwise, remove this account and return true
        accounts.remove(acc);
        return true;
    }

    //Returns the name of the person
    public String getName(){
        return name;
    }
    //Returns the age of the person
    public int getAge(){
        return age;
    }

    //Returns the size of the account array
    public int getSize(){
        return accounts.size();
    }

    //ADDED BY SCOTT -- ALLOWS AN ACCOUNT TO BE FOUND BY ITS NUMBER.
    //If not applicable, this will also have to be changed in the Bank Employees

    //Searches a customer's account for the account number and returns the match if present
    public Account getAccount(String acc) {
        for (int i = 0; i < this.getSize(); i++) {
            if (acc.equals(this.getAccount(i).getAccountNumber())) {
                return this.getAccount(i);
            }
        }
        //If there was no match, returns null
        return null;
    }
}
