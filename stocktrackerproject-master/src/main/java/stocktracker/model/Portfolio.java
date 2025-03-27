package stocktracker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

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

    /**
     * Gets all stocks in the portfolio
     *
     * @return A list of all owned stocks
     */
    public List<OwnedStock> getAllStocks() {
        List<OwnedStock> stockList = new ArrayList<>(stocks.values());
        Collections.sort(stockList);
        return stockList;
    }
    /**
     * Gets the total value of the portfolio
     *
     * @return The total value
     */
    public double getTotalValue() {
        double total = 0;
        for (OwnedStock stock : stocks.values()) {
            total += stock.getTotalValue();
        }
        return total;
    }
    /**
     * Gets the total profit/loss of the portfolio
     *
     * @return The total profit/loss
     */
    public double getTotalProfitLoss() {
        double total = 0;
        for (OwnedStock stock : stocks.values()) {
            total += stock.getProfitLoss();
        }
        return total;
    }

    /**
     * Returns a string representation of the portfolio
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Portfolio Summary:\n");
        sb.append("Total Value: $").append(String.format("%.2f", getTotalValue())).append("\n");
        sb.append("Total P/L: $").append(String.format("%.2f", getTotalProfitLoss())).append("\n");
        sb.append("Holdings:\n");

        List<OwnedStock> sortedStocks = getAllStocks();
        for (OwnedStock stock : sortedStocks) {
            sb.append("  ").append(stock).append("\n");
        }

        return sb.toString();
    }





}

