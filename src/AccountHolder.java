import java.util.*;
//Locks aren't included with above import, so this one has been added
import java.util.concurrent.locks.*;

//An abstract class for the people working with accounts
public class AccountHolder {
    /*
     accounts -> hold lists of accounts
     name -> the name of the person
     */
    private ArrayList<Account> accounts;
    private String name;
    private int age;
    private ReentrantLock lock;

    //Declare person as new object with initially empty list of accounts and also gets the person's name
    public AccountHolder(String personName, int age) {
        accounts = new ArrayList<>();
        name = personName;
        this.age = age;
        lock = new ReentrantLock();
    }

    //Get balance of one of the accounts, used for concurrency system
    public synchronized void getBalance(int index) {
        lock.lock();
        try {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + name + " checking balance of account " + accounts.get(index).getAccountNumber());
            //double result =
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accounts.get(index).getAccountNumber() + " is: " + accounts.get(index).getBalance());
        }
        finally {
            lock.unlock();
        }
    }

    //Deposit method of account holder, used for concurrency system
    public synchronized void deposit(int index, double amount, Account otherAcc) throws Exception {
        lock.lock();
        try {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + name + " depositing amount " + amount + " into account " + accounts.get(index).getAccountNumber());
            try {
                accounts.get(index).deposit(amount, otherAcc);
            } catch (Exception e) {
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", Error occurred for depositing into account " + accounts.get(index));
            }
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accounts.get(index).getAccountNumber() + " is: " + accounts.get(index).getBalance());
        }
        finally {
            lock.unlock();
        }
    }

    //Get the details of an account based on index
    public Account getAccount(int index) {
        System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + name + " checks account at index " + index);
        //If index is out of list range, return nothing
        if (index < 0 || index >= accounts.size()) {
            return null;
        }
        //Otherwise, return the account at this index
        return accounts.get(index);
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

    //Returns the name of the person
    public synchronized String getName(){
        return name;
    }
    //Returns the age of the person
    public synchronized int  getAge(){
        return age;
    }

    //Returns the size of the account array
    public synchronized int  getSize(){
        return accounts.size();
    }




}