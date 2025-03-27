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




}
