package stocktracker.model;

/**
 * Class representing a buy transaction
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */

public class BuyTransaction extends Transaction {

    /**
     * Constructor for the BuyTransaction class
     *
     * @param symbol   The stock symbol
     * @param quantity The number of shares
     * @param price    The price per share
     */

    public BuyTransaction(String symbol, int quantity, double price) {
        super(symbol, quantity, price);

    }
    /**
     * Constructor with timestamp parameter
     *
     * @param symbol The stock symbol
     * @param quantity The number of shares
     * @param price The price per share
     * @param timestamp The transaction timestamp
     */
    public BuyTransaction(String symbol, int quantity, double price, LocalDateTime timestamp) {
        super(symbol, quantity, price, timestamp);
    }

    /**
     * Gets the transaction type
     *
     * @return "Buy"
     */
    @Override
    public String getType() {
        return "Buy";
    }
}


}