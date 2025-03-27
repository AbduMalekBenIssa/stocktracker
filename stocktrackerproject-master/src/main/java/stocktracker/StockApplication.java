package stocktracker;

import stocktracker.model.*;
import stocktracker.service.*;
import stocktracker.util.*;
import stocktracker.analysis.*;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for the Stock Tracker Application
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */

public class StockApplication {

    private User user;
    private StockMarket stockMarket;
    private Scanner scanner;
    private boolean running;

/**
 * Constructor for the StockApplication class
 *
 * @param initialDataFile Optional file to load initial data from
 */

public StockApplication(String initialDataFile) {
    this.stockMarket = new FinancialModelPrepAPI();
    this.scanner = new Scanner(System.in);
    this.running = true;

    // Load data from file if provided
    if (initialDataFile != null && !initialDataFile.isEmpty()) {
        try {
            this.user = FileManager.loadFromFile(initialDataFile);
            System.out.println("Data loaded from " + initialDataFile);
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
            createNewUser();
        }
    } else {
        createNewUser();
    }
}


    /**
     * Creates a new user
     */
    private void createNewUser() {
        System.out.println("Welcome to Stock Tracker!");
        System.out.print("Enter your name: ");
        String name = scanner.nextLine();
        user = new User(name, 10000.0); // Default starting balance
        System.out.println("New user created with $10,000 starting balance.");
    }

    /**
     * Runs the application
     */
    public void run() {
        while (running) {
            displayMenu();
            int choice = getUserChoice();
            processChoice(choice);
        }
        scanner.close();
    }

    /**
     * Displays the main menu
     */
    private void displayMenu() {
        System.out.println("\n========== Stock Tracker Menu ==========");
        System.out.println("1. View Portfolio");
        System.out.println("2. View Watchlist");
        System.out.println("3. Add Stock to Watchlist");
        System.out.println("4. Remove Stock from Watchlist");
        System.out.println("5. Buy Stock");
        System.out.println("6. Sell Stock");
        System.out.println("7. View Recent Transactions");
        System.out.println("8. Show Market Information");
        System.out.println("9. Deposit/Withdraw Funds");
        System.out.println("10. Save Data to File");
        System.out.println("11. Load Data from File");
        System.out.println("12. Cool Stuff (Advanced Insights)");
        System.out.println("13. Exit");
        System.out.println("=========================================");
        System.out.println("Current Balance: $" + String.format("%.2f", user.getBalance()));
        System.out.println("Total Assets: $" + String.format("%.2f", user.getTotalValue()));
        System.out.println("=========================================");
    }

