package stocktracker.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import stocktracker.model.OwnedStock;

/**
 * JUnit tests for OwnedStock class
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */


public class OwnedStockTest {

    @Test
    public void testGetTotalValue() {
        // Create a stock with 10 shares at $150 each
        OwnedStock stock = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);

        // Check that the total value is calculated correctly
        assertEquals(1500.0, stock.getTotalValue(), 0.01, "Total value should be price * quantity");
    }

    @Test
    public void testGetProfitLoss() {
        // Create a stock with current price higher than purchase price
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);
        assertEquals(50.0, stock1.getProfitLoss(), 0.01, "Profit should be (current - purchase) * quantity");

        // Create a stock with current price lower than purchase price
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 290.0, 5, 300.0);
        assertEquals(-50.0, stock2.getProfitLoss(), 0.01, "Loss should be (current - purchase) * quantity");
    }

    @Test
    public void testGetProfitLossPercentage() {
        // Create a stock with 10% gain
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 110.0, 10, 100.0);
        assertEquals(10.0, stock1.getProfitLossPercentage(), 0.01, "Profit percentage should be calculated correctly");

        // Create a stock with 10% loss
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 90.0, 5, 100.0);
        assertEquals(-10.0, stock2.getProfitLossPercentage(), 0.01, "Loss percentage should be calculated correctly");
    }

    @Test
    public void testAddShares() {
        // Create a stock with 10 shares at $100 each
        OwnedStock stock = new OwnedStock("AAPL", "Apple Inc.", 110.0, 10, 100.0);

        // Add 5 more shares at $120 each
        stock.addShares(5, 120.0);

        // Check that the quantity and average purchase price were updated correctly
        assertEquals(15, stock.getQuantity(), "Quantity should be increased");
        // Average price calculation: (10*100 + 5*120) / 15 = 106.67
        assertEquals(106.67, stock.getPurchasePrice(), 0.01, "Purchase price should be averaged correctly");
    }

    @Test
    public void testRemoveShares_success() {
        // Create a stock with 10 shares
        OwnedStock stock = new OwnedStock("AAPL", "Apple Inc.", 110.0, 10, 100.0);

        // Remove 4 shares
        boolean result = stock.removeShares(4);

        // Check that shares were removed
        assertTrue(result, "Should return true when shares are successfully removed");
        assertEquals(6, stock.getQuantity(), "Quantity should be decreased");
        assertEquals(100.0, stock.getPurchasePrice(), 0.01, "Purchase price should remain unchanged");
    }

    @Test
    public void testRemoveShares_tooMany() {
        // Create a stock with 10 shares
        OwnedStock stock = new OwnedStock("AAPL", "Apple Inc.", 110.0, 10, 100.0);

        // Try to remove more shares than available
        boolean result = stock.removeShares(15);

        // Check that no shares were removed
        assertFalse(result, "Should return false when trying to remove too many shares");
        assertEquals(10, stock.getQuantity(), "Quantity should remain unchanged");
    }

    @Test
    public void testEquals() {
        // Create two stocks with same symbol but different details
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);
        OwnedStock stock2 = new OwnedStock("AAPL", "Apple Corporation", 155.0, 5, 140.0);

        // Create a stock with different symbol
        OwnedStock stock3 = new OwnedStock("MSFT", "Microsoft Corp.", 300.0, 5, 295.0);

        // Check equality
        assertEquals(stock1, stock2, "Stocks with same symbol should be equal");
        assertNotEquals(stock1, stock3, "Stocks with different symbols should not be equal");
    }



}
