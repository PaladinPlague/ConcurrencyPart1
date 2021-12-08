import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

//A class that represents the entire set of processes within the bank used to test concurrency
public class BankSystem {

    //List of account holders for the bank
    ArrayList<AccountHolder> accHolders;
    //List of bank employees working with accounts for the bank
    ArrayList<BankEmployee> bankEmployees;
    //Lock and condition for ensuring that withdraw waits until balance grows if funds are insufficient
    Lock lock;
    Condition condition;

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
    public ArrayList<AccountHolder> getAccountHolders() {return this.accHolders;}

    //Return the list of bank employees in the bank
    public ArrayList<BankEmployee> getBankEmployees() {return this.bankEmployees;}


    //A customer tries to check the balance of their account
    public void checkBalance(int holderIndex, int accountIndex) {
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else {
            lock.lock();
            try {
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " checking balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            } finally {
                lock.unlock();
            }
        }
    }

    //A customer tries to deposit an amount of money into their account
    public void deposit(int holderIndex, int accountIndex, double amount) throws Exception{
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit amount " + amount + " is invalid, it must be greater than 0");
        } else {
            lock.lock();
            try {
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " depositing amount " + amount + " into account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                try {
                    accHolders.get(holderIndex).getAccount(accountIndex).deposit(amount, new CurrentAccount("00000000", 10000000000.00));
                } catch (Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }


    //A customer tries to withdraw an amount of money from their account
    public void withdraw(int holderIndex, int accountIndex, double amount) throws Exception {
        if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw amount " + amount + " is invalid, it must be greater than 0");
        } else {
            boolean stillWaiting = true;
            lock.lock();
            try {
                while (withdrawCondition(holderIndex, accountIndex, amount)) {
                    if (!stillWaiting)
                        Thread.currentThread().interrupt();
                    stillWaiting = condition.await(1, TimeUnit.SECONDS);
                }
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", holder " + accHolders.get(holderIndex).getName() + " withdrawing amount " + amount + " from account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber());
                try{
                    accHolders.get(holderIndex).getAccount(accountIndex).withdraw(amount, new CurrentAccount("00000000", 0.0));
                }catch(Exception e){
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }


                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            }
            finally {
                lock.unlock();
            }
        }
    }

    //An employee tries to deposit an amount of money into one of their customers' account
    public void deposit(int employeeIndex, int holderIndex, int accountIndex, double amount) throws Exception {
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit amount " + amount + " is invalid, it must be greater than 0");
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        } else {
            lock.lock();
            try {
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " depositing amount " + amount + " into account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " held by account holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName());
                try{
                    bankEmployees.get(employeeIndex).deposit(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), amount);

                }catch(Exception e){
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: deposit method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    //An employee tries to withdraw an amount of money from one of their customers' held account
    public void withdraw(int employeeIndex, int holderIndex, int accountIndex, double amount) throws Exception {
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else if (amount <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw amount " + amount + " is invalid, it must be greater than 0");
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        } else {
            boolean stillWaiting = true;
            lock.lock();
            try {
                while (withdrawCondition(holderIndex, accountIndex, amount)) {
                    if (!stillWaiting)
                        Thread.currentThread().interrupt();
                    stillWaiting = condition.await(1, TimeUnit.SECONDS);
                }
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " withdrawing amount " + amount + " from account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " held by account holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName());
                try{
                    bankEmployees.get(employeeIndex).withdraw(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), amount);

                } catch (Exception e) {
                    System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw method on account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " failed. ERROR CODE: " + e.getMessage());
                }
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", current balance of account " + accHolders.get(holderIndex).getAccount(accountIndex).getAccountNumber() + " is: " + accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
            } finally {
                lock.unlock();
            }
        }
    }

    public void changeInterest(int employeeIndex, int holderIndex, int accountIndex, double interest) {
        if (employeeIndex < 0 || employeeIndex >= bankEmployees.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: employee index " + employeeIndex + " out of bounds for bank system's bank employees");
        } else if (holderIndex < 0 || holderIndex >= accHolders.size()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " out of bounds for bank system's account holders");
        } else if (accountIndex < 0 || accountIndex >= accHolders.get(holderIndex).getSize()) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: account index " + accountIndex + " out of bounds for holder " + accHolders.get(accountIndex).getName());
        } else if (interest <= 0) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: withdraw new interest rate " + interest + " is invalid, it must be greater than 0");
        } else if (bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)) == null) {
            System.out.println("Thread with id " + Thread.currentThread().getId() + ", ERROR: holder index " + holderIndex + " for bank employee " + bankEmployees.get(employeeIndex).getEmpName() + " is invalid");
        } else {
            lock.lock();
            try {
                System.out.println("Thread with id " + Thread.currentThread().getId() + ", employee " + bankEmployees.get(employeeIndex).getEmpName() + " changing interest of account " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex).getAccountNumber() + " from holder " + bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getName() + " to rate " + interest);
                bankEmployees.get(employeeIndex).changeInterest(bankEmployees.get(employeeIndex).getCustAccount(accHolders.get(holderIndex)).getAccount(accountIndex), interest);
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

    public boolean withdrawCondition(int holderIndex, int accountIndex, double amount) {
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof CreditAccount)
            return ((CreditAccount) accHolders.get(holderIndex).getAccount(accountIndex)).getAvailableCredit() < amount;
        //We cannot withdraw from a mortgage account, so condition is always false
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof MortgageAcc)
            return false;
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof SavingAccount)
            return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() < amount && ((SavingAccount) accHolders.get(holderIndex).getAccount(accountIndex)).checkBalance(accHolders.get(holderIndex).getAccount(accountIndex).getBalance());
        if (accHolders.get(holderIndex).getAccount(accountIndex) instanceof StudentAccount)
            return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() + ((StudentAccount) accHolders.get(holderIndex).getAccount(accountIndex)).getOverdraft() < amount;
        return accHolders.get(holderIndex).getAccount(accountIndex).getBalance() < amount;
    }
}