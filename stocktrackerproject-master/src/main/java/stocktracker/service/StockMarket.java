package stocktracker.service;

import java.io.IOException;
import java.util.List;

/**
 * Interface for stock market data services
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */


public interface StockMarket {

    /**
     * Gets the current price of a stock
     *
     * @param symbol The stock symbol
     * @return The current price
     * @throws IOException If there's an error getting the data
     */
    double getStockPrice(String symbol) throws IOException;

    /**
     * Gets the daily change percentage of a stock
     *
     * @param symbol The stock symbol
     * @return The daily change percentage
     * @throws IOException If there's an error getting the data
     */
    double getDailyChangePercentage(String symbol) throws IOException;

    /**
     * Gets the name of a company by its stock symbol
     *
     * @param symbol The stock symbol
     * @return The company name
     * @throws IOException If there's an error getting the data
     */
    String getCompanyName(String symbol) throws IOException;

    /**
     * Gets the top gainers in the market
     *
     * @return A list of top gaining stock symbols
     * @throws IOException If there's an error getting the data
     */
    List<String> getTopGainers() throws IOException;

    /**
     * Gets the top losers in the market
     *
     * @return A list of top losing stock symbols
     * @throws IOException If there's an error getting the data
     */
    List<String> getTopLosers() throws IOException;

    /**
     * Gets the most actively traded stocks
     *      * @return A list of the most actively traded stock symbols
     * @throws IOException If there's an error getting the data
     */
    List<String> getMostActivelyTraded() throws IOException;

    /**
     * Validates if a stock symbol exists
     *
     * @param symbol The stock symbol to validate
     * @return True if the symbol is valid
     * @throws IOException If there's an error validating the symbol
     */
    boolean isValidSymbol(String symbol) throws IOException;

}
