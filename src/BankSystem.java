import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

//A class that represents the processes of the bank used to test concurrency
public class BankSystem {

    //List of account holders for the bank
    ArrayList<AccountHolder> accHolders;
    //List of bank employees working with accounts for the bank
    ArrayList<BankEmployee> bankEmployees;
    //Lock and condition for ensuring that withdraw waits until balance grows if funds are insufficient
    Lock lock;
    Condition condition;

    //Construct a new bank system with empty lists of account holders and employees, and construct the lock and condition objects
    public BankSystem() {
        accHolders = new ArrayList<>();
        bankEmployees = new ArrayList<>();
        lock = new ReentrantLock();
        condition = lock.newCondition();
    }

    //Add a new account holder to the system
    public void addAccountHolder(AccountHolder holder) {
        lock.lock();
        try {
            accHolders.add(holder);
        } finally {
            lock.unlock();
        }
    }

    //Add a new bank employee to the system
    public void addBankEmployee(BankEmployee employee) {
        lock.lock();
        try {
            bankEmployees.add(employee);
        } finally {
            lock.unlock();
        }
    }

    //Return the list of account holders in the bank
    public synchronized ArrayList<AccountHolder> getAccountHolders() {return this.accHolders;}

    //Return the list of bank employees in the bank
    public synchronized ArrayList<BankEmployee> getBankEmployees() {return this.bankEmployees;}

