package stocktracker.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import stocktracker.model.*;
import java.util.List;


/**
 * JUnit tests for User class
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class UserTest {
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Test User", 10000.0);
    }

    @Test
    public void testGetName() {
        assertEquals("Test User", user.getName(), "Name should match constructor value");
    }

    @Test
    public void testGetBalance() {
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should match constructor value");
    }

    @Test
    public void testDeposit_validAmount() {
        boolean result = user.deposit(500.0);

        assertTrue(result, "Deposit should return true for valid amount");
        assertEquals(10500.0, user.getBalance(), 0.01, "Balance should increase by deposit amount");
    }
    @Test
    public void testDeposit_invalidAmount() {
        boolean result = user.deposit(-100.0);

        assertFalse(result, "Deposit should return false for negative amount");
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should remain unchanged");
    }
    @Test
    public void testWithdraw_validAmount() {
        boolean result = user.withdraw(500.0);

        assertTrue(result, "Withdraw should return true for valid amount");
        assertEquals(9500.0, user.getBalance(), 0.01, "Balance should decrease by withdrawal amount");
    }

    @Test
    public void testWithdraw_tooMuch() {
        boolean result = user.withdraw(15000.0);

        assertFalse(result, "Withdraw should return false when amount exceeds balance");
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should remain unchanged");
    }
    @Test
    public void testWithdraw_invalidAmount() {
        boolean result = user.withdraw(-100.0);

        assertFalse(result, "Withdraw should return false for negative amount");
        assertEquals(10000.0, user.getBalance(), 0.01, "Balance should remain unchanged");
    }

    @Test
    public void testGetTotalValue_emptyPortfolio() {
        // With an empty portfolio, total value should equal balance
        assertEquals(10000.0, user.getTotalValue(), 0.01, "Total value should equal balance with empty portfolio");
    }

    @Test
    public void testGetTotalValue_withStocks() {
        // Add some stocks to the portfolio
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);  // Value: 1500
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 300.0, 5, 295.0);  // Value: 1500

        user.getPortfolio().addStock(stock1);
        user.getPortfolio().addStock(stock2);

        // Total value should be balance + portfolio value
        assertEquals(13000.0, user.getTotalValue(), 0.01, "Total value should equal balance + portfolio value");
    }

    @Test
    public void testAddTransaction() {
        // Add a transaction
        Transaction transaction = new BuyTransaction("AAPL", 10, 150.0);
        user.addTransaction(transaction);

        // Check that the transaction was added
        List<Transaction> transactions = user.getTransactions();
        assertEquals(1, transactions.size(), "Should have one transaction");
        assertEquals(transaction, transactions.get(0), "Transaction should match the added one");
    }
    @Test
    public void testGetRecentTransactions_emptyList() {
        List<Transaction> recentTransactions = user.getRecentTransactions(5);

        assertTrue(recentTransactions.isEmpty(), "Recent transactions should be empty");
    }

    @Test
    public void testGetRecentTransactions_fewerThanRequested() {
        // Add 3 transactions
        user.addTransaction(new BuyTransaction("AAPL", 10, 150.0));
        user.addTransaction(new BuyTransaction("MSFT", 5, 300.0));
        user.addTransaction(new SellTransaction("AAPL", 5, 160.0, 150.0));

        // Get the 5 most recent transactions
        List<Transaction> recentTransactions = user.getRecentTransactions(5);

        assertEquals(3, recentTransactions.size(), "Should return all 3 transactions");
    }

    @Test
    public void testGetRecentTransactions_moreThanRequested() {
        // Add 5 transactions
        user.addTransaction(new BuyTransaction("AAPL", 10, 150.0));
        user.addTransaction(new BuyTransaction("MSFT", 5, 300.0));
        user.addTransaction(new SellTransaction("AAPL", 5, 160.0, 150.0));
        user.addTransaction(new BuyTransaction("GOOGL", 1, 2500.0));
        user.addTransaction(new SellTransaction("MSFT", 2, 310.0, 300.0));

        // Get the 3 most recent transactions
        List<Transaction> recentTransactions = user.getRecentTransactions(3);

        assertEquals(3, recentTransactions.size(), "Should return only 3 transactions");

        // Check that they're the most recent ones
        assertEquals("GOOGL", recentTransactions.get(0).getSymbol(), "Third most recent transaction");
        assertEquals("MSFT", recentTransactions.get(2).getSymbol(), "Most recent transaction");
    }
}


}
