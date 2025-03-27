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
                // Skip if there's an error
            }
        }

        for (String symbol : losers) {
            try {
                double change = stockMarket.getDailyChangePercentage(symbol);
                if (change < -10.0) { // Consider over 10% loss as extreme
                    extremeMovers.put(symbol, change);
                }
            } catch (IOException e) {
                // Skip if there's an error
            }
        }

        // Display findings
        if (extremeMovers.isEmpty()) {
            System.out.println("No unusual market movements detected today.");
        } else {
            System.out.println("The following stocks are showing unusual price movements:");
            for (Map.Entry<String, Double> entry : extremeMovers.entrySet()) {
                try {
                    String name = stockMarket.getCompanyName(entry.getKey());
                    double price = stockMarket.getStockPrice(entry.getKey());
                    System.out.println(name + " (" + entry.getKey() + "): " +
                            String.format("%.2f", entry.getValue()) + "% | $" +
                            String.format("%.2f", price));
                } catch (IOException e) {
                    System.out.println(entry.getKey() + ": " +
                            String.format("%.2f", entry.getValue()) + "%");
                }
            }
        }
    }

    /**
     * Displays the top market gainers
     */
    public void displayTopGainers() throws IOException {
        System.out.println("\n========== Top Market Gainers 🚀 ==========");
        List<String> gainers = stockMarket.getTopGainers();

        if (gainers.isEmpty()) {
            System.out.println("No data available for top gainers.");
            return;
        }

        for (int i = 0; i < gainers.size(); i++) {
            String symbol = gainers.get(i);
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);
                System.out.println((i + 1) + ". " + name + " (" + symbol + "): $" +
                        String.format("%.2f", price) + " (+" +
                        String.format("%.2f", change) + "%)");
            } catch (IOException e) {
                System.out.println((i + 1) + ". " + symbol + ": Error getting data");
            }
        }
    }
}
