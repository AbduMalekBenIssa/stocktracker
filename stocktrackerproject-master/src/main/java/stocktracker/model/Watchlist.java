package stocktracker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Class representing a user's stock watchlist
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */


public class Watchlist {

    private Map<String, WatchlistStock> stocks;


    /**
     * Constructor for the Watchlist class
     */
    public Watchlist() {
        this.stocks = new HashMap<>();
    }

    /**
     * Adds a stock to the watchlist
     *
     * @param stock The stock to add
     */
    public void addStock(WatchlistStock stock) {
        stocks.put(stock.getSymbol(), stock);
    }

    /**
     * Removes a stock from the watchlist
     *
     * @param symbol The stock symbol
     * @return The removed stock, or null if not found
     */
    public WatchlistStock removeStock(String symbol) {
        return stocks.remove(symbol);
    }

    /**
     * Checks if a stock is in the watchlist
     *
     * @param symbol The stock symbol
     * @return True if the stock is in the watchlist
     */
    public boolean containsStock(String symbol) {
        return stocks.containsKey(symbol);
    }

    /**
     * Gets a stock from the watchlist
     *
     * @param symbol The stock symbol
     * @return The stock, or null if not found
     */
    public WatchlistStock getStock(String symbol) {
        return stocks.get(symbol);
    }

    /**
     * Gets all stocks in the watchlist
     *
     * @return A list of all watchlist stocks
     */
    public List<WatchlistStock> getAllStocks() {
        List<WatchlistStock> stockList = new ArrayList<>(stocks.values());
        Collections.sort(stockList);
        return stockList;
    }

    /**
     * Gets the size of the watchlist
     *
     * @return The number of stocks in the watchlist
     */
    public int size() {
        return stocks.size();
    }

    /**
     * Returns a string representation of the watchlist
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Watchlist (").append(stocks.size()).append(" stocks):\n");

        List<WatchlistStock> sortedStocks = getAllStocks();
        for (WatchlistStock stock : sortedStocks) {
            sb.append("  ").append(stock).append("\n");
        }

        return sb.toString();
    }

}
