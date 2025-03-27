package stocktracker.model;

import java.util.ArrayList;
import java.util.List;

public class User {
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
         * @param name The user's name
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

    }
