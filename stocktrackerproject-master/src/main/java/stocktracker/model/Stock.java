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
    protected double change;
    protected double changePercent;
    protected double marketCap;
    protected double peRatio;
    protected double dividendYield;
    protected int volume;


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
        this.change = 0;
        this.changePercent = 0;
        this.marketCap = 0;
        this.peRatio = 0;
        this.dividendYield = 0;
        this.volume = 0;
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
     * Gets the current price (alias for getCurrentPrice)
     *
     * @return The current price
     */
    public double getPrice() {
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
     * Gets the daily price change
     *
     * @return The price change
     */
    public double getChange() {
        return change;
    }

    /**
     * Sets the daily price change
     *
     * @param change The price change
     */
    public void setChange(double change) {
        this.change = change;
    }

    /**
     * Gets the daily price change percentage
     *
     * @return The price change percentage
     */
    public double getChangePercent() {
        return changePercent;
    }

    /**
     * Sets the daily price change percentage
     *
     * @param changePercent The price change percentage
     */
    public void setChangePercent(double changePercent) {
        this.changePercent = changePercent;
    }

    /**
     * Gets the market capitalization
     *
     * @return The market cap
     */
    public double getMarketCap() {
        return marketCap;
    }

    /**
     * Sets the market capitalization
     *
     * @param marketCap The market cap
     */
    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    /**
     * Gets the price to earnings ratio
     *
     * @return The P/E ratio
     */
    public double getPeRatio() {
        return peRatio;
    }

    /**
     * Sets the price to earnings ratio
     *
     * @param peRatio The P/E ratio
     */
    public void setPeRatio(double peRatio) {
        this.peRatio = peRatio;
    }

    /**
     * Gets the dividend yield
     *
     * @return The dividend yield
     */
    public double getDividendYield() {
        return dividendYield;
    }

    /**
     * Sets the dividend yield
     *
     * @param dividendYield The dividend yield
     */
    public void setDividendYield(double dividendYield) {
        this.dividendYield = dividendYield;
    }

    /**
     * Gets the trading volume
     *
     * @return The volume
     */
    public int getVolume() {
        return volume;
    }

    /**
     * Sets the trading volume
     *
     * @param volume The volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
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