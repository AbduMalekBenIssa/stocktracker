package stocktracker.analysis;


import stocktracker.service.StockMarket;


import java.io.IOException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


/**
 * Class for analyzing market data and providing advanced insights
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class MarketAnalyzer {
    private StockMarket stockMarket;

    /**
     * Constructor for the MarketAnalyzer class
     *
     * @param stockMarket The stock market service for fetching data
     */
    public MarketAnalyzer(StockMarket stockMarket) {
        this.stockMarket = stockMarket;
    }
