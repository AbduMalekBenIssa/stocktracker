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
