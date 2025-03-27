package stocktracker.model;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Class representing a user's stock watchlist
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */


public class Watchlist {

    private Map<String, WatchlistStock> stocks;


    /**
     * Constructor for the Watchlist class
     */
    public Watchlist() {
        this.stocks = new HashMap<>();
    }

    /**
     * Adds a stock to the watchlist
     *
     * @param stock The stock to add
     */
    public void addStock(WatchlistStock stock) {
        stocks.put(stock.getSymbol(), stock);
    }


}
