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

}
