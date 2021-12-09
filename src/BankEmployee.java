import java.util.ArrayList;

//A class holding the bank employees who oversee a set of account holders, and manage their accounts using various methods
public class BankEmployee {

    /*
    A Bank Employee should be able to:
        - Show the basic details of all accounts under a person's name (type and number) _/
        - Show ALL details of a user's specific account _/
        - Put money into a customer's account _/
        - Pull money out of a customer's account _/
        - Create an account _/
        - Delete an account _/
    A Bank Employee should ALSO be able to:
        - Change interest rate (Mortgage Account/Savings Account/Credit Account) _/
        - Change overdraft limit (Student Account) _/
     */

    //Holds list of account holders
    private ArrayList<AccountHolder> holders;
    //The employee number given to this employee
    private String employeeNo;
    //The name of the employee
    private String empName;

    //Initializes a very basic employee with information like name and number, with list of account owners initially being empty
    public BankEmployee(String name, String employeeNumber) {
        this.employeeNo = employeeNumber;
        this.empName = name;
        this.holders = new ArrayList<>();
    }

    //Returns a customer included in the list of account holders for this employee
    public synchronized AccountHolder getCustAccount(AccountHolder acc) {
        if (this.holders.contains(acc)) {
            int i = holders.indexOf(acc);
            return this.holders.get(i);
        //If the holder does not hold this customer, return null
        } else {
            return null;
        }
    }

    //Returns the name of the employee
    public synchronized String getEmpName(){
        return this.empName;
    }

    //Returns the assigned employee number
    public synchronized String getEmpNum(){
        return this.employeeNo;
    }

    //Returns the size of the holders array
    public synchronized int getSize(){
        return this.holders.size();
    }

    //Given a customer's account, prints the basic details for all bank accounts attached to them
    public synchronized void printAllAccounts(AccountHolder acc) {
        if (this.holders.contains(acc)) {
            System.out.println(acc.getName() + "'s accounts: ");
            for (int i = 0; i < acc.getSize(); i++) {
                System.out.println("Account Type: " + acc.getAccount(i).getType());
                System.out.println("Account Number: " + acc.getAccount(i).getAccountNumber());
                System.out.println();
            }
        //If the account holder is not managed by the employee, show this in a message
        } else {
            System.out.println("Account holder " + acc.getName() + " is not overseen by employee " + empName);
        }
    }

    //Gets the Account attached to the account number and prints the relevant information
    public synchronized void printOneAccount(AccountHolder person, String acc) {
        if (this.holders.contains(person)) {
            Account foundAcc = person.getAccount(acc);
            //If holder does not have an account with the account number, show this in a message
            if (foundAcc == null) {
                System.out.println("Account holder " + person.getName() + " does not hold account " + acc);
                //Otherwise, print the details of the account
            } else {
                foundAcc.printDetails();
            }
            //If the account holder is not managed by the employee, show this in a message
        } else {
            System.out.println("Account holder " + person.getName() + " is not overseen by employee " + empName);
        }
    }

    //Given the ACCOUNT, the employee can put the amount into the specific account
    public synchronized void deposit(Account acc, double amount) throws Exception {
        boolean found = false;
        for (int i = 0; i < holders.size() && !found; i++) {
            if (this.holders.get(i).getAccount(acc.getAccountNumber()) != null) {
                acc.deposit(amount, new CurrentAccount("Employee Account", 1000000000000.00));
                found = true;
            }
        }
        //If the account is not contained by one of the employee's managed holders, show this in a message
        if (!found) {
            System.out.println("Account " + acc.getAccountNumber() + " is not overseen by employee " + empName);
        }
    }

    //Given the account NUMBER, the employee can search for it and then put the amount into it
    public synchronized void deposit(AccountHolder person, String acc, double amount) throws Exception {
        if (this.holders.contains(person)) {
            Account foundAcc = this.getCustAccount(person).getAccount(acc);
            //If the holder does not have the account, show this in a message
            if (foundAcc == null) {
                System.out.println("Account number " + acc + " does not exist for customer " + person.getName());
                //Otherwise, carry out deposit
            } else {
                foundAcc.deposit(amount, new CurrentAccount("Employee Account", 1000000000000.00));
            }
            //If the account is not contained by one of the employee's managed holders, show this in a message
        } else {
            System.out.println("Account holder " + person.getName() + " is not overseen by employee " + empName);
        }
    }

    //Given the ACCOUNT, the employee can pull money out of the amount into the specific account
    public synchronized void withdraw(Account acc, double amount) throws Exception {
        boolean found = false;
        System.out.println("Number of holders for " + empName + " is: " + this.holders.size());
        for (int i = 0; i < holders.size() && !found; i++) {
            System.out.println("Number of holders for " + empName + " is: " + this.holders.size());
            System.out.println(this.holders.get(i).getSize());
            if (this.holders.get(i).getAccount(acc.getAccountNumber()) != null) {
                acc.withdraw(amount,  new CurrentAccount("Employee Account", 1000000000000.00));
                found = true;
            }
        }
        //If the account is not contained by one of the employee's managed holders, show this in a message
        if (!found) {
            System.out.println("Account " + acc.getAccountNumber() + " is not overseen by employee " + empName);
        }
    }

