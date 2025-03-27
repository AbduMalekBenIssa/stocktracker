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
}
