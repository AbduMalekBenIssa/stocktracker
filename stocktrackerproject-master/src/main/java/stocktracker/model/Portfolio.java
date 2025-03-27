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

}