    //Given the account NUMBER, the employee can search for it and then pull money out of the amount into it
    public synchronized void withdraw(AccountHolder person, String acc, double amount) throws Exception {
        if (this.holders.contains(person)) {
            Account foundAcc = this.getCustAccount(person).getAccount(acc);
            //If the account holder does not hold an account with this number, show this in a message
            if (foundAcc == null) {
                System.out.println("Account number " + acc + " does not exist for customer " + person.getName());
                //Otherwise, carry out withdraw
            } else {
                foundAcc.withdraw(amount,  new CurrentAccount("Employee Account", 1000000000000.00));
            }
            //If the account is not contained by one of the employee's managed holders, show this in a message
        } else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Adds an already existing AccountHolder
    public synchronized void addAccountHolder(AccountHolder person) {
        //If holder is already overseen by employee, show this in output
        if (this.holders.contains(person)) {
            System.out.println("Holder " + person.getName() + " is already overseen by employee " + empName);
            //Otherwise, add this person to employee's list of account holders
        } else {
            holders.add(person);
        }
    }

    //Adds an already existing account to an AccountHolder
    //NOTE: it may be possible to reorganise this code so the operations are the same but the layout is neater
    public synchronized void addAccount(AccountHolder person, Account acc) {
        if (this.holders.contains(person)) {
            //Check to ensure it isn't already under the account holder, and if it is, show this in a message
            Account foundAcc = person.getAccount(acc.getAccountNumber());
            if (foundAcc == null) {
                System.out.println("Account " + acc.getAccountNumber() + " from holder " + person.getName() + " added to employee " + this.getEmpName());
                person.addAccount(acc);
            } else {
                System.out.println("This account is already owned by" + person.getName());
            }
            //If account holder is not already overseen by employer, add it to list then add account to this holder
        } else {
            holders.add(person);
            person.addAccount(acc);
        }
    }

    //Opens a new AccountHolder (i.e. for a new customer), adds it to the AccountHolder array and returns
    public synchronized AccountHolder createCustAccount(String name, int age) {
        //The AccountHolder ONLY takes a string and a name - since these are not unique it wouldn't make sense to check if the account already exists
        AccountHolder acc = new AccountHolder(name, age);
        this.holders.add(acc);
        return acc;
    }

    //Deletes an already existing bank account from an Account Holder
    public synchronized void deleteAccount(AccountHolder person, Account acc) {
        //Check to ensure it IS actually under the account holder, and if so, show this in a message
        Account foundAcc = person.getAccount(acc.getAccountNumber());
        if (foundAcc == null) {
            System.out.println("Account " + acc.getAccountNumber() + " is not owned by" + person.getName());
        } else {
            this.getCustAccount(person).deleteAccount(acc);
        }
    }

    //Deletes the entire account for a customer
    //Should also delete EVERY account the user has
    public synchronized boolean deleteCustAccount(AccountHolder acc) {
        //If the person has no accounts, their account can be closed
        if (acc.getSize() == 0) {
            this.holders.remove(acc);
            acc = null;
        }
        //Otherwise, every one of their bank accounts must ALSO be closed
        else {
            //Loop through the account array and delete every one of them
            int size = acc.getSize();
            for (int i = 0; i <= size; i++) {
                acc.deleteAccount(0);
            }
            this.holders.remove(acc);
            acc = null;
        }
        //If deleted, return true
        if (acc == null) {
            return true;
        }
        return false;
    }

    //Updates the interest rate of specific accounts
    public synchronized void changeInterest(Account acc, double interest) {
        //Check if account type is one where an interest rate can be updated
        if (acc instanceof MortgageAcc) {
            ((MortgageAcc) acc).updateInterest(interest);
        } else if (acc instanceof CreditAccount) {
            ((CreditAccount) acc).setAPR(interest);
        } else if (acc instanceof SavingAccount) {
            ((SavingAccount) acc).changeInterest(interest);
        }
        //Otherwise, there is no interest rate to update
        else {
            System.out.println("Account " + acc.getAccountNumber() + " does not have an interest rate to update");
        }
    }

    //Updates the overdraft of a student account
    public synchronized void changeOverdraftLimit(Account acc, double limit) throws Exception {
        //Should only accept student accounts, otherwise show this in message
        if (acc instanceof StudentAccount) {
            ((StudentAccount) acc).setOverdraft(limit);
        }
        else {
            System.out.println("This account does not have an overdraft limit to update");
        }
    }
}