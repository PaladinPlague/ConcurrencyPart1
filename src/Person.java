import java.util.*;

public abstract class Person {

    //Holds a list of accounts managed by person
    private ArrayList<Account> accounts;

    //Declare person as new object with initially empty list of accounts
    public Person() {
        accounts = new ArrayList<>();
    }

    //Carry out depositing method depending on what process is used
    public abstract void deposit();

    //Carry out the withdraw method depending on process by person
    public abstract void withdraw();

    //Carryout the transfer methods for this person
    public abstract void transfer();

    //Get the details of an account based on index
    public Account getAccount(int index) {
        //If index is out of list range, return nothing
        if (index < 0 || index >= accounts.size()) {
            return null;
        }
        //Otherwise, return the account at this index
        return accounts.get(index);
    }

    //Get the total balance available for a person through all their accounts
    public Double getTotalBalance() {
        //Initialise balance as 0
        double result = 0;
        //For each account stored for person, add its balance to result
        for (int i = 0; i < accounts.size(); i++) {
            result += accounts.get(i).getBalance();
        }
        //Return the total balance
        return result;
    }

    //Create an account to add to the person's list
    //NOTE: I'm not sure if we should consider this as "creating" an account just yet or if we should
    public synchronized boolean addAccount(Account acc) {
        //If this account is already managed by person, do not add it and return false
        if (accounts.contains(acc)) {
            return false;
        }
        //Otherwise, add this account to list of accounts and return true
        this.accounts.add(acc);
        return true;
    }

    //Remove an account based on index
    public synchronized boolean deleteAccount(int index) {
        //If index is out of list range, do not remove an account and return false
        if (index < 0 || index >= accounts.size()) {
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

    //Set the balance of an account in the list using an index
    public synchronized boolean updateAccountBalance(int index, Double amount) {
        //If index is out of list range, do not update account balance and return false
        if (index < 0 || index >= accounts.size()) {
            return false;
        }
        //Otherwise, set this account's balance to our amount
        accounts.get(index).setBalance(amount);
        return true;
    }

    //Set the balance of an account in the list using the account itself as a parameter
    public synchronized boolean updateAccountBalance(Account acc, Double amount) {
        //Move through list of accounts until we find the parameter account
        //(ArrayLists in Java don't have a get function corresponding to object types)
        for (int i = 0; i < accounts.size(); i++) {
            //If the account in the list is equal to the parameter account, update its balance and return true
            if (accounts.get(i).equals(acc)) {
                accounts.get(i).setBalance(amount);
                return true;
            }
        }
        //If we could not find the object, return false
        return false;
    }
}
