package stocktracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * Class representing a stock in the user's watchlist
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */

public class WatchlistStock extends Stock {

    private LocalDateTime lastChecked;
    private double changePercentage;


    /**
     * Constructor for the WatchlistStock class
     *
     * @param symbol The stock ticker symbol
     * @param name The company name
     * @param currentPrice The current price of the stock
     * @param changePercentage The daily change percentage
     */
    public WatchlistStock(String symbol, String name, double currentPrice, double changePercentage) {
        super(symbol, name, currentPrice);
        this.lastChecked = LocalDateTime.now();
        this.changePercentage = changePercentage;
    }

    /**
     * Gets the last time the stock was checked
     *
     * @return The last checked time
     */
    public LocalDateTime getLastChecked() {
        return lastChecked;
    }

    /**
     * Gets the daily change percentage
     *
     * @return The change percentage
     */
    public double getChangePercentage() {
        return changePercentage;
    }

    /**
     * Updates the stock information
     *
     * @param price The new price
     * @param changePercentage The new change percentage
     */
    public void update(double price, double changePercentage) {
        this.currentPrice = price;
        this.changePercentage = changePercentage;
        this.lastChecked = LocalDateTime.now();
    }




}
