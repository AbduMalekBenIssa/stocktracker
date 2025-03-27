package stocktracker.util;

import stocktracker.model.*;
import stocktracker.service.StockMarket;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for handling file I/O operations
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */

public class FileManager {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Saves user data to a text file
     *
     * @param user     The user to save
     * @param filename The filename to save to
     * @throws IOException If there's an error writing to the file
     */
    public static void saveToFile(User user, String filename) throws IOException {
        // Add .txt extension if not already present
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        File file = new File(filename);

        // Check if file exists and prompt for overwrite if it does
        if (file.exists()) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("File already exists. Do you want to overwrite it? (y/n): ");
            String response = scanner.nextLine().toLowerCase();

            if (!response.equals("y")) {
                System.out.println("Save operation cancelled.");
                return; // Exit without saving
            }
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write user info
            writer.println("USER|" + user.getName() + "|" + user.getBalance());

            // Write portfolio stocks
            writer.println("PORTFOLIO_START");
            for (OwnedStock stock : user.getPortfolio().getAllStocks()) {
                writer.println("OWNED|" + stock.getSymbol() + "|" + stock.getName() + "|"
                        + stock.getCurrentPrice() + "|" + stock.getQuantity() + "|"
                        + stock.getPurchasePrice());
            }
            writer.println("PORTFOLIO_END");

            // Write watchlist stocks
            writer.println("WATCHLIST_START");
            for (WatchlistStock stock : user.getWatchlist().getAllStocks()) {
                writer.println("WATCH|" + stock.getSymbol() + "|" + stock.getName() + "|"
                        + stock.getCurrentPrice() + "|" + stock.getChangePercentage());
            }
            writer.println("WATCHLIST_END");

            // Write transactions
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
            System.out.println("Data saved to " + filename);
        }
    }

    /**
     * Loads user data from a text file
     *
     * @param filename    The filename to load from
     * @param stockMarket The stock market service
     * @return The loaded user
     * @throws IOException If there's an error reading the file
     */
    public static User loadFromFile(String filename) throws IOException {
        // Add .txt extension if not already present
        if (!filename.toLowerCase().endsWith(".txt")) {
            filename += ".txt";
        }

        File file = new File(filename);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filename);
        }

        User user = null;
        Portfolio portfolio = new Portfolio();
        Watchlist watchlist = new Watchlist();
        List<Transaction> transactions = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            String section = "";

            while ((line = reader.readLine()) != null) {
                // Check for section markers
                if (line.equals("PORTFOLIO_START")) {
                    section = "PORTFOLIO";
                    continue;
                } else if (line.equals("PORTFOLIO_END")) {
                    section = "";
                    continue;
                } else if (line.equals("WATCHLIST_START")) {
                    section = "WATCHLIST";
                    continue;
                } else if (line.equals("WATCHLIST_END")) {
                    section = "";
                    continue;
                } else if (line.equals("TRANSACTIONS_START")) {
                    section = "TRRANSACTIONS";
                    continue;
                } else if (line.equals("TRANSACTIONS_END")) {
                    section = "";
                    continue;

                    // Process the line based on the current section
                    String[] parts = line.split("\\|");

                    if (parts[0].equals("USER")) {
                        String name = parts[1];
                        double balance = Double.parseDouble(parts[2]);
                        user = new User(name, balance);
                    } else if (section.equals("PORTFOLIO") && parts[0].equals("OWNED")) {
                        String symbol = parts[1];
                        String name = parts[2];
                        double currentPrice = Double.parseDouble(parts[3]);
                        int quantity = Integer.parseInt(parts[4]);
                        double purchasePrice = Double.parseDouble(parts[5]);

                        OwnedStock stock = new OwnedStock(symbol, name, currentPrice, quantity, purchasePrice);
                        portfolio.addStock(stock);
                    } else if (section.equals("WATCHLIST") && parts[0].equals("WATCH")) {
                        String symbol = parts[1];
                        String name = parts[2];
                        double currentPrice = Double.parseDouble(parts[3]);
                        double changePercentage = Double.parseDouble(parts[4]);

                        WatchlistStock stock = new WatchlistStock(symbol, name, currentPrice, changePercentage);
                        watchlist.addStock(stock);
                    } else if (section.equals("TRANSACTIONS")) {
                        if (parts[0].equals("BUY")) {
                            String symbol = parts[1];
                            int quantity = Integer.parseInt(parts[2]);
                            double price = Double.parseDouble(parts[3]);
                            LocalDateTime timestamp = LocalDateTime.parse(parts[4], DATE_TIME_FORMATTER);

                            BuyTransaction transaction = new BuyTransaction(symbol, quantity, price, timestamp);
                            transactions.add(transaction);
                        } else if (parts[0].equals("SELL")) {
                            String symbol = parts[1];
                            int quantity = Integer.parseInt(parts[2]);
                            double price = Double.parseDouble(parts[3]);
                            double profitLoss = Double.parseDouble(parts[4]);
                            LocalDateTime timestamp = LocalDateTime.parse(parts[5], DATE_TIME_FORMATTER);

                            SellTransaction transaction = new SellTransaction(symbol, quantity, price, profitLoss, timestamp);
                            transactions.add(transaction);
                        }


                    }
                }
            }
            if (user == null) {
                throw new IOException("Invalid file format or no user data found");
            }

            // Set the loaded data to the user
            user.setPortfolio(portfolio);
            user.setWatchlist(watchlist);
            user.setTransactions(transactions);

            return user;
        }
    }
}





