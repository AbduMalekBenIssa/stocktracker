package stocktracker.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Class representing a user of the stock tracker application
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class User {
    private String name;
    private double balance;
    private Portfolio portfolio;
    private Watchlist watchlist;
    private List<Transaction> transactions;

    /**
     * Constructor for the User class
     *
     * @param name           The user's name
     * @param initialBalance The user's initial balance
     */
    public User(String name, double initialBalance) {
        this.name = name;
        this.balance = initialBalance;
        this.portfolio = new Portfolio();
        this.watchlist = new Watchlist();
        this.transactions = new ArrayList<>();
    }

    /**
     * Gets the user's name
     *
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's balance
     *
     * @return The balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Gets the user's portfolio
     *
     * @return The portfolio
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Gets the user's watchlist
     *
     * @return The watchlist
     */
    public Watchlist getWatchlist() {
        return watchlist;
    }

    /**
     * Gets the user's transactions
     *
     * @return The transactions
     */
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    /**
     * Gets the most recent transactions
     *
     * @param count The number of transactions to get
     * @return The recent transactions
     */
    public List<Transaction> getRecentTransactions(int count) {
        int size = transactions.size();
        int startIndex = Math.max(0, size - count);
        return new ArrayList<>(transactions.subList(startIndex, size));
    }

    /**
     * Adds a transaction to the user's history
     *
     * @param transaction The transaction to add
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Deposits funds into the user's account
     *
     * @param amount The amount to deposit
     * @return True if successful
     */
    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    /**
     * Withdraws funds from the user's account
     *
     * @param amount The amount to withdraw
     * @return True if successful
     */
    public boolean withdraw(double amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    /**
     * Gets the total asset value (balance + portfolio)
     *
     * @return The total value
     */
    public double getTotalValue() {
        return balance + portfolio.getTotalValue();
    }


    /**
     * Returns a string representation of the user
     */
    @Override
    public String toString() {
        return "User [Name=" + name +
                ", Balance=$" + String.format("%.2f", balance) +
                ", Portfolio Value=$" + String.format("%.2f", portfolio.getTotalValue()) +
                ", Total Value=$" + String.format("%.2f", getTotalValue()) + "]";
    }

    /**
     * Sets the user's portfolio
     *
     * @param portfolio The portfolio to set
     */
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    /**
     * Sets the user's watchlist
     *
     * @param watchlist The watchlist to set
     */
    public void setWatchlist(Watchlist watchlist) {
        this.watchlist = watchlist;
    }

    /**
     * Sets the user's transaction history
     *
     * @param transactions The transactions to set
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    /**
     * Sets the user's name.
     *
     * @param name The new name to set.
     */
    public void setName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
    }
}
