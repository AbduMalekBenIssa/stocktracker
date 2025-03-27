package stocktracker.model;

/**
 * Class representing a user's stock portfolio
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class Portfolio {
    private Map<String, OwnedStock> stocks;

    /**
     * Constructor for the Portfolio class
     */
    public Portfolio() {
        this.stocks = new HashMap<>();
    }
    /**
     * Adds a stock to the portfolio
     *
     * @param stock The stock to add
     */
    public void addStock(OwnedStock stock) {
        if (stocks.containsKey(stock.getSymbol())) {
            OwnedStock existingStock = stocks.get(stock.getSymbol());
            existingStock.addShares(stock.getQuantity(), stock.getPurchasePrice());
        } else {
            stocks.put(stock.getSymbol(), stock);
        }
    }
    /**
     * Checks if a stock is in the portfolio
     *
     * @param symbol The stock symbol to check
     * @return True if the stock is in the portfolio
     */
    public boolean containsStock(String symbol) {
        return stocks.containsKey(symbol);
    }

    /**
     * Gets a stock from the portfolio
     *
     * @param symbol The stock symbol
     * @return The stock, or null if not found
     */
    public OwnedStock getStock(String symbol) {
        return stocks.get(symbol);
    }

    /**
     * Removes a stock from the portfolio
     *
     * @param symbol The stock symbol
     * @return The removed stock, or null if not found
     */
    public OwnedStock removeStock(String symbol) {
        return stocks.remove(symbol);
    }
    
}

