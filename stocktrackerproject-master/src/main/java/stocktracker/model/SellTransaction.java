package stocktracker.model;

import java.time.LocalDateTime;

/**
 * Class representing a sell transaction
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 2.0
 * @Tutorial T04
 */
public class SellTransaction extends Transaction {
    private double profitLoss;

    /**
     * Constructor for the SellTransaction class
     *
     * @param symbol        The stock symbol
     * @param quantity      The number of shares
     * @param price         The price per share
     * @param purchasePrice The original purchase price
     */
    public SellTransaction(String symbol, int quantity, double price, double purchasePrice) {
        super(symbol, quantity, price);
        this.profitLoss = (price - purchasePrice) * quantity;
    }
    /**
     * Constructor with timestamp parameter
     *
     * @param symbol The stock symbol
     * @param quantity The number of shares
     * @param price The price per share
     * @param profitLoss The profit/loss on the transaction
     * @param timestamp The transaction timestamp
     */
    public SellTransaction(String symbol, int quantity, double price, double profitLoss, LocalDateTime timestamp) {
        super(symbol, quantity, price, timestamp);
        this.profitLoss = profitLoss;
    }

    /**
     * Gets the profit/loss on the transaction
     *
     * @return The profit/loss
     */
    public double getProfitLoss() {
        return profitLoss;
    }

    /**
     * Gets the transaction type
     *
     * @return "Sell"
     */
    @Override
    public String getType() {
        return "Sell";
    }


}

