package stocktracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public abstract class Transaction {

    protected String symbol;
    protected int quantity;
    protected double price;
    protected LocalDateTime timestamp;

    public Transaction(String symbol, int quantity, double price) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String symbol, int quantity, double price, LocalDateTime timestamp) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    /**
     * Gets the stock symbol
     *
     * @return The stock symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Gets the quantity
     *
     * @return The quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Gets the price
     *
     * @return The price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the timestamp
     *
     * @return The timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the total value of the transaction
     *
     * @return The total value
     */
    public double getTotalValue() {
        return quantity * price;
    }

    /**
     * Gets the transaction type (Buy or Sell)
     *
     * @return The transaction type
     */
    public abstract String getType();

    /**
     * Returns a string representation of the transaction
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return getType() + " [Symbol=" + symbol +
                ", Quantity=" + quantity +
                ", Price=$" + String.format("%.2f", price) +
                ", Total=$" + String.format("%.2f", getTotalValue()) +
                ", Time=" + timestamp.format(formatter) + "]";
    }


}
