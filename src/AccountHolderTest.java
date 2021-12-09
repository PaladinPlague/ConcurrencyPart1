//Reference used on how to carry out JUnit testing in Eclipse: https://www.youtube.com/watch?v=o5pE7L2tVV8
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Account holder testing class
class AccountHolderTest {

    //Variable used for testing all AccountHolder's methods
    AccountHolder test = new AccountHolder("Sonic", 18);
    //Variable usable as a parameter for deposit/withdraw
    Account savingTest = new SavingAccount("1212345", 50);

    //Check the age getter method
    @Test
    void checkGetAge(){
        assertEquals(18,test.getAge());
    }

    //Check the name getter method
    @Test
    void checkGetName(){
        assertEquals("Sonic",test.getName());
    }

    //Check if add account works, we add 2 accounts and test various methods including deposit and withdraw
    @Test
    void addAccountTest() throws Exception {
        //Add accounts to this holder
        test.addAccount(new CurrentAccount("12345",30.0));
        test.addAccount(new StudentAccount("235235",60.0));

        //Check that the account holder currently has 2 accounts, and that getter method involving bank details are correct
        assertEquals(2,test.getSize());
        assertEquals("12345",test.getAccount(0).getAccountNumber());
        assertEquals("235235",test.getAccount(1).getAccountNumber());
        assertEquals("Current Account",test.getAccount(0).getType());
        assertEquals("Student Account",test.getAccount(1).getType());
        assertEquals(30.0,test.getAccount(0).getBalance());
        //Expected value is 110 as we have passed 60 as a parameter and student accounts get 50 extra when constructed
        assertEquals(110.0,test.getAccount(1).getBalance());

        //Deposit and withdraw from accounts held and ensure that values are changed appropriately
        test.getAccount(0).deposit(20.0, savingTest);
        test.getAccount(1).withdraw(15.0, savingTest);
        assertEquals(50.0,test.getAccount(0).getBalance());
        assertEquals(95.0,test.getAccount(1).getBalance());
    }

    //Check if deleteAccount method works with both int & account as type parameter
    @Test
    void deleteAccountTest() throws Exception {
        //Testing for int parameter
        test.addAccount(new CurrentAccount("12345", 30.0));
        assertEquals(true, test.deleteAccount(0));
        assertEquals(0, test.getSize());
        //Testing for Account type
        Account current = new CurrentAccount("12345", 30.0);
        test.addAccount(current);
        assertEquals(true, test.deleteAccount(current));
        assertEquals(0, test.getSize());
    }
}