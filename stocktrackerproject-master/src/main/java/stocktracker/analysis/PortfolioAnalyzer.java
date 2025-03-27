package stocktracker.analysis;


import stocktracker.model.*;
import stocktracker.service.StockMarket;


import java.io.IOException;
import java.util.*;


/**
 * Class for analyzing portfolio data and providing advanced insights
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class PortfolioAnalyzer {
    private User user;
    private StockMarket stockMarket;

    /**
     * Constructor for the PortfolioAnalyzer class
     *
     * @param user The user whose portfolio will be analyzed
     * @param stockMarket The stock market service for fetching data
     */
    public PortfolioAnalyzer(User user, StockMarket stockMarket) {
        this.user = user;
        this.stockMarket = stockMarket;
    }

    /**
     * Displays the total portfolio value with detailed breakdown
     */
    public void displayTotalPortfolioValue() {
        System.out.println("\n========== Total Portfolio Value ==========");
        double portfolioValue = user.getPortfolio().getTotalValue();
        double cashBalance = user.getBalance();
        double totalValue = portfolioValue + cashBalance;

        System.out.println("Stock Holdings: $" + String.format("%.2f", portfolioValue) +
                " (" + String.format("%.2f", (portfolioValue/totalValue)*100) + "%)");
        System.out.println("Cash Balance: $" + String.format("%.2f", cashBalance) +
                " (" + String.format("%.2f", (cashBalance/totalValue)*100) + "%)");
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalValue));

        double profitLoss = user.getPortfolio().getTotalProfitLoss();
        System.out.println("Total Profit/Loss: $" + String.format("%.2f", profitLoss));

        // Calculate percent return if we have profit/loss
        if (portfolioValue > 0) {
            double percentReturn = (profitLoss / (portfolioValue - profitLoss)) * 100;
            System.out.println("Return on Investment: " + String.format("%.2f", percentReturn) + "%");
        }
    }

    /**
     * Shows the top most valuable stocks in the portfolio
     *
     * @param count The number of stocks to display
     */
    public void displayTopValuableStocks(int count) {
        System.out.println("\n========== Top " + count + " Most Valuable Stocks ==========");
        List<OwnedStock> allStocks = user.getPortfolio().getAllStocks();

        if (allStocks.isEmpty()) {
            System.out.println("You don't own any stocks yet.");
            return;
        }

        // Sort by total value (highest first)
        allStocks.sort((s1, s2) -> Double.compare(s2.getTotalValue(), s1.getTotalValue()));

        int displayCount = Math.min(count, allStocks.size());
        for (int i = 0; i < displayCount; i++) {
            OwnedStock stock = allStocks.get(i);
            System.out.println((i + 1) + ". " + stock.getName() + " (" + stock.getSymbol() + ")" +
                    " - $" + String.format("%.2f", stock.getTotalValue()) +
                    " (" + stock.getQuantity() + " shares)");
        }
    }

    /**
     * Shows the best performing stocks (highest profit)
     *
     * @param count The number of stocks to display
     */
    public void displayBestPerformingStocks(int count) {
        System.out.println("\n========== Best Performing Stocks ==========");
        List<OwnedStock> allStocks = user.getPortfolio().getAllStocks();

        if (allStocks.isEmpty()) {
            System.out.println("You don't own any stocks yet.");
            return;
        }

        // Sort by profit/loss percentage (highest first)
        allStocks.sort((s1, s2) -> Double.compare(s2.getProfitLossPercentage(), s1.getProfitLossPercentage()));

        int displayCount = 0;
        for (int i = 0; i < allStocks.size() && displayCount < count; i++) {
            OwnedStock stock = allStocks.get(i);
            if (stock.getProfitLoss() <= 0) {
                continue; // Skip stocks with no profit
            }
            displayCount++;
            System.out.println(displayCount + ". " + stock.getName() + " (" + stock.getSymbol() + ")" +
                    " - Gain: $" + String.format("%.2f", stock.getProfitLoss()) +
                    " (" + String.format("%.2f", stock.getProfitLossPercentage()) + "%)");
        }

        if (displayCount == 0) {
            System.out.println("No stocks showing profit in your portfolio.");
        }
    }

    /**
     * Shows the worst performing stocks (biggest loss)
     *
     * @param count The number of stocks to display
     */
    public void displayWorstPerformingStocks(int count) {
        System.out.println("\n========== Worst Performing Stocks ==========");
        List<OwnedStock> allStocks = user.getPortfolio().getAllStocks();

        if (allStocks.isEmpty()) {
            System.out.println("You don't own any stocks yet.");
            return;
        }

        // Sort by profit/loss percentage (lowest first)
        allStocks.sort((s1, s2) -> Double.compare(s1.getProfitLossPercentage(), s2.getProfitLossPercentage()));

        int displayCount = 0;
        for (int i = 0; i < allStocks.size() && displayCount < count; i++) {
            OwnedStock stock = allStocks.get(i);
            if (stock.getProfitLoss() >= 0) {
                continue; // Skip stocks with no loss
            }
            displayCount++;
            System.out.println(displayCount + ". " + stock.getName() + " (" + stock.getSymbol() + ")" +
                    " - Loss: $" + String.format("%.2f", stock.getProfitLoss()) +
                    " (" + String.format("%.2f", stock.getProfitLossPercentage()) + "%)");
        }

        if (displayCount == 0) {
            System.out.println("No stocks showing loss in your portfolio.");
        }
    }



}