    /**
     * Gets the user's menu choice
     *
     * @return The user's choice
     */
    private int getUserChoice() {
        System.out.print("Enter your choice: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }

    /**
     * Processes the user's menu choice
     *
     * @param choice The user's choice
     */
    private void processChoice(int choice) {
        try {
            switch (choice) {
                case 1: viewPortfolio(); break;
                case 2: viewWatchlist(); break;
                case 3: addToWatchlist(); break;
                case 4: removeFromWatchlist(); break;
                case 5: buyStock(); break;
                case 6: sellStock(); break;
                case 7: viewRecentTransactions(); break;
                case 8: showMarketInfo(); break;
                case 9: manageFunds(); break;
                case 10: saveData(); break;
                case 11: loadData(); break;
                case 12: showCoolStuffMenu(); break;
                case 13: exit(); break;
                default: System.out.println("Invalid choice. Please try again.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Menu option methods

    /**
     * Displays the user's portfolio
     */
    private void viewPortfolio() {
        System.out.println("\n========== Portfolio ==========");
        System.out.println(user.getPortfolio());
    }

    /**
     * Displays the user's watchlist
     */
    private void viewWatchlist() throws IOException {
        System.out.println("\n========== Watchlist ==========");
        // Update watchlist prices first
        List<WatchlistStock> stocks = user.getWatchlist().getAllStocks();
        for (WatchlistStock stock : stocks) {
            try {
                double price = stockMarket.getStockPrice(stock.getSymbol());
                double change = stockMarket.getDailyChangePercentage(stock.getSymbol());
                stock.update(price, change);
            } catch (IOException e) {
                System.out.println("Warning: Could not update price for " + stock.getSymbol());
            }
        }
        System.out.println(user.getWatchlist());
    }

    /**
     * Adds a stock to the watchlist
     */
    private void addToWatchlist() throws IOException {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!stockMarket.isValidSymbol(symbol)) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        if (user.getWatchlist().containsStock(symbol)) {
            System.out.println("Stock is already in watchlist.");
            return;
        }

        String name = stockMarket.getCompanyName(symbol);
        double price = stockMarket.getStockPrice(symbol);
        double change = stockMarket.getDailyChangePercentage(symbol);

        WatchlistStock stock = new WatchlistStock(symbol, name, price, change);
        user.getWatchlist().addStock(stock);

        System.out.println("Added " + name + " (" + symbol + ") to watchlist.");
    }

    /**
     * Removes a stock from the watchlist
     */
    private void removeFromWatchlist() {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!user.getWatchlist().containsStock(symbol)) {
            System.out.println("Stock is not in watchlist.");
            return;
        }

        WatchlistStock stock = user.getWatchlist().removeStock(symbol);
        System.out.println("Removed " + stock.getName() + " (" + symbol + ") from watchlist.");
    }

    /**
     * Buys a stock
     */
    private void buyStock() throws IOException {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!stockMarket.isValidSymbol(symbol)) {
            System.out.println("Invalid stock symbol.");
            return;
        }

        System.out.print("Enter number of shares: ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        String name = stockMarket.getCompanyName(symbol);
        double price = stockMarket.getStockPrice(symbol);
        double totalCost = price * quantity;

        System.out.println("\n========== Buy Order ==========");
        System.out.println("Stock: " + name + " (" + symbol + ")");
        System.out.println("Price: $" + String.format("%.2f", price) + " per share");
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Cost: $" + String.format("%.2f", totalCost));
        System.out.println("Your Balance: $" + String.format("%.2f", user.getBalance()));

        if (totalCost > user.getBalance()) {
            System.out.println("Insufficient funds.");
            return;
        }

        System.out.print("Confirm purchase (y/n): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Purchase cancelled.");
            return;
        }

        // Withdraw funds from balance
        user.withdraw(totalCost);

// Add stock to portfolio
        OwnedStock newStock = new OwnedStock(symbol, name, price, quantity, price);
        if (user.getPortfolio().containsStock(symbol)) {
            OwnedStock existingStock = user.getPortfolio().getStock(symbol);
            existingStock.addShares(quantity, price);
        } else {
            user.getPortfolio().addStock(newStock);
        }

        // Add transaction to history
        Transaction transaction = new BuyTransaction(symbol, quantity, price);
        user.addTransaction(transaction);

        System.out.println("Purchase completed.");
    }

    /**
     * Sells a stock
     */
    private void sellStock() throws IOException {
        System.out.print("Enter stock symbol: ");
        String symbol = scanner.nextLine().toUpperCase();

        if (!user.getPortfolio().containsStock(symbol)) {
            System.out.println("You don't own this stock.");
            return;
        }

        OwnedStock stock = user.getPortfolio().getStock(symbol);

        System.out.print("Enter number of shares (max " + stock.getQuantity() + "): ");
        int quantity;
        try {
            quantity = Integer.parseInt(scanner.nextLine());
            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                return;
            }
            if (quantity > stock.getQuantity()) {
                System.out.println("You don't own that many shares.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity.");
            return;
        }

        double currentPrice = stockMarket.getStockPrice(symbol);
        stock.updatePrice(currentPrice);

        double totalValue = currentPrice * quantity;
        double purchasePrice = stock.getPurchasePrice();
        double profitLoss = (currentPrice - purchasePrice) * quantity;

        System.out.println("\n========== Sell Order ==========");
        System.out.println("Stock: " + stock.getName() + " (" + symbol + ")");
        System.out.println("Price: $" + String.format("%.2f", currentPrice) + " per share");
        System.out.println("Quantity: " + quantity);
        System.out.println("Total Value: $" + String.format("%.2f", totalValue));
        System.out.println("Profit/Loss: $" + String.format("%.2f", profitLoss));

        System.out.print("Confirm sale (y/n): ");
        String confirm = scanner.nextLine().toLowerCase();

        if (!confirm.equals("y")) {
            System.out.println("Sale cancelled.");
            return;
        }

        // Add funds to balance
        user.deposit(totalValue);

        // Remove stock from portfolio
        if (quantity == stock.getQuantity()) {
            user.getPortfolio().removeStock(symbol);
        } else {
            stock.removeShares(quantity);
        }

        // Add transaction to history
        Transaction transaction = new SellTransaction(symbol, quantity, currentPrice, purchasePrice);
        user.addTransaction(transaction);

        System.out.println("Sale completed.");
    }

    /**
     * Displays recent transactions
     */
    private void viewRecentTransactions() {
        System.out.println("\n========== Recent Transactions ==========");
        List<Transaction> transactions = user.getRecentTransactions(10);

        if (transactions.isEmpty()) {
            System.out.println("No transactions to display.");
            return;
        }

        for (int i = transactions.size() - 1; i >= 0; i--) {
            System.out.println(transactions.get(i));
        }
    }

    private void showMarketInfo() throws IOException {
        System.out.println("\n========== Market Information ==========");

        System.out.println("\nTop Gainers:");
        List<String> gainers = stockMarket.getTopGainers();
        for (String symbol : gainers) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);
                System.out.println(name + " (" + symbol + "): $" + String.format("%.2f", price) +
                        " (" + String.format("+%.2f", change) + "%)");
            } catch (IOException e) {
                System.out.println(symbol + ": Error getting data");
            }
        }

        System.out.println("\nTop Losers:");
        List<String> losers = stockMarket.getTopLosers();
        for (String symbol : losers) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);
                System.out.println(name + " (" + symbol + "): $" + String.format("%.2f", price) +
                        " (" + String.format("%.2f", change) + "%)");
            } catch (IOException e) {
                System.out.println(symbol + ": Error getting data");
            }
        }

        System.out.println("\nMost Actively Traded:");
        List<String> active = stockMarket.getMostActivelyTraded();
        for (String symbol : active) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                System.out.println(name + " (" + symbol + "): $" + String.format("%.2f", price));
            } catch (IOException e) {
                System.out.println(symbol + ": Error getting data");
            }
        }
    }

    /**
     * Manages user funds
     */
    private void manageFunds() {
        System.out.println("\n========== Manage Funds ==========");
        System.out.println("Current Balance: $" + String.format("%.2f", user.getBalance()));
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Back to Main Menu");

        System.out.print("Enter your choice: ");
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

















    }
