import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountHolderTest {

/*
ACCOUNT HOLDER TESTING CLASS
Made by Suleman Akhter
*/

    //the test variable is used for testing all of AccountHolder methods, savingTest is just as a parameter for deposit/withdraw
    AccountHolder test = new AccountHolder("Sonic", 18);
    Account savingTest = new SavingAccount("1212345", 50);


    //Checks the getters methods
    @Test
    void checkGetAge(){
        assertEquals(18,test.getAge());
    }
    @Test
    void checkGetName(){
        assertEquals("Sonic",test.getName());
    }
    // This checks if add account works, we add 2 accounts and test various methods including deposit/withdraw
    @Test
    void addAccountTest() throws Exception {
        test.addAccount(new CurrentAccount("12345",30.0));
        test.addAccount(new StudentAccount("235235",60.0));
        assertEquals(2,test.getSize());
        assertEquals("Current Account",test.getAccount(0).getType());
        assertEquals(30.0,test.getAccount(0).getBalance());
        test.getAccount(0).deposit(20.0,savingTest);
    }

    // This checks if delete account works with both int & account as type parameter
    @Test
    void deleteAccountTest() throws Exception {
        //Testing for int parameter
        test.addAccount(new CurrentAccount("12345", 30.0));
        assertEquals(true, test.deleteAccount(0));
        //Testing for Account type
        Account current = new CurrentAccount("12345", 30.0);
        test.addAccount(current);
        assertEquals(true, test.deleteAccount(current));

    }
}
