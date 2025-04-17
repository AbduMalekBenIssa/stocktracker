package stocktracker.util;

import javafx.beans.property.*;
import stocktracker.model.*;
import stocktracker.service.StockMarket;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

/**
 * Class for handling user data file I/O operations and managing data file path setting.
 * Implements the Singleton pattern.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.2
 * @Tutorial T04
 */
public class FileManager {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final String PROPERTIES_FILE = "stocktracker.properties";

    // --- Singleton Instance ---
    private static FileManager instance;

    // --- Configuration Property (File Path Only) ---
    private final StringProperty dataFilePath = new SimpleStringProperty();

    // --- Property Key (File Path Only) ---
    private static final String KEY_DATA_FILE_PATH = "data.filePath";

    // --- Default Value (File Path Only) ---
    private static final String DEFAULT_DATA_FILE_PATH = System.getProperty("user.home") + File.separator + "StockTrackerData";

    // --- Private Constructor for Singleton ---
    private FileManager() {
        loadDataConfiguration(); // Load path setting on initialization
    }

    // --- Get Singleton Instance ---
    public static synchronized FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    // --- Getter for File Path Property (for potential binding elsewhere) ---
    public StringProperty dataFilePathProperty() {
        return dataFilePath;
    }

    // --- Standard Getters/Setters for File Path ---
    public String getDataFilePath() { return dataFilePath.get(); }
    public void setDataFilePath(String path) { dataFilePath.set(path); }

    // --- Load/Save/Reset Configuration for File Path Only ---

    /**
     * Loads data file path configuration from the shared properties file.
     */
    public void loadDataConfiguration() {
        Properties properties = new Properties();
        File propertiesFile = new File(PROPERTIES_FILE);

        if (propertiesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("FileManager: Error loading properties: " + e.getMessage());
                // Fallback to default path if loading fails
                resetDataConfigurationToDefaults();
                return;
            }
        }
        // Load file path, using default if key is missing
        setDataFilePath(properties.getProperty(KEY_DATA_FILE_PATH, DEFAULT_DATA_FILE_PATH));
    }

    /**
     * Saves the current data file path configuration to the shared properties file.
     * Note: Reads existing properties first to avoid overwriting other settings.
     */
    public void saveDataConfiguration() {
        Properties properties = new Properties();
        File propertiesFile = new File(PROPERTIES_FILE);

        if (propertiesFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propertiesFile)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("FileManager: Error reading existing properties before save: " + e.getMessage());
            }
        }
        // Update only the file path property
        properties.setProperty(KEY_DATA_FILE_PATH, getDataFilePath());

        try (FileOutputStream fos = new FileOutputStream(PROPERTIES_FILE)) {
            properties.store(fos, "Stock Tracker Application Settings (updated by FileManager)");
        } catch (IOException e) {
            System.err.println("FileManager: Error saving properties: " + e.getMessage());
        }
    }

    /**
     * Resets the data file path configuration managed by FileManager to its default.
     * Does NOT automatically save.
     */
    public void resetDataConfigurationToDefaults() {
        setDataFilePath(DEFAULT_DATA_FILE_PATH);
    }


    // --- User Data Save/Load Methods (Unchanged, still use internal dataFilePath) ---
    public void saveUserData(User user) throws IOException {
        String filename = getDataFilePath();
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename = filename + File.separator + "userdata.txt";
        }
        File file = new File(filename);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("USER|" + user.getName() + "|" + user.getBalance());
            writer.println("PORTFOLIO_START");
            for (OwnedStock stock : user.getPortfolio().getAllStocks()) {
                writer.println("OWNED|" + stock.getSymbol() + "|" + stock.getName() + "|"
                        + stock.getCurrentPrice() + "|" + stock.getQuantity() + "|"
                        + stock.getPurchasePrice());
            }
            writer.println("PORTFOLIO_END");
            writer.println("WATCHLIST_START");
            for (WatchlistStock stock : user.getWatchlist().getAllStocks()) {
                writer.println("WATCH|" + stock.getSymbol() + "|" + stock.getName() + "|"
                        + stock.getCurrentPrice() + "|" + stock.getChangePercentage());
            }
            writer.println("WATCHLIST_END");
            writer.println("TRANSACTIONS_START");
            for (Transaction transaction : user.getTransactions()) {
                if (transaction instanceof BuyTransaction) {
                    writer.println("BUY|" + transaction.getSymbol() + "|" + transaction.getQuantity() + "|"
                            + transaction.getPrice() + "|" + transaction.getTimestamp().format(DATE_TIME_FORMATTER));
                } else if (transaction instanceof SellTransaction) {
                    SellTransaction sellTransaction = (SellTransaction) transaction;
                    writer.println("SELL|" + transaction.getSymbol() + "|" + transaction.getQuantity() + "|"
                            + transaction.getPrice() + "|" + sellTransaction.getProfitLoss() + "|"
                            + transaction.getTimestamp().format(DATE_TIME_FORMATTER));
                }
            }
            writer.println("TRANSACTIONS_END");
            System.out.println("User data saved to " + filename);
        }
    }

    public User loadUserData() throws IOException {
        String filename = getDataFilePath();
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename = filename + File.separator + "userdata.txt";
        }
        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("Data file not found at configured path: " + filename);
        }
        User user = null;
        Portfolio portfolio = new Portfolio();
        Watchlist watchlist = new Watchlist();
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String section = "";
            while ((line = reader.readLine()) != null) {
                if (line.equals("PORTFOLIO_START")) { section = "PORTFOLIO"; continue; }
                else if (line.equals("PORTFOLIO_END")) { section = ""; continue; }
                else if (line.equals("WATCHLIST_START")) { section = "WATCHLIST"; continue; }
                else if (line.equals("WATCHLIST_END")) { section = ""; continue; }
                else if (line.equals("TRANSACTIONS_START")) { section = "TRANSACTIONS"; continue; }
                else if (line.equals("TRANSACTIONS_END")) { section = ""; continue; }
                String[] parts = line.split("\\|");
                if (parts[0].equals("USER")) {
                    user = new User(parts[1], Double.parseDouble(parts[2]));
                } else if (section.equals("PORTFOLIO") && parts[0].equals("OWNED")) {
                    OwnedStock stock = new OwnedStock(parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), Double.parseDouble(parts[5]));
                    portfolio.addStock(stock);
                } else if (section.equals("WATCHLIST") && parts[0].equals("WATCH")) {
                    WatchlistStock stock = new WatchlistStock(parts[1], parts[2], Double.parseDouble(parts[3]), Double.parseDouble(parts[4]));
                    watchlist.addStock(stock);
                } else if (section.equals("TRANSACTIONS")) {
                    if (parts[0].equals("BUY")) {
                        BuyTransaction transaction = new BuyTransaction(parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), LocalDateTime.parse(parts[4], DATE_TIME_FORMATTER));
                        transactions.add(transaction);
                    } else if (parts[0].equals("SELL")) {
                        SellTransaction transaction = new SellTransaction(parts[1], Integer.parseInt(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), LocalDateTime.parse(parts[5], DATE_TIME_FORMATTER));
                        transactions.add(transaction);
                    }
                }
            }
        }
        if (user == null) { throw new IOException("Invalid file format or no user data found"); }
        user.setPortfolio(portfolio);
        user.setWatchlist(watchlist);
        user.setTransactions(transactions);
        return user;
    }
}