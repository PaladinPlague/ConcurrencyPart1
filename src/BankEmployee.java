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

    private String employeeNo;
    private String empName;

    //Initializes a very basic employee with information they'd typically have like a name and number
    public BankEmployee(String name, String employeeNumber) {
        this.employeeNo = employeeNumber;
        this.empName = name;
    }

    //Given a customer's account, prints the basic details for all bank accounts attached to them
    public void printAllAccounts(AccountHolder acc) {
        System.out.println(acc.getName() + "'s Accounts: ");
        for (int i = 0; i < acc.getSize(); i++) {
            System.out.println("Account Type: " + acc.getAccount(i).getType());
            System.out.println("Account Number: " + acc.getAccount(i).getAccountNumber());
            System.out.println();
        }
    }

    //Searches a customer's account for the account number and returns the match if present
    public Account getAccount(AccountHolder person, String acc) {
        for (int i = 0; i < person.getSize(); i++) {
            if (acc.equals(person.getAccount(i).getAccountNumber())) {
                return person.getAccount(i);
            }
        }
        //If there was no match, returns null
        return null;
    }

    //Gets the Account attached to the account number and prints the relevant information
    public void printOneAccount(AccountHolder person, String acc) {
        Account foundAcc = getAccount(person, acc);
        if (foundAcc == null) {
            System.out.println("This is not the number of an existing account");
        } else {
            System.out.println("Account Type: " + foundAcc.getType());
            foundAcc.printDetails();
        }
    }

    //Given the ACCOUNT, the employee can put the amount into the specific account
    public synchronized void deposit(Account acc, double amount) throws Exception {
        //Unsure about the 'acc' parameter here - need to change this I think
        acc.deposit(amount, acc);
    }

    //Given the account NUMBER, the employee can search for it and then put the amount into it
    public synchronized void deposit(AccountHolder person, String acc, double amount) throws Exception {
        Account foundAcc = getAccount(person, acc);
        if (foundAcc == null) {
            System.out.println("This is not the number of an existing account");
        } else {
            //Unsure about the 'acc' parameter here - need to change this I think
            foundAcc.deposit(amount, foundAcc);
        }
    }

    //Given the ACCOUNT, the employee can pull money out of the amount into the specific account
    public synchronized void withdraw(Account acc, double amount) throws Exception {
        //Unsure about the 'acc' parameter here - need to change this I think
        acc.withdraw(amount, acc);
    }

    //Given the account NUMBER, the employee can search for it and then pull money out of the amount into it
    public synchronized void withdraw(AccountHolder person, String acc, double amount) throws Exception {
        Account foundAcc = getAccount(person, acc);
        if (foundAcc == null) {
            System.out.println("This is not the number of an existing account");
        } else {
            //Unsure about the 'acc' parameter here - need to change this I think
            foundAcc.withdraw(amount, foundAcc);
        }
    }

    //Adds an already existing account to an AccountHolder
    public synchronized void addAccount(AccountHolder person, Account acc) {
        //Check to ensure it isn't already under the account holder
        Account foundAcc = getAccount(person, acc.getAccountNumber());
        if (foundAcc == null) {
            person.addAccount(acc);
        } else {
            System.out.println("This account is already owned by" + person.getName());
        }
    }

    //Opens a new AccountHolder (i.e. for a new customer)
    public synchronized AccountHolder createCustAccount(String name, int age) {
        return new AccountHolder(name, age);
    }

    //Deletes an already existing bank account from an Account Holder
    public synchronized void deleteAccount(AccountHolder person, Account acc) {
        //Check to ensure it IS actually under the account holder
        Account foundAcc = getAccount(person, acc.getAccountNumber());
        if (foundAcc == null) {
            System.out.println("This account is NOT owned by" + person.getName());
        } else {
            person.deleteAccount(acc);
        }
    }

    //Deletes the entire account for a customer
    //Should also delete EVERY account the user has
    public synchronized boolean deleteCustAccount(AccountHolder acc) {
        //If the person has no accounts, their account can be closed
        if (acc.getSize() == 0) {
            acc = null;
        }
        //Otherwise, every one of their bank accounts must ALSO be closed
        else {
            //Loop through the account array and delete every one of them
            int size = acc.getSize();
            for (int i = 0; i <= size; i++) {
                acc.deleteAccount(0);
            }
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