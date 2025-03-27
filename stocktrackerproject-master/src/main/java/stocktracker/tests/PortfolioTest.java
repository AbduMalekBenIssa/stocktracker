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
    @Test
    public void testAddStock_multipleStocks() {
        // Add multiple different stocks
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 300.0, 5, 295.0);
        OwnedStock stock3 = new OwnedStock("GOOGL", "Alphabet Inc.", 2500.0, 1, 2450.0);

        portfolio.addStock(stock1);
        portfolio.addStock(stock2);
        portfolio.addStock(stock3);

        // Check that all stocks were added
        assertEquals(3, portfolio.getAllStocks().size(), "Portfolio should have three stocks");
        assertTrue(portfolio.containsStock("AAPL"), "Portfolio should contain AAPL");
        assertTrue(portfolio.containsStock("MSFT"), "Portfolio should contain MSFT");
        assertTrue(portfolio.containsStock("GOOGL"), "Portfolio should contain GOOGL");
    }
    @Test
    public void testGetTotalValue() {
        // Add stocks with known values
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);  // Value: 1500
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 300.0, 5, 295.0);  // Value: 1500

        portfolio.addStock(stock1);
        portfolio.addStock(stock2);

        // Check total value
        assertEquals(3000.0, portfolio.getTotalValue(), 0.01, "Total value should be sum of all stock values");
    }
    @Test
    public void testGetTotalProfitLoss() {
        // Add stocks with known profit/loss
        // Stock 1: Current 150, Purchase 145, Quantity 10 -> P/L = (150-145)*10 = 50
        OwnedStock stock1 = new OwnedStock("AAPL", "Apple Inc.", 150.0, 10, 145.0);

        // Stock 2: Current 290, Purchase 300, Quantity 5 -> P/L = (290-300)*5 = -50
        OwnedStock stock2 = new OwnedStock("MSFT", "Microsoft Corp.", 290.0, 5, 300.0);

        portfolio.addStock(stock1);
        portfolio.addStock(stock2);

        // Check total profit/loss
        assertEquals(0.0, portfolio.getTotalProfitLoss(), 0.01, "Total P/L should be sum of all stock P/L");
    }



}