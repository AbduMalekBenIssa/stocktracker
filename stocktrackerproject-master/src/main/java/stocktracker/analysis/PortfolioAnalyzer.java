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


}
