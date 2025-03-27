package stocktracker.model;

/**
 * Class representing a stock owned by the user
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class OwnedStock extends Stock {
    private int quantity;
    private double purchasePrice;


    /**
     * Constructor for the OwnedStock class
     *
     * @param symbol The stock ticker symbol
     * @param name The company name
     * @param currentPrice The current price of the stock
     * @param quantity The number of shares owned
     * @param purchasePrice The average purchase price
     */
    public OwnedStock(String symbol, String name, double currentPrice, int quantity, double purchasePrice) {
        super(symbol, name, currentPrice);
        this.quantity = quantity;
        this.purchasePrice = purchasePrice;
    }
    /**
     * Gets the number of shares owned
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the average purchase price
     *
     * @return The purchase price
     */
    public double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * Gets the total value of the owned stock
     *
     * @return The total value
     */
    public double getTotalValue() {
        return quantity * currentPrice;
    }

    /**
     * Gets the profit/loss on the stock
     *
     * @return The profit/loss
     */
    public double getProfitLoss() {
        return (currentPrice - purchasePrice) * quantity;
    }




}
