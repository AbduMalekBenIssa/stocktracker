package stocktracker.model;


import java.util.Objects;


/**
 * Abstract class representing a stock
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public abstract class Stock implements Comparable<Stock> {
    protected String symbol;
    protected String name;
    protected double currentPrice;


    /**
     * Constructor for the Stock class
     *
     * @param symbol       The stock ticker symbol
     * @param name         The company name
     * @param currentPrice The current price of the stock
     */
    public Stock(String symbol, String name, double currentPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    /**
     * Gets the stock symbol
     *
     * @return The stock symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the company name
     *
     * @return The company name
     */
    public String getName() {
        return name;
    }


}