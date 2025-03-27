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







}