    //A customer tries to check the balance of their account
    public void checkBalance(int holderIndex, int accountIndex) {
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //Otherwise, show the balance of the account gotten by this employee
        } else {
            lock.lock();
            try {
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " checking balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                //Synchronized process is finished - show results of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            } finally {
                lock.unlock();
            }
        }
    }

    //A customer tries to deposit an amount of money into one of their accounts
    public void deposit(int holderIndex, int accountIndex, double amount) throws Exception{
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //If the deposit amount is less or equal to zero, show this in a message
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit amount " + amount + " is invalid, it must be greater than 0");
        //Otherwise, deposit the set amount into the specified account
        } else {
            lock.lock();
            try {
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " depositing amount " + amount + " into account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                //Call the deposit method of the account gotten from the account holder, and catch any exceptions caught with a relevant message shown
                try {
                    accHolders.get(holderIndex).getAccount(accountIndex).deposit(amount, new CurrentAccount("00000000", 10000000000.00));
                } catch (Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                //Synchronized process is finished - show results of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
                //Call the signalAll function of the condition, so other threads waiting on a value update from this thread become updated
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    //A customer tries to withdraw an amount of money from one of their accounts
    public void withdraw(int holderIndex, int accountIndex, double amount) throws Exception {
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //If the withdraw amount is less or equal to zero, show this in a message
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw amount " + amount + " is invalid, it must be greater than 0");
        //Otherwise, withdraw the set amount from the specified account
        } else {
            //Declare boolean variable that is true until we are able to withdraw from the account or the thread waits too long
            boolean stillWaiting = true;
            lock.lock();
            try {
                //Loop which waits until we are able to withdraw from the account and may interrupt the whole thread process if it waits too long
                while (withdrawCondition(holderIndex, accountIndex, amount)) {
                    if (!stillWaiting)
                        Thread.currentThread().interrupt();
                    stillWaiting = condition.await(1, TimeUnit.SECONDS);
                }
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " withdrawing amount " + amount + " from account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                //Call the withdraw method of the account gotten from the account holder, and catch any exceptions caught with a relevant message shown
                try{
                    accHolders.get(holderIndex).getAccount(accountIndex).withdraw(amount, new CurrentAccount("00000000", 0.0));
                }catch(Exception e){
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                //Synchronized process is finished - show results of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            }
            finally {
                lock.unlock();
            }
        }
    }

    //An employee tries to deposit an amount of money into one of their customers' account
    public void deposit(int employeeIndex, int holderIndex, int accountIndex, double amount) throws Exception {
        //If the employee index is out of bounds for the list of bank employees in the system, show this in a message
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //If the deposit amount is less or equal to zero, show this in a message
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit amount " + amount + " is invalid, it must be greater than 0");
        //If the customer is not overseen by the employee, show this in a message
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        //Otherwise, deposit the set amount into the specified account
        } else {
            lock.lock();
            try {
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " depositing amount " + amount + " into account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " held by account holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName());
                //Call the deposit method of the account gotten from the account holder via the bank employee, and catch any exceptions caught with a relevant message shown
                try{
                    bankEmployees.get(employeeIndex).deposit(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), amount);
                } catch(Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                //Synchronized process is finished - show results of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
                //Call the signalAll function of the condition, so other threads waiting on a value update from this thread become updated
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    //An employee tries to withdraw an amount of money from one of their customers' held account
    public void withdraw(int employeeIndex, int holderIndex, int accountIndex, double amount) throws Exception {
        //If the employee index is out of bounds for the list of bank employees in the system, show this in a message
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //If the withdraw amount is less or equal to zero, show this in a message
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw amount " + amount + " is invalid, it must be greater than 0");
        //If the customer is not overseen by the employee, show this in a message
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        //Otherwise, withdraw the set amount from the specified account
        } else {
            //Declare boolean variable that is true until we are able to withdraw from the account or the thread waits too long
            boolean stillWaiting = true;
            lock.lock();
            try {
                //Loop which waits until we are able to withdraw from the account and may interrupt the whole thread process if it waits too long
                while (withdrawCondition(holderIndex, accountIndex, amount)) {
                    if (!stillWaiting)
                        Thread.currentThread().interrupt();
                    stillWaiting = condition.await(1, TimeUnit.SECONDS);
                }
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " withdrawing amount " + amount + " from account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " held by account holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName());
                //Call the withdraw method of the account gotten from the account holder via the bank employee, and catch any exceptions caught with a relevant message shown
                try{
                    bankEmployees.get(employeeIndex).withdraw(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), amount);
                } catch (Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                //Synchronized process is finished - show results of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            } finally {
                lock.unlock();
            }
        }
    }

    //An employee tries to change the interest rate of one of their customers' held account
    public void changeInterest(int employeeIndex, int holderIndex, int accountIndex, double interest) {
        //If the employee index is out of bounds for the list of bank employees in the system, show this in a message
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        //If the holder index is out of bounds for the list of account holders in the system, show this in a message
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        //If the account index is out of bounds for the list of accounts held by the account holder, show this in a message
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        //If the new interest rate is less or equal to zero, show this in a message
        } else if (interest <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw new interest rate " + interest + " is invalid, it must be greater than 0");
        //If the customer is not overseen by the employee, show this in a message
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        //Otherwise, change the interest for the account via specified employee
        } else {
            lock.lock();
            try {
                //Synchronized process starts - show command of thread
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " changing interest of account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " from holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName() + " to rate " + interest);
                //Call the interest change method of the bank employee using the specified holder and account as parameters, and catch any exceptions caught with a relevant message shown
                try{
                    bankEmployees.get(employeeIndex).changeInterest(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), interest);
                } catch (Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: interest change method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                //Synchronized process is finished - show results of thread related specifically to account type used for process
                if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex) instanceof MortgageAcc) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", monthly payment of account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " is " + ((MortgageAcc) bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex)).getMonthInterest());
                } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex) instanceof CreditAccount) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", APR of account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " is " + ((CreditAccount) bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex)).getAPR());
                } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex) instanceof SavingAccount) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", interest rate of account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " is " + ((SavingAccount) bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex)).getInterestRate());
                } else {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " is not account type with set interest");
                }
            } finally {
                lock.unlock();
            }
        }
    }

    //Helper function to show if it is possible to deposit an amount from a bank account or not
    //This is used as a helper function exclusively via other methods in this class where all parameters are checked beforehand, therefore it is safe to assume the parameters for this method are valid
    private boolean withdrawCondition(int holderIndex, int accountIndex, double amount) {
        //For a credit account, the condition is true if the available credit of the account is less than the specified amount
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof CreditAccount)
            return ((CreditAccount) accHolders.get(holderIndex).getAccount(accountIndex)).getAvailableCredit() < amount;
        //We cannot withdraw from a mortgage account, so condition is always false
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof MortgageAcc)
            return false;
        //For a savings account, the condition is true if the balance of the account is less than the amount and the conditions of the account are tested to show it is suitable for taking out this amount
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof SavingAccount)
            return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() < amount && ((SavingAccount) accHolders.get(holderIndex).getAccount(accountIndex)).checkBalance(accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
        //For a student account, the condition is true if the balance of the account combined with the account's overdraft is less than the specified amount
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof StudentAccount)
            return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() + ((StudentAccount) accHolders.get(holderIndex).getAccount(accountIndex)).getOverdraft() < amount;
        //For a "default" condition (which is used by the current account - the class which does not have a predetermined withdraw condition), the condition is true if the balance of the account is less than the specified amount
        return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() < amount;
    }
}