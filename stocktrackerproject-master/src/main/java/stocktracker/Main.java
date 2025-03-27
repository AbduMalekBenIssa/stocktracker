package stocktracker;

import java.io.*;
import java.util.*;

/**
 * Main class for the Stock Tracker & Trader program.
 * This class handles user input and displays the main menu through a loop.
 * The menu allows users to manage their stock watchlist, buy/sell stocks,
 * view portfolio insights, and access advanced financial insights.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version Demo 1
 * @Tutorial T04
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in); // Scanner object for user input
    private static final int EXIT_OPTION = 9; // Constant for menu exit option

    /**
     * Main method that runs the menu system inside a do-while loop.
     * The loop continues running until the user selects the exit option.
     *
     */
    public static void main(String[] args) {
        int choice;

        do {
            displayMenu(); // Display the menu options
            choice = getUserChoice(); // Get the user's menu selection
            handleMenuChoice(choice); // Process user selection
        } while (choice != EXIT_OPTION); // Exit when user selects 9

        System.out.println("Exiting Stock Tracker. Goodbye!");
    }

    /**
     * Prints the menu options for the user.
     * Displays the current balance and all available actions.
     */
    private static void displayMenu() {
        System.out.println("\n====== STOCK TRACKER & TRADER ======");
        System.out.println("💰 Current Balance: $" + StockTrackerData.getBalance()); // Display balance
        System.out.println("1. Add Stock to Watchlist");
        System.out.println("2. Remove Stock from Watchlist");
        System.out.println("3. View Watchlist");
        System.out.println("4. Buy Stock");
        System.out.println("5. Sell Stock");
        System.out.println("6. View Portfolio");
        System.out.println("7. View Transaction History");
        System.out.println("8. Cool Stuff (Advanced Insights)");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    /**
     * Gets the user's menu selection while handling invalid input.
     * If the user enters a non-numeric value, an error message is displayed,
     * and the function returns -1 to prompt the user again.
     *
     * @return The menu option selected by the user.
     */
    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine()); // Read and parse user input
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1; // Return -1 to force the menu to re-prompt
        }
    }

    /**
     * Handles user selection from the menu.
     * Calls the appropriate function from `StockTrackerData` based on the selected option.
     *
     * @param choice The user's menu selection.
     */
    private static void handleMenuChoice(int choice) {
        switch (choice) {
            case 1 -> {
                System.out.print("Enter stock symbol to add: ");
                String symbol = scanner.nextLine().toUpperCase();
                StockTrackerData.addToWatchlist(symbol);
            }
            case 2 -> {
                System.out.print("Enter stock symbol to remove: ");
                String symbol = scanner.nextLine().toUpperCase();
                StockTrackerData.removeFromWatchlist(symbol);
            }
            case 3 -> StockTrackerData.viewWatchlist();
            case 4 -> {
                System.out.print("Enter stock symbol to buy: ");
                String symbol = scanner.nextLine().toUpperCase();
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                StockTrackerData.buyStock(symbol, quantity);
            }
            case 5 -> {
                System.out.print("Enter stock symbol to sell: ");
                String symbol = scanner.nextLine().toUpperCase();
                System.out.print("Enter quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine());
                StockTrackerData.sellStock(symbol, quantity);
            }
            case 6 -> StockTrackerData.viewPortfolio();
            case 7 -> StockTrackerData.viewTransactionHistory();
            case 8 -> showAdvancedInsightsMenu(); // Opens the Cool Stuff insights menu
            case EXIT_OPTION -> System.out.println("Saving data and exiting...");
            default -> System.out.println("Invalid choice. Try again.");
        }
    }

    /**
     * Displays the advanced insights menu (Cool Stuff).
     * Allows users to access more detailed financial reports and market insights.
     * Uses a do-while loop to keep showing the menu until the user selects "Go Back".
     */
    private static void showAdvancedInsightsMenu() {

        final int BACK_OPTION = 12; // Constant for going back option
        int insightChoice;
        do {
            System.out.println("\n====== Cool Stuff (Advanced Insights) ======");
            System.out.println("1. View Total Portfolio Value");
            System.out.println("2. View Top 5 Most Valuable Stocks");
            System.out.println("3. View Best Performing Stocks (Highest Gain)");
            System.out.println("4. View Worst Performing Stocks (Biggest Loss)");
            System.out.println("5. View Most Traded Stock");
            System.out.println("6. View Portfolio Diversification");
            System.out.println("7. View Recent Transactions");
            System.out.println("8. View Stocks with Unusual Market Movements");
            System.out.println("9. View Top 5 Market Gainers 🌍");
            System.out.println("10. View Top 5 Market Losers 🌍");
            System.out.println("11. View Most Actively Traded Stocks 🌍");
            System.out.println("12. Go Back");
            System.out.print("Enter your choice: ");

            insightChoice = getUserChoice();

            switch (insightChoice) {
                case 1 -> StockTrackerData.viewTotalPortfolioValue();
                case 2 -> StockTrackerData.viewTop5ValuableStocks();
                case 3 -> StockTrackerData.viewBestPerformingStocks();
                case 4 -> StockTrackerData.viewWorstPerformingStocks();
                case 5 -> StockTrackerData.viewMostTradedStock();
                case 6 -> StockTrackerData.viewPortfolioDiversification();
                case 7 -> StockTrackerData.viewRecentTransactions();
                case 8 -> StockTrackerData.viewUnusualMovements();
                case 9 -> StockTrackerData.viewTopMarketGainers();
                case 10 -> StockTrackerData.viewTopMarketLosers();
                case 11 -> StockTrackerData.viewMostActivelyTraded();
                case 12 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid choice. Try again.");
            }
        } while (insightChoice != BACK_OPTION);
    }
}
