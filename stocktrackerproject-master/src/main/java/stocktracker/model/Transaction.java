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

}
