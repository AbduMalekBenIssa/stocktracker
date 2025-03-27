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





}
