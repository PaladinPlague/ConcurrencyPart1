import java.util.ArrayList;

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

    private ArrayList<AccountHolder> accounts;
    private String employeeNo;
    private String empName;

    //Initializes a very basic employee with information they'd typically have like a name and number
    public BankEmployee(String name, String employeeNumber) {
        this.employeeNo = employeeNumber;
        this.empName = name;
        this.accounts = new ArrayList<>();
    }

    //Returns the account of the customer
    public AccountHolder getCustAccount(AccountHolder acc) {
        if (this.accounts.contains(acc)) {
            int i = accounts.indexOf(acc);
            return this.accounts.get(i);
        } else {
            return null;
        }
    }

    //Returns the name of the employee
    public String getEmpName(){
        return this.empName;
    }

    //Returns the assigned employee number
    public String getEmpNum(){
        return this.employeeNo;
    }

    //Returns the size of the accounts array
    public int getSize(){
        return this.accounts.size();
    }

    //Given a customer's account, prints the basic details for all bank accounts attached to them
    public void printAllAccounts(AccountHolder acc) {
        if (this.accounts.contains(acc)) {
            System.out.println(acc.getName() + "'s Accounts: ");
            for (int i = 0; i < acc.getSize(); i++) {
                System.out.println("Account Type: " + acc.getAccount(i).getType());
                System.out.println("Account Number: " + acc.getAccount(i).getAccountNumber());
                System.out.println();
            }
        } else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Gets the Account attached to the account number and prints the relevant information
    public void printOneAccount(AccountHolder person, String acc) {
        if (this.accounts.contains(person)) {
            Account foundAcc = person.getAccount(acc);
            if (foundAcc == null) {
                System.out.println("This is not the number of an existing account");
            } else {
                System.out.println("Account Type: " + foundAcc.getType());
                foundAcc.printDetails();
            }
        } else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Given the ACCOUNT, the employee can put the amount into the specific account
    public synchronized void deposit(Account acc, double amount) throws Exception {
        //Unsure about the 'acc' parameter here - need to change this I think
        if (this.accounts.contains(acc)) {
            acc.deposit(amount, acc);
        }
        else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Given the account NUMBER, the employee can search for it and then put the amount into it
    public synchronized void deposit(AccountHolder person, String acc, double amount) throws Exception {
        if (this.accounts.contains(person)) {
            Account foundAcc = this.getCustAccount(person).getAccount(acc);
            if (foundAcc == null) {
                System.out.println("This is not the number of an existing account");
            } else {
                System.out.println("yes");
                //Unsure about the 'acc' parameter here - need to change this I think
                foundAcc.deposit(amount, foundAcc);
            }
        }
        else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Given the ACCOUNT, the employee can pull money out of the amount into the specific account
    public synchronized void withdraw(Account acc, double amount) throws Exception {
        //Unsure about the 'acc' parameter here - need to change this I think
        if (this.accounts.contains(acc)) {
            acc.withdraw(amount, acc);
        }
        else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Given the account NUMBER, the employee can search for it and then pull money out of the amount into it
    public synchronized void withdraw(AccountHolder person, String acc, double amount) throws Exception {
        if (this.accounts.contains(person)) {
            Account foundAcc = this.getCustAccount(person).getAccount(acc);
            if (foundAcc == null) {
                System.out.println("This is not the number of an existing account");
            } else {
                //Unsure about the 'acc' parameter here - need to change this I think
                foundAcc.withdraw(amount, foundAcc);
            }
        }
        else {
        System.out.println("This account may not be overseen by this employee");
    }
}

    //Adds an already existing account to an AccountHolder
    public synchronized void addAccount(AccountHolder person, Account acc) {
        if (this.accounts.contains(person)) {
            //Check to ensure it isn't already under the account holder
            Account foundAcc = person.getAccount(acc.getAccountNumber());
            if (foundAcc == null) {
                person.addAccount(acc);
            } else {
                System.out.println("This account is already owned by" + person.getName());
            }
        } else {
            System.out.println("This account may not be overseen by this employee");
        }
    }

    //Opens a new AccountHolder (i.e. for a new customer), adds it to the AccountHolder array and returns
    public synchronized AccountHolder createCustAccount(String name, int age) {
        //The AccountHolder ONLY takes a string and a name - since these are not unique it wouldn't make
        //Sense to check if the account already exists
        AccountHolder acc = new AccountHolder(name, age);
        this.accounts.add(acc);
        return acc;
    }

    //Deletes an already existing bank account from an Account Holder
    public synchronized void deleteAccount(AccountHolder person, Account acc) {
        //Check to ensure it IS actually under the account holder
        Account foundAcc = person.getAccount(acc.getAccountNumber());
        if (foundAcc == null) {
            System.out.println("This account is NOT owned by" + person.getName());
        } else {
            this.getCustAccount(person).deleteAccount(acc);
        }
    }

    //Deletes the entire account for a customer
    //Should also delete EVERY account the user has
    public synchronized boolean deleteCustAccount(AccountHolder acc) {
        //If the person has no accounts, their account can be closed
        if (acc.getSize() == 0) {
            this.accounts.remove(acc);
            acc = null;
        }
        //Otherwise, every one of their bank accounts must ALSO be closed
        else {
            //Loop through the account array and delete every one of them
            int size = acc.getSize();
            for (int i = 0; i <= size; i++) {
                acc.deleteAccount(0);
            }
            this.accounts.remove(acc);
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
        if (acc instanceof MortgageAcc) {
            ((MortgageAcc) acc).updateInterest(interest);
        } else if (acc instanceof CreditAccount) {
            ((CreditAccount) acc).setAPR(interest);
        } else if (acc instanceof SavingAccount) {
            ((SavingAccount) acc).changeInterest(interest);
        }
        //Otherwise, there is no interest rate to update
        else {
            System.out.println("This account does not have an interest rate to update");
        }
    }

    //Updates the overdraft of a student account
    public synchronized void changeOverdraftLimit(Account acc, double limit) throws Exception {
        //Should only accept student accounts
        if (acc instanceof StudentAccount) {
            ((StudentAccount) acc).setOverdraft(limit);
        }
        else {
            System.out.println("This account does not have an overdraft limit to update");
        }
    }
}