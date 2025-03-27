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

    /**
     * Checks for unusual market movements
     */
    public void checkUnusualMarketMovements() throws IOException {
        System.out.println("\n========== Unusual Market Movements ==========");

        // Get top gainers and losers
        List<String> gainers = stockMarket.getTopGainers();
        List<String> losers = stockMarket.getTopLosers();

        // Check for extreme movements
        Map<String, Double> extremeMovers = new HashMap<>();

        for (String symbol : gainers) {
            try {
                double change = stockMarket.getDailyChangePercentage(symbol);
                if (change > 10.0) { // Consider over 10% daily change as extreme
                    extremeMovers.put(symbol, change);
                }
            } catch (IOException e) {
            }