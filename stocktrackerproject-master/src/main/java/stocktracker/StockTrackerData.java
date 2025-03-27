package stocktracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

/**
 * this class handles stock tracking operations, including managing watchlists,
 * portfolio holdings, transactions, and market insights. tt also provides
 * functionalities to interact with stock data using external APIs.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 1.0
 * @Tutorial T04
 */

public class StockTrackerData { // defines the main class for stock tracking data

    // Data storage for the stock tracking system
    private static final HashSet<String> watchlist = new HashSet<>(); // stores stock symbols in the watchlist
    private static final HashMap<String, Integer> stockHoldings = new HashMap<>(); // stores quantity of stocks owned
    private static final HashMap<String, Double> stockPurchasePrice = new HashMap<>(); // stores purchase price per stock
    private static final ArrayList<String> transactionHistory = new ArrayList<>(); // stores transaction history
    private static double balance = 10000.0; // user account balance starting with $10000

    /**
     * returns the current balance of the users account
     *
     * @return the current balance as a double
     */
    public static double getBalance() {
        return balance; // returns the current balance
    }

    /**
     * adds a stock symbol to the watchlist
     *
     * @param symbol the stock symbol to add
     */
    public static void addToWatchlist(String symbol) {
        if (watchlist.add(symbol)) { // attempts to add the symbol to the watchlist
            System.out.println(symbol + " added to watchlist."); // prints success message
        } else {
            System.out.println(symbol + " is already in your watchlist."); // prints if symbol already exists
        }
    }

    /**
     * removes a stock symbol from the watchlist
     *
     * @param symbol the stock symbol to remove
     */
    public static void removeFromWatchlist(String symbol) {
        if (watchlist.remove(symbol)) { // attempts to remove the symbol from the watchlist
            System.out.println(symbol + " removed from watchlist."); // prints success message
        } else {
            System.out.println(symbol + " not in watchlist."); // prints if symbol is not in the watchlist
        }
    }

    /**
     * displays the stocks in the watchlist along with their current prices
     */
    public static void viewWatchlist() {
        if (watchlist.isEmpty()) { // checks if the watchlist is empty
            System.out.println("Your watchlist is empty."); // prints message if watchlist is empty
            return; // exits the method
        }
        System.out.println("\n==== Your Watchlist ===="); // prints header for watchlist
        for (String symbol : watchlist) { // iterates through each symbol in the watchlist
            try {
                double price = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                System.out.println(symbol + " | Current Price: $" + price); // prints symbol and its price
            } catch (IOException e) { // catches IO exceptions
                System.out.println("Error fetching stock data for " + symbol); // prints error message
            }
        }
    }

    /**
     * buys a specified quantity of a stock
     *
     * @param symbol   the stock symbol to buy
     * @param quantity the quantity of shares to buy
     */
    public static void buyStock(String symbol, int quantity) {
        try {
            double price = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
            double totalCost = price * quantity; // calculates the total cost of the purchase

            if (totalCost > balance) { // checks if the user has enough funds
                System.out.println("Not enough funds. Available balance: $" + balance); // prints insufficient funds message
                return; // exits the method
            }

            stockHoldings.put(symbol, stockHoldings.getOrDefault(symbol, 0) + quantity); // updates stock holdings
            stockPurchasePrice.put(symbol, price); // updates the purchase price
            balance -= totalCost; // deducts the total cost from the balance
            transactionHistory.add("Bought " + quantity + " shares of " + symbol + " at $" + price); // adds transaction to history
            System.out.println("Purchase successful! Remaining balance: $" + balance); // prints success message
        } catch (IOException e) { // catches IO exceptions
            System.out.println("Error fetching stock data: " + e.getMessage()); // prints error message
        }
    }

    /**
     * sells a specified quantity of a stock
     *
     * @param symbol   the stock symbol to sell
     * @param quantity the quantity of shares to sell
     */
    public static void sellStock(String symbol, int quantity) {
        if (!stockHoldings.containsKey(symbol) || stockHoldings.get(symbol) < quantity) { // checks if the user has enough shares
            System.out.println("Not enough shares to sell."); // prints insufficient shares message
            return; // exits the method
        }
        try {
            double price = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
            double totalValue = price * quantity; // calculates the total value of the sale

            stockHoldings.put(symbol, stockHoldings.get(symbol) - quantity); // updates stock holdings
            if (stockHoldings.get(symbol) == 0) { // checks if the user has no shares left
                stockHoldings.remove(symbol); // removes the symbol from holdings
                stockPurchasePrice.remove(symbol); // removes the purchase price
            }

            balance += totalValue; // adds the sale value to the balance
            transactionHistory.add("Sold " + quantity + " shares of " + symbol + " at $" + price); // adds transaction to history
            System.out.println("Sale successful! New balance: $" + balance); // prints success message
        } catch (IOException e) { // catches IO exceptions
            System.out.println("Error fetching stock data: " + e.getMessage()); // prints error message
        }
    }

    /**
     * displays the users stock holdings including current prices and total values
     */
    public static void viewPortfolio() {
        if (stockHoldings.isEmpty()) { // checks if the portfolio is empty
            System.out.println("Portfolio is empty."); // prints message if portfolio is empty
            return; // exits the method
        }
        System.out.println("\n==== Portfolio ===="); // prints header for portfolio
        double totalPortfolioValue = 0.0; // initializes total portfolio value

        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double currentPrice = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                int quantity = stockHoldings.get(symbol); // gets the quantity of shares
                double totalValue = currentPrice * quantity; // calculates the total value of the stock
                totalPortfolioValue += totalValue; // adds to the total portfolio value

                System.out.println(symbol + " | Shares: " + quantity + " | Current Price: $" + currentPrice + " | Total Value: $" + totalValue); // prints stock details
            } catch (IOException e) { // catches IO exceptions
                System.out.println("Error fetching stock data for " + symbol); // prints error message
            }
        }
        System.out.println("Total Portfolio Value: $" + totalPortfolioValue); // prints total portfolio value
        System.out.println("Available Balance: $" + balance); // prints available balance
    }

    /**
     * displays the users transaction history
     */
    public static void viewTransactionHistory() {
        System.out.println("\n==== Transaction History ===="); // prints header for transaction history
        for (String record : transactionHistory) { // iterates through each transaction
            System.out.println(record); // prints the transaction record
        }
    }

    /**
     * displays the total value of all stocks in the portfolio
     */
    public static void viewTotalPortfolioValue() {
        double totalValue = 0.0; // initializes total portfolio value
        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double currentPrice = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                totalValue += stockHoldings.get(symbol) * currentPrice; // adds to the total portfolio value
            } catch (IOException e) { // catches IO exceptions
                System.out.println("Error fetching price for " + symbol); // prints error message
            }
        }
        System.out.println("Total Portfolio Value: $" + totalValue); // prints total portfolio value
    }

    /**
     * displays the top 5 most valuable stocks in the portfolio
     */
    public static void viewTop5ValuableStocks() {
        List<Map.Entry<String, Integer>> stockList = new ArrayList<>(stockHoldings.entrySet()); // converts holdings to a list
        stockList.sort((a, b) -> { // sorts the list by stock value
            try {
                double priceA = StockDataFetcher.getStockPrice(a.getKey()); // fetches price for stock A
                double priceB = StockDataFetcher.getStockPrice(b.getKey()); // fetches price for stock B
                return Double.compare(priceB * b.getValue(), priceA * a.getValue()); // compares total values
            } catch (IOException e) { // catches IO exceptions
                return 0; // returns 0 if theres an error
            }
        });
        System.out.println("\nTop 5 Most Valuable Stocks:"); // prints header for top 5 stocks
        for (int i = 0; i < Math.min(5, stockList.size()); i++) { // iterates through the top 5 stocks
            String symbol = stockList.get(i).getKey(); // gets the stock symbol
            try {
                double price = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                System.out.println((i + 1) + ". " + symbol + " | Value: $" + (price * stockHoldings.get(symbol))); // prints stock details
            } catch (IOException e) { // catches IO exceptions
                System.out.println((i + 1) + ". " + symbol + " | Error fetching price."); // prints error message
            }
        }
    }

    /**
     * displays the best performing stocks in the portfolio based on percentage gain
     */
    public static void viewBestPerformingStocks() {
        System.out.println("\nBest Performing Stocks:"); // prints header for best performing stocks
        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double purchasePrice = stockPurchasePrice.get(symbol); // gets the purchase price
                double currentPrice = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                double gain = ((currentPrice - purchasePrice) / purchasePrice) * 100; // calculates the gain percentage
                System.out.println(symbol + " | Gain: " + String.format("%.2f", gain) + "%"); // prints stock gain
            } catch (IOException e) { // catches IO exceptions
                System.out.println(symbol + " | Error fetching price."); // prints error message
            }
        }
    }

    /**
     * displays the worst performing stocks in the portfolio based on percentage loss
     */
    public static void viewWorstPerformingStocks() {
        System.out.println("\nWorst Performing Stocks:"); // prints header for worst performing stocks
        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double purchasePrice = stockPurchasePrice.get(symbol); // gets the purchase price
                double currentPrice = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                double loss = ((currentPrice - purchasePrice) / purchasePrice) * 100; // calculates the loss percentage
                System.out.println(symbol + " | Loss: " + String.format("%.2f", loss) + "%"); // prints stock loss
            } catch (IOException e) { // catches IO exceptions
                System.out.println(symbol + " | Error fetching price."); // prints error message
            }
        }
    }

    /**
     * displays the most traded stock in the portfolio based on transaction history
     */
    public static void viewMostTradedStock() {
        HashMap<String, Integer> tradeCount = new HashMap<>(); // creates a map to count trades
        for (String record : transactionHistory) { // iterates through transaction history
            String[] parts = record.split(" "); // splits the transaction record into parts
            String symbol = parts[2]; // gets the stock symbol from the record
            tradeCount.put(symbol, tradeCount.getOrDefault(symbol, 0) + 1); // updates the trade count
        }
        if (!tradeCount.isEmpty()) { // checks if there are any trades
            String mostTraded = Collections.max(tradeCount.entrySet(), Map.Entry.comparingByValue()).getKey(); // finds the most traded stock
            System.out.println("\nMost Traded Stock: " + mostTraded); // prints the most traded stock
        } else {
            System.out.println("\nNo trades have been made yet."); // prints message if no trades exist
        }
    }

    /**
     * displays the diversification of the portfolio, showing the percentage of each stock
     */
    public static void viewPortfolioDiversification() {
        double totalValue = 0.0; // initializes total portfolio value
        HashMap<String, Double> stockValues = new HashMap<>(); // creates a map to store stock values
        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double price = StockDataFetcher.getStockPrice(symbol); // fetches the current stock price
                double value = stockHoldings.get(symbol) * price; // calculates the total value of the stock
                totalValue += value; // adds to the total portfolio value
                stockValues.put(symbol, value); // stores the stock value
            } catch (IOException ignored) { // ignores IO exceptions
            }
        }
        System.out.println("\nPortfolio Diversification:"); // prints header for diversification
        for (Map.Entry<String, Double> entry : stockValues.entrySet()) { // iterates through each stock value
            double percentage = (entry.getValue() / totalValue) * 100; // calculates the percentage of the portfolio
            System.out.println(entry.getKey() + " | " + String.format("%.2f", percentage) + "% of portfolio"); // prints stock percentage
        }
    }

    /**
     * displays the most recent transactions (up to the last 5)
     */
    public static void viewRecentTransactions() {
        System.out.println("\nLast 5 Transactions:"); // prints header for recent transactions
        int count = Math.min(5, transactionHistory.size()); // determines the number of transactions to display
        for (int i = transactionHistory.size() - count; i < transactionHistory.size(); i++) { // iterates through the last 5 transactions
            System.out.println(transactionHistory.get(i)); // prints the transactions
        }
    }

    /**
     * detects and displays stocks with unusual price movements
     */
    public static void viewUnusualMovements() {
        System.out.println("\nStocks with 5%+ Movement Today:"); // prints header for unusual movements
        for (String symbol : stockHoldings.keySet()) { // iterates through each stock in the portfolio
            try {
                double movement = StockDataFetcher.getDailyChangePercentage(symbol); // fetches the daily change percentage
                if (Math.abs(movement) >= 5) { // checks if the movement is 5% or more
                    System.out.println(symbol + " | Change: " + String.format("%.2f", movement) + "%"); // prints the stock movement
                }
            } catch (IOException e) { // catches IO exceptions
                System.out.println(symbol + " | Error fetching daily change."); // prints error message
            }
        }
    }

    /**
     * displays the top 5 market gainers
     */
    public static void viewTopMarketGainers() {
        System.out.println("\nTop 5 Market Gainers:"); // prints header for market gainers
        try {
            for (String stock : StockDataFetcher.getTopGainers()) { // iterates through the top gainers
                System.out.println(stock); // prints the stock
            }
        } catch (IOException e) { // catches IO exceptions
            System.out.println("Error fetching market gainers."); // prints error message
        }
    }

    /**
     * displays the top 5 market losers
     */
    public static void viewTopMarketLosers() {
        System.out.println("\nTop 5 Market Losers:"); // prints header for market losers
        try {
            for (String stock : StockDataFetcher.getTopLosers()) { // iterates through the top losers
                System.out.println(stock); // prints the stock
            }
        } catch (IOException e) { // catches IO exceptions
            System.out.println("Error fetching market losers."); // prints error message
        }
    }

    /**
     * displays the most actively traded stocks in the market
     */
    public static void viewMostActivelyTraded() {
        System.out.println("\nMost Actively Traded Stocks:"); // prints header for actively traded stocks
        try {
            for (String stock : StockDataFetcher.getMostActivelyTraded()) { // iterates through the actively traded stocks
                System.out.println(stock); // prints the stock
            }
        } catch (IOException e) { // catches IO exceptions
            System.out.println("Error fetching active stocks."); // prints error message
        }
    }
/**
 * Below are helper functions to aid in the Junit testing phase, and can be used
 * as well for extra functionality in future ;)
 */
    /**
     * Resets all static data to initial values
     */
    public static void reset() {
        watchlist.clear();
        stockHoldings.clear();
        stockPurchasePrice.clear();
        transactionHistory.clear();
        balance = 10000.0;
    }

    /**
     * Checks if a symbol is in the watchlist
     * @param symbol The stock symbol to check
     * @return true if the symbol is in the watchlist, false otherwise
     */
    public static boolean isInWatchlist(String symbol) {
        return watchlist.contains(symbol);
    }

    /**
     * Gets the number of symbols in the watchlist
     * @return The size of the watchlist
     */
    public static int getWatchlistSize() {
        return watchlist.size();
    }

    /**
     * Gets the quantity of a specific stock in holdings
     * @param symbol The stock symbol to check
     * @return The quantity of the stock, or 0 if not owned
     */
    public static int getStockQuantity(String symbol) {
        return stockHoldings.getOrDefault(symbol, 0);
    }

    /**
     * Adds funds to the balance
     * @param amount The amount to add
     */
    public static void deposit(double amount) {
        balance += amount;
    }

    /**
     * Withdraws funds from the balance
     * @param amount The amount to withdraw
     */
    public static void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        }
    }
}