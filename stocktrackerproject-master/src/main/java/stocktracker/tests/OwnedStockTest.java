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

}
