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

    /**
     * Gets the current price
     *
     * @return The current price
     */
    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Updates the current price
     *
     * @param price The new price
     */
    public void updatePrice(double price) {
        this.currentPrice = price;
    }

    /**
     * Compares stocks based on their symbols
     */
    @Override
    public int compareTo(Stock other) {
        return this.symbol.compareTo(other.symbol);
    }

    /**
     * Checks if two stocks are equal based on their symbols
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass().getSuperclass() != obj.getClass().getSuperclass()) return false;
        Stock stock = (Stock) obj;
        return Objects.equals(symbol, stock.symbol);
    }

    /**
     * Generates a hash code for the stock based on its symbol
     */
    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }


    /**
     * Returns a string representation of the stock
     */
    @Override
    public String toString() {
        return "Stock [Symbol=" + symbol + ", Name=" + name + ", Price=$" + String.format("%.2f", currentPrice) + "]";
    }







}