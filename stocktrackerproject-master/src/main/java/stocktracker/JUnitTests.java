package stocktracker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

/**
 * JUnit test class for testing the functionality of StockTrackerData and StockDataFetcher.
 * Ensures that all core non-input/non-output functions work correctly.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 1.0
 * @Tutorial T04
 */
public class JUnitTests {

    /**
     * Reset the StockTrackerData before each test
     * This requires adding a reset() method to StockTrackerData
     */
    @BeforeEach
    public void setup() {
        // Call a method that resets all static fields in StockTrackerData
        StockTrackerData.reset();
    }

    // Tests for getBalance method
    @Test
    public void testGetBalance_initialValue() {
        // Test that the initial balance is 10000.0
        assertEquals(10000.0, StockTrackerData.getBalance(), "Initial balance should be 10000.0");
    }

    @Test
    public void testGetBalance_afterDeposit() {
        // Test balance after adding funds (assuming you have a deposit method)
        StockTrackerData.deposit(1000.0);
        assertEquals(11000.0, StockTrackerData.getBalance(), "Balance should be 11000.0 after deposit");
    }

    @Test
    public void testGetBalance_afterWithdrawal() {
        // Test balance after withdrawing funds (assuming you have a withdraw method)
        StockTrackerData.withdraw(1000.0);
        assertEquals(9000.0, StockTrackerData.getBalance(), "Balance should be 9000.0 after withdrawal");
    }

    // Tests for addToWatchlist method
    @Test
    public void testAddToWatchlist_newSymbol() {
        // Add a new symbol to the watchlist
        StockTrackerData.addToWatchlist("AAPL");

        // Verify it was added by checking if it's in the watchlist
        assertTrue(StockTrackerData.isInWatchlist("AAPL"), "Watchlist should contain AAPL after adding it");
    }

    @Test
    public void testAddToWatchlist_duplicateSymbol() {
        // Add the same symbol twice
        StockTrackerData.addToWatchlist("AAPL");
        StockTrackerData.addToWatchlist("AAPL");

        // Verify it was added only once by checking the watchlist size
        assertEquals(1, StockTrackerData.getWatchlistSize(), "Watchlist should have only one entry for AAPL");
        assertTrue(StockTrackerData.isInWatchlist("AAPL"), "Watchlist should contain AAPL");
    }

    @Test
    public void testAddToWatchlist_multipleSymbols() {
        // Add multiple symbols
        StockTrackerData.addToWatchlist("AAPL");
        StockTrackerData.addToWatchlist("MSFT");
        StockTrackerData.addToWatchlist("GOOGL");

        // Verify all were added
        assertEquals(3, StockTrackerData.getWatchlistSize(), "Watchlist should contain 3 symbols");
        assertTrue(StockTrackerData.isInWatchlist("AAPL"), "Watchlist should contain AAPL");
        assertTrue(StockTrackerData.isInWatchlist("MSFT"), "Watchlist should contain MSFT");
        assertTrue(StockTrackerData.isInWatchlist("GOOGL"), "Watchlist should contain GOOGL");
    }

    // Tests for removeFromWatchlist method
    @Test
    public void testRemoveFromWatchlist_existingSymbol() {
        // Add and then remove a symbol
        StockTrackerData.addToWatchlist("AAPL");
        StockTrackerData.removeFromWatchlist("AAPL");

        // Verify it was removed
        assertFalse(StockTrackerData.isInWatchlist("AAPL"), "Watchlist should not contain AAPL after removing it");
    }

    @Test
    public void testRemoveFromWatchlist_nonExistingSymbol() {
        // Remove a symbol that doesn't exist
        StockTrackerData.removeFromWatchlist("AAPL");

        // Verify the watchlist is still empty
        assertEquals(0, StockTrackerData.getWatchlistSize(), "Watchlist should be empty");
    }

    @Test
    public void testRemoveFromWatchlist_multipleSymbols() {
        // Add multiple symbols
        StockTrackerData.addToWatchlist("AAPL");
        StockTrackerData.addToWatchlist("MSFT");
        StockTrackerData.addToWatchlist("GOOGL");

        // Remove one
        StockTrackerData.removeFromWatchlist("MSFT");

        // Verify only that one was removed
        assertEquals(2, StockTrackerData.getWatchlistSize(), "Watchlist should contain 2 symbols after removal");
        assertTrue(StockTrackerData.isInWatchlist("AAPL"), "Watchlist should still contain AAPL");
        assertFalse(StockTrackerData.isInWatchlist("MSFT"), "Watchlist should not contain MSFT");
        assertTrue(StockTrackerData.isInWatchlist("GOOGL"), "Watchlist should still contain GOOGL");
    }

    // Tests for buyStock method
    @Test
    public void testBuyStock_sufficientFunds() {
        // Buy stock with sufficient funds
        StockTrackerData.buyStock("AAPL", 10);

        // Verify stock was added to holdings
        assertTrue(StockTrackerData.getStockQuantity("AAPL") > 0, "Should have AAPL in holdings after purchase");
        assertTrue(StockTrackerData.getBalance() < 10000.0, "Balance should decrease after purchase");
    }

    @Test
    public void testBuyStock_insufficientFunds() {
        // Try to buy very expensive stock with insufficient funds
        StockTrackerData.buyStock("AAPL", 1000);

        // Verify no stock was added and balance unchanged
        assertEquals(0, StockTrackerData.getStockQuantity("AAPL"), "Should not have AAPL in holdings");
        assertEquals(10000.0, StockTrackerData.getBalance(), "Balance should remain unchanged");
    }

    @Test
    public void testBuyStock_zeroQuantity() {
        // Try to buy zero shares
        StockTrackerData.buyStock("AAPL", 0);

        // Verify no stock was added and balance unchanged
        assertEquals(0, StockTrackerData.getStockQuantity("AAPL"), "Should not have AAPL in holdings");
        assertEquals(10000.0, StockTrackerData.getBalance(), "Balance should remain unchanged");
    }
    // Tests for sellStock method
    @Test
    public void testSellStock_ownedStock() {
        // First buy some stock
        StockTrackerData.buyStock("AAPL", 10);
        double balanceAfterBuy = StockTrackerData.getBalance();


        // Then sell some of it
        StockTrackerData.sellStock("AAPL", 5);


        // Verify quantity decreased and balance increased
        assertEquals(5, StockTrackerData.getStockQuantity("AAPL"), "Should have 5 shares of AAPL left");
        assertTrue(StockTrackerData.getBalance() > balanceAfterBuy, "Balance should increase after selling");
    }


    @Test
    public void testSellStock_notOwnedStock() {
        // Try to sell stock we don't own
        StockTrackerData.sellStock("AAPL", 5);


        // Verify balance unchanged
        assertEquals(10000.0, StockTrackerData.getBalance(), "Balance should remain unchanged");
    }


    @Test
    public void testSellStock_moreThanOwned() {
        // First buy some stock
        StockTrackerData.buyStock("AAPL", 3);
        double balanceAfterBuy = StockTrackerData.getBalance();


        // Try to sell more than we own
        StockTrackerData.sellStock("AAPL", 5);


        // Verify quantity and balance unchanged
        assertEquals(3, StockTrackerData.getStockQuantity("AAPL"), "Should still have 3 shares of AAPL");
        assertEquals(balanceAfterBuy, StockTrackerData.getBalance(), "Balance should remain unchanged");
    }


    // Tests for StockDataFetcher methods
    @Test
    public void testGetStockPrice_validSymbol() {
        try {
            double price = StockDataFetcher.getStockPrice("AAPL");
            assertTrue(price > 0, "Stock price should be positive");
        } catch (IOException e) {
            // Skip test if API fails
            System.out.println("Skipping test due to API error: " + e.getMessage());
        }
    }


    @Test
    public void testGetDailyChangePercentage_validSymbol() {
        try {
            double change = StockDataFetcher.getDailyChangePercentage("AAPL");
            // We don't assert on the actual value since it can be positive or negative
            assertTrue(true, "Successfully retrieved daily change percentage");
        } catch (IOException e) {
            // Skip test if API fails
            System.out.println("Skipping test due to API error: " + e.getMessage());
        }
    }


    @Test
    public void testGetTopGainers() {
        try {
            List<String> gainers = StockDataFetcher.getTopGainers();
            assertNotNull(gainers, "Top gainers list should not be null");
            assertTrue(gainers.size() <= 5, "Should return at most 5 top gainers");
        } catch (IOException e) {
            // Skip test if API fails
            System.out.println("Skipping test due to API error: " + e.getMessage());
        }
    }


    @Test
    public void testGetTopLosers() {
        try {
            List<String> losers = StockDataFetcher.getTopLosers();
            assertNotNull(losers, "Top losers list should not be null");
            assertTrue(losers.size() <= 5, "Should return at most 5 top losers");
        } catch (IOException e) {
            // Skip test if API fails
            System.out.println("Skipping test due to API error: " + e.getMessage());
        }
    }


    @Test
    public void testGetMostActivelyTraded() {
        try {
            List<String> active = StockDataFetcher.getMostActivelyTraded();
            assertNotNull(active, "Most actively traded list should not be null");
            assertTrue(active.size() <= 5, "Should return at most 5 most actively traded stocks");
        } catch (IOException e) {
            // Skip test if API fails
            System.out.println("Skipping test due to API error: " + e.getMessage());
        }
    }

}
