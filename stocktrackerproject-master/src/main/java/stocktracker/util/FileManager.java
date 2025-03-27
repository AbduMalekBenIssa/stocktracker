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
     * @param user The user to save
     * @param filename The filename to save to
     * @throws IOException If there's an error writing to the file
     */
    public static void saveToFile(User user, String filename) throws IOException{
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




        }
}
