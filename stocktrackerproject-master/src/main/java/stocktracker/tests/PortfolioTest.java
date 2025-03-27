package stocktracker.tests;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import stocktracker.model.OwnedStock;
import stocktracker.model.Portfolio;


/**
 * JUnit tests for Portfolio class
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class PortfolioTest {
    private Portfolio portfolio;

    @BeforeEach
    public void setUp() {
        portfolio = new Portfolio();
    }

    @Test
    public void testAddStock_newStock() {
        // Add a new stock to the portfolio
        OwnedStock stock = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);
        portfolio.addStock(stock);

        // Check that the stock was added
        assertTrue(portfolio.containsStock("AAPL"), "Portfolio should contain the stock");
        assertEquals(1, portfolio.getAllStocks().size(), "Portfolio should have one stock");
        assertEquals(stock, portfolio.getStock("AAPL"), "Retrieved stock should match added stock");
    }

    @Test
    public void testAddStock_existingStock() {
        // Add a stock to the portfolio
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);
        portfolio.addStock(stock1);

        // Add the same stock again with different quantity and price
        OwnedStock stock2 = new OwnedStock("AAPL", "Apple Inc.", 155.0, 5, 155.0);
        portfolio.addStock(stock2);

        // Check that the quantities and prices were combined correctly
        OwnedStock combinedStock = portfolio.getStock("AAPL");
        assertEquals(15, combinedStock.getQuantity(), "Quantity should be combined");
        // Average price calculation: (10*145 + 5*155) / 15 = 148.33...
        assertEquals(148.33, combinedStock.getPurchasePrice(), 0.01, "Purchase price should be averaged");
    }

}