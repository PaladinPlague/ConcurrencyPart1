import java.util.*;

//An abstract class for the people working with accounts
public abstract class Person {

    //Holds a list of accounts managed by person
    private ArrayList<Account> accounts;

    //Declare person as new object with initially empty list of accounts
    public Person() {
        accounts = new ArrayList<>();
    }

    //Carry out depositing method depending on what process is used, search this person's account by index
    public synchronized void deposit(int thisAccIndex, Account otherAcc) {
        //Get the user's account at this index
        Account thisAcc = getAccount(thisAccIndex);
        //If the index is not out of range, complete the fully defined code for deposit
        if (thisAcc != null) {
            deposit(thisAcc, otherAcc);
        }
    };

    //Carry out depositing method depending on what process is used, search this person's account by object
    public abstract void deposit(Account thisAcc, Account otherAcc);

    //Carry out the withdraw method depending on process by person, search this person's account by index
    public synchronized void withdraw(int thisAccIndex, Account otherAcc) {
        //Get the user's account at this index
        Account thisAcc = getAccount(thisAccIndex);
        //If the index is not out of range, complete the fully defined code for deposit
        if (thisAcc != null) {
            withdraw(thisAcc, otherAcc);
        }
    };

    //Carry out the withdraw method depending on process by person, search this person's account by object
    public abstract void withdraw(Account thisAcc, Account otherAcc);

    //Carryout the transfer methods for this person, search both accounts by index
    public synchronized void transfer(int acc1Index, int acc2Index) {
        //Get the user's accounts at the specified indexes
        Account acc1 = getAccount(acc1Index);
        Account acc2 = getAccount(acc2Index);
        //If the two indexes are not out of range, and they are not the same, complete the fully defined code for transfer
        if (acc1 != null && acc2 != null && acc1Index != acc2Index) {
            transfer(acc1, acc2);
        }
    };

    //Carryout the transfer methods for this person, search 1st account by index and 2nd account by object
    public synchronized void transfer(int acc1Index, Account acc2) {
        //Get the user's account at the index for the 1st account
        Account acc1 = getAccount(acc1Index);
        //If the index is not out of range, complete the fully defined code for transfer
        if (acc1 != null) {
            transfer(acc1, acc2);
        }
    }

    //Carryout the transfer methods for this person, search 1st account by object and 2nd account by index
    public synchronized void transfer(Account acc1, int acc2Index) {
        //Get the user's account at the index for the 2nd account
        Account acc2 = getAccount(acc2Index);
        //If the index is not out of range, complete the fully defined code for transfer
        if (acc2 != null) {
            transfer(acc1, acc2);
        }
    }

    //Carryout the transfer methods for this person, search both accounts by object
    public abstract void transfer(Account acc1, Account acc2);

    //Get the details of an account based on index
    public Account getAccount(int index) {
        //If index is out of list range, return nothing
        if (index < 0 || index >= accounts.size()) {
            return null;
        }
        //Otherwise, return the account at this index
        return accounts.get(index);
    }

    //Get the details of an account based on object reference
    //This seems pointless, but it's used as a helper method to ensure for other methods that object parameters are in this person's list
    //(ArrayLists in Java don't have a get function corresponding to object types)
    public Account getAccount(Account acc) {
        //Move through list of accounts until we find the parameter account
        for (int i = 0; i < accounts.size(); i++) {
            //If the account in the list is equal to the parameter account, return it
            if (accounts.get(i).equals(acc)) {
                return accounts.get(i);
            }
        }
        //If we could not find the object, return null
        return null;
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

    //Print the details of an account using index
    public boolean printAccountDetails(int index) {
        //Validate account's existence through helper method
        Account acc = getAccount(index);
        //If account does not exist, show error method and return false
        if (acc == null) {
            System.out.println("ERROR: index " + index + " is out of range of account length.");
            return false;
        }
        //Otherwise, print account's details and return true
        acc.printDetails();
        return true;
    }

    //Print the details of an account using the account itself
    public boolean printAccountDetails(Account acc) {
        //Validate account's existence through helper method
        acc = getAccount(acc);
        //If account does not exist, show error method and return false
        if (acc == null) {
            System.out.println("ERROR: Account does not exist in list of accounts for this person.");
            return false;
        }
        //Otherwise, print account's details and return true
        acc.printDetails();
        return true;
    }

    //Print the details of an account using index as a string (for GUI purposes)
    public String getAccountDetails(int index) {
        //Validate account's existence through helper method
        Account acc = getAccount(index);
        //If index is out of list range, show error message
        if (acc == null) {
            return "ERROR: index " + index + " is out of range of account length.";
        }
        //Otherwise, print this account's details
        return acc.getDetails();
    }

    //Print the details of an account using the account itself as a string (for GUI purposes)
    public String getAccountDetails(Account acc) {
        //Validate account's existence through helper method
        acc = getAccount(acc);
        //If account does not exist, show error method
        if (acc == null) {
            return "ERROR: account does not appear in this person's list of accounts.";
        }
        //Otherwise, get account details
        return acc.getDetails();
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

    //Set the balance of an account in the list using an index
    public synchronized boolean updateAccountBalance(int index, Double amount) {
        //Validate account's existence through helper method
        Account acc = getAccount(index);
        //If index is out of list range, do not update account balance and return false
        if (acc == null) {
            return false;
        }
        //Otherwise, set this account's balance to our amount
        acc.setBalance(amount);
        return true;
    }

    //Set the balance of an account in the list using the account itself as a parameter
    public synchronized boolean updateAccountBalance(Account acc, Double amount) {
        //Validate account's existence through helper method
        acc = getAccount(acc);
        //If account does not exist, return false
        if (acc == null) {
            return false;
        }
        //Otherwise, update its balance and return true
        acc.setBalance(amount);
        return true;
    }
}
