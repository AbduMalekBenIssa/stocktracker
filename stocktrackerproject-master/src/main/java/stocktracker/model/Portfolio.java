package stocktracker.model;

/**
 * Class representing a user's stock portfolio
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class Portfolio {
    private Map<String, OwnedStock> stocks;

    /**
     * Constructor for the Portfolio class
     */
    public Portfolio() {
        this.stocks = new HashMap<>();
    }
}

