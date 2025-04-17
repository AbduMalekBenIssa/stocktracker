package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import stocktracker.model.OwnedStock;
import stocktracker.model.WatchlistStock;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller for the Watchlist view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class WatchlistController extends BaseController {
    @FXML
    private Label watchlistCountLabel;

    @FXML
    private TableView<WatchlistStock> watchlistTable;

    @FXML
    private TableColumn<WatchlistStock, String> symbolColumn;

    @FXML
    private TableColumn<WatchlistStock, String> nameColumn;

    @FXML
    private TableColumn<WatchlistStock, Number> priceColumn;

    @FXML
    private TableColumn<WatchlistStock, Number> changeColumn;

    private ObservableList<WatchlistStock> stockList;

    @Override
    protected void onInitialize() {
        setupTable();
        updateWatchlistData();
    }
    /**
     * Sets up the table columns and cell factories
     */
    private void setupTable() {
        // Initialize the observable list
        stockList = FXCollections.observableArrayList();
        watchlistTable.setItems(stockList);

        // Setup the columns
        symbolColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        // Format currency values
        priceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()));
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value.doubleValue()));
                }
            }
        });
        // Change percentage with colored text
        changeColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getChangePercentage()));
        changeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    double change = value.doubleValue();
                    setText(String.format("%.2f%%", change));

                    if (change > 0) {
                        setStyle("-fx-text-fill: -fx-success;");
                        setText("+" + getText());
                    } else if (change < 0) {
                        setStyle("-fx-text-fill: -fx-danger;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        // Set row factory for context menu
        watchlistTable.setRowFactory(tv -> {
            TableRow<WatchlistStock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStockDetailsDialog(row.getItem());
                }
            });

            // Create context menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem buyMenuItem = new MenuItem("Buy Stock");
            buyMenuItem.setOnAction(e -> buyStock(row.getItem()));

            MenuItem removeMenuItem = new MenuItem("Remove from Watchlist");
            removeMenuItem.setOnAction(e -> removeFromWatchlist(row.getItem()));

            MenuItem detailsMenuItem = new MenuItem("View Details");
            detailsMenuItem.setOnAction(e -> showStockDetailsDialog(row.getItem()));

            contextMenu.getItems().addAll(buyMenuItem, new SeparatorMenuItem(), removeMenuItem, new SeparatorMenuItem(), detailsMenuItem);

            // Only display context menu for non-empty rows
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }
    /**
     * Updates the watchlist data
     */
    private void updateWatchlistData() {
        // Clear the list
        stockList.clear();

        // Add all stocks from the watchlist
        stockList.addAll(user.getWatchlist().getAllStocks());

        // Update summary label
        int stockCount = stockList.size();
        watchlistCountLabel.setText(String.valueOf(stockCount));
    }
    /**
     * Refreshes the stock prices in the watchlist
     */
    @FXML
    private void refreshPrices() {
        try {
            // Update each stock price
            for (WatchlistStock stock : stockList) {
                double price = stockMarket.getStockPrice(stock.getSymbol());
                double change = stockMarket.getDailyChangePercentage(stock.getSymbol());
                stock.update(price, change);
            }

            // Refresh the table
            watchlistTable.refresh();

            showInfoDialog("Refresh Complete", "Prices Updated", "Stock prices have been updated with the latest market data.");
        } catch (IOException e) {
            showErrorDialog("Refresh Error", "Could not update stock prices", e.getMessage());
        }
    }
    /**
     * Shows a dialog to add a new stock to the watchlist
     */
    @FXML
    private void addToWatchlist() {
        // Create the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add to Watchlist");
        dialog.setHeaderText("Add a Stock to Your Watchlist");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the form fields
        VBox content = new VBox(10);
        content.getStyleClass().add("dialog-content");

        TextField symbolField = new TextField();
        symbolField.setPromptText("Stock Symbol (e.g., AAPL)");

        Label symbolInfoLabel = new Label("Enter a valid stock symbol");

        content.getChildren().addAll(
                new Label("Stock Symbol:"),
                symbolField,
                symbolInfoLabel
        );
        // Add validation for the symbol
        symbolField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    String symbol = newValue.toUpperCase();
                    if (stockMarket.isValidSymbol(symbol)) {
                        String name = stockMarket.getCompanyName(symbol);
                        double price = stockMarket.getStockPrice(symbol);
                        symbolInfoLabel.setText(String.format("%s - Current Price: $%.2f", name, price));
                        symbolInfoLabel.getStyleClass().removeAll("text-danger");
                        symbolInfoLabel.getStyleClass().add("text-info");
                    } else {
                        symbolInfoLabel.setText("Invalid stock symbol");
                        symbolInfoLabel.getStyleClass().removeAll("text-info");
                        symbolInfoLabel.getStyleClass().add("text-danger");
                    }
                } catch (IOException e) {
                    symbolInfoLabel.setText("Error checking symbol: " + e.getMessage());
                    symbolInfoLabel.getStyleClass().removeAll("text-info");
                    symbolInfoLabel.getStyleClass().add("text-danger");
                }
            } else {
                symbolInfoLabel.setText("Enter a valid stock symbol");
                symbolInfoLabel.getStyleClass().removeAll("text-danger", "text-info");
            }
        });
}