package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import stocktracker.model.OwnedStock;
import stocktracker.model.Stock;
import stocktracker.model.BuyTransaction;
import stocktracker.model.SellTransaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Controller for the Portfolio view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class PortfolioController extends BaseController {

    @FXML
    private Label portfolioValueLabel;

    @FXML
    private Label profitLossLabel;

    @FXML
    private Label portfolioCountLabel;

    @FXML
    private TableView<OwnedStock> portfolioTable;

    @FXML
    private TableColumn<OwnedStock, String> symbolColumn;

    @FXML
    private TableColumn<OwnedStock, String> nameColumn;

    @FXML
    private TableColumn<OwnedStock, Number> quantityColumn;

    @FXML
    private TableColumn<OwnedStock, Number> purchasePriceColumn;

    @FXML
    private TableColumn<OwnedStock, Number> currentPriceColumn;

    @FXML
    private TableColumn<OwnedStock, Number> totalValueColumn;

    @FXML
    private TableColumn<OwnedStock, Number> profitLossColumn;

    @FXML
    private TableColumn<OwnedStock, Number> profitLossPercentageColumn;

    private ObservableList<OwnedStock> stockList;

    @Override
    protected void onInitialize() {
        setupTable();
        updatePortfolioData();
    }

    /**
     * Sets up the table columns and cell factories
     */
    private void setupTable() {
        // Initialize the observable list
        stockList = FXCollections.observableArrayList();
        portfolioTable.setItems(stockList);

        // Setup the columns
        symbolColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSymbol()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        quantityColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getQuantity()));

        // Format currency values
        purchasePriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPurchasePrice()));
        purchasePriceColumn.setCellFactory(column -> new TableCell<>() {
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

        currentPriceColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getCurrentPrice()));
        currentPriceColumn.setCellFactory(column -> new TableCell<>() {
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

        totalValueColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getTotalValue()));
        totalValueColumn.setCellFactory(column -> new TableCell<>() {
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

        // Profit loss columns with colored text
        profitLossColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProfitLoss()));
        profitLossColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    double profitLoss = value.doubleValue();
                    setText(String.format("$%.2f", profitLoss));

                    if (profitLoss > 0) {
                        setStyle("-fx-text-fill: -fx-success;");
                    } else if (profitLoss < 0) {
                        setStyle("-fx-text-fill: -fx-danger;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        profitLossPercentageColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getProfitLossPercentage()));
        profitLossPercentageColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Number value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    double percentage = value.doubleValue();
                    setText(String.format("%.2f%%", percentage));

                    if (percentage > 0) {
                        setStyle("-fx-text-fill: -fx-success;");
                    } else if (percentage < 0) {
                        setStyle("-fx-text-fill: -fx-danger;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        // Set row factory for context menu
        portfolioTable.setRowFactory(tv -> {
            TableRow<OwnedStock> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStockDetailsDialog(row.getItem());
                }
            });

            // Create context menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem buyMenuItem = new MenuItem("Buy More");
            buyMenuItem.setOnAction(e -> buyStock(row.getItem().getSymbol()));

            MenuItem sellMenuItem = new MenuItem("Sell Shares");
            sellMenuItem.setOnAction(e -> sellStock(row.getItem()));

            MenuItem detailsMenuItem = new MenuItem("View Details");
            detailsMenuItem.setOnAction(e -> showStockDetailsDialog(row.getItem()));

            contextMenu.getItems().addAll(buyMenuItem, sellMenuItem, new SeparatorMenuItem(), detailsMenuItem);

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
     * Updates the portfolio data
     */
    private void updatePortfolioData() {
        // Clear the list
        stockList.clear();

        // Add all stocks from the portfolio
        stockList.addAll(user.getPortfolio().getAllStocks());

        // Update summary labels
        double portfolioValue = user.getPortfolio().getTotalValue();
        double profitLoss = user.getPortfolio().getTotalProfitLoss();
        int stockCount = stockList.size();

        portfolioValueLabel.setText(String.format("$%.2f", portfolioValue));
        portfolioCountLabel.setText(String.valueOf(stockCount));

        // Set profit/loss value and style
        profitLossLabel.setText(String.format("$%.2f", profitLoss));
        if (profitLoss > 0) {
            profitLossLabel.getStyleClass().removeAll("text-danger");
            profitLossLabel.getStyleClass().add("text-success");
            profitLossLabel.setText("+" + profitLossLabel.getText());
        } else if (profitLoss < 0) {
            profitLossLabel.getStyleClass().removeAll("text-success");
            profitLossLabel.getStyleClass().add("text-danger");
        }
    }

    /**
     * Refreshes the stock prices in the portfolio
     */
    @FXML
    private void refreshPrices() {
        try {
            // Update each stock price
            for (OwnedStock stock : stockList) {
                double price = stockMarket.getStockPrice(stock.getSymbol());
                stock.updatePrice(price);
            }

            // Refresh the table
            portfolioTable.refresh();

            // Update summary values
            updatePortfolioData();

            // Update the main view's user info
            updateUserInfo();

            showInfoDialog("Refresh Complete", "Prices Updated", "Stock prices have been updated with the latest market data.");
        } catch (IOException e) {
            showErrorDialog("Refresh Error", "Could not update stock prices", e.getMessage());
        }
    }

    /**
     * Shows a dialog to add a new stock to the portfolio
     */
    @FXML
    private void addNewStock() {
        // Create the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Buy Stock");
        dialog.setHeaderText("Add a New Stock to Your Portfolio");

        // Set the button types
        ButtonType buyButtonType = new ButtonType("Buy", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);

        // Create the form fields
        VBox content = new VBox(10);
        content.getStyleClass().add("dialog-content");

        TextField symbolField = new TextField();
        symbolField.setPromptText("Stock Symbol (e.g., AAPL)");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Number of Shares");

        Label balanceLabel = new Label(String.format("Available Balance: $%.2f", user.getBalance()));
        Label symbolInfoLabel = new Label("Enter a valid stock symbol");

        content.getChildren().addAll(
                new Label("Stock Symbol:"),
                symbolField,
                symbolInfoLabel,
                new Label("Quantity:"),
                quantityField,
                balanceLabel
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

        dialog.getDialogPane().setContent(content);

        // Request focus on the symbol field by default
        symbolField.requestFocus();

        // Show the dialog and process the result
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == buyButtonType) {
            try {
                // Get the values from the form
                String symbol = symbolField.getText().toUpperCase();
                int quantity = Integer.parseInt(quantityField.getText());

                // Validate the input
                if (symbol.isEmpty() || quantity <= 0) {
                    showErrorDialog("Invalid Input", "Invalid Input", "Please enter a valid stock symbol and quantity.");
                    return;
                }

                // Check if the symbol is valid
                if (!stockMarket.isValidSymbol(symbol)) {
                    showErrorDialog("Invalid Symbol", "Invalid Stock Symbol", "The stock symbol you entered is not valid.");
                    return;
                }

                // Get stock info
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);

                // Calculate total cost
                double totalCost = price * quantity;

                // Check if user has enough funds
                if (totalCost > user.getBalance()) {
                    showErrorDialog("Insufficient Funds", "Insufficient Funds",
                            String.format("You don't have enough funds to buy %d shares of %s for $%.2f.",
                                    quantity, symbol, totalCost));
                    return;
                }

                // Proceed with the purchase
                // Withdraw the funds
                user.withdraw(totalCost);

                // Add the stock to the portfolio
                OwnedStock stock = new OwnedStock(symbol, name, price, quantity, price);
                user.getPortfolio().addStock(stock);

                // Add a transaction record
                BuyTransaction transaction = new BuyTransaction(symbol, quantity, price, LocalDateTime.now());
                user.addTransaction(transaction);

                // Update the view
                updatePortfolioData();
                updateUserInfo();

                showInfoDialog("Purchase Complete", "Stock Purchased",
                        String.format("Successfully purchased %d shares of %s (%s) for $%.2f.",
                                quantity, name, symbol, totalCost));

            } catch (NumberFormatException e) {
                showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a valid number for the quantity.");
            } catch (IOException e) {
                showErrorDialog("API Error", "Could not complete purchase", "Error accessing stock market data: " + e.getMessage());
            }
        }
    }

    /**
     * Buys more shares of an existing stock
     *
     * @param symbol The stock symbol
     */
    private void buyStock(String symbol) {
        try {
            // Get current price
            double price = stockMarket.getStockPrice(symbol);

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Buy More Shares");
            dialog.setHeaderText("Buy Additional Shares of " + symbol);

            // Set the button types
            ButtonType buyButtonType = new ButtonType("Buy", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(buyButtonType, ButtonType.CANCEL);

            // Create the form fields
            VBox content = new VBox(10);
            content.getStyleClass().add("dialog-content");

            Label priceLabel = new Label(String.format("Current Price: $%.2f", price));
            Label balanceLabel = new Label(String.format("Available Balance: $%.2f", user.getBalance()));

            TextField quantityField = new TextField();
            quantityField.setPromptText("Number of Shares");

            content.getChildren().addAll(
                    priceLabel,
                    new Label("Quantity:"),
                    quantityField,
                    balanceLabel
            );

            dialog.getDialogPane().setContent(content);

            // Request focus on the quantity field by default
            quantityField.requestFocus();

            // Show the dialog and process the result
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == buyButtonType) {
                try {
                    // Get the quantity from the form
                    int quantity = Integer.parseInt(quantityField.getText());

                    // Validate the input
                    if (quantity <= 0) {
                        showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a positive number for the quantity.");
                        return;
                    }

                    // Calculate total cost
                    double totalCost = price * quantity;

                    // Check if user has enough funds
                    if (totalCost > user.getBalance()) {
                        showErrorDialog("Insufficient Funds", "Insufficient Funds",
                                String.format("You don't have enough funds to buy %d shares of %s for $%.2f.",
                                        quantity, symbol, totalCost));
                        return;
                    }

                    // Proceed with the purchase
                    // Withdraw the funds
                    user.withdraw(totalCost);

                    // Get the stock from the portfolio
                    OwnedStock stock = user.getPortfolio().getStock(symbol);

                    // Add the new shares
                    stock.addShares(quantity, price);

                    // Add a transaction record
                    BuyTransaction transaction = new BuyTransaction(symbol, quantity, price, LocalDateTime.now());
                    user.addTransaction(transaction);

                    // Update the view
                    updatePortfolioData();
                    updateUserInfo();

                    showInfoDialog("Purchase Complete", "Stock Purchased",
                            String.format("Successfully purchased %d additional shares of %s for $%.2f.",
                                    quantity, symbol, totalCost));

                } catch (NumberFormatException e) {
                    showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a valid number for the quantity.");
                }
            }
        } catch (IOException e) {
            showErrorDialog("API Error", "Could not complete purchase", "Error accessing stock market data: " + e.getMessage());
        }
    }

    /**
     * Sells shares of an owned stock
     *
     * @param stock The stock to sell
     */
    private void sellStock(OwnedStock stock) {
        try {
            // Get current price
            double price = stockMarket.getStockPrice(stock.getSymbol());

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Sell Shares");
            dialog.setHeaderText("Sell Shares of " + stock.getSymbol());

            // Set the button types
            ButtonType sellButtonType = new ButtonType("Sell", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(sellButtonType, ButtonType.CANCEL);

            // Create the form fields
            VBox content = new VBox(10);
            content.getStyleClass().add("dialog-content");

            Label stockInfoLabel = new Label(String.format("%s (%s)", stock.getName(), stock.getSymbol()));
            Label priceLabel = new Label(String.format("Current Price: $%.2f", price));
            Label ownedSharesLabel = new Label(String.format("Owned Shares: %d", stock.getQuantity()));

            TextField quantityField = new TextField();
            quantityField.setPromptText("Number of Shares to Sell");

            content.getChildren().addAll(
                    stockInfoLabel,
                    priceLabel,
                    ownedSharesLabel,
                    new Label("Quantity:"),
                    quantityField
            );

            dialog.getDialogPane().setContent(content);

            // Request focus on the quantity field by default
            quantityField.requestFocus();

            // Show the dialog and process the result
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == sellButtonType) {
                try {
                    // Get the quantity from the form
                    int quantity = Integer.parseInt(quantityField.getText());

                    // Validate the input
                    if (quantity <= 0) {
                        showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a positive number for the quantity.");
                        return;
                    }

                    if (quantity > stock.getQuantity()) {
                        showErrorDialog("Invalid Quantity", "Too Many Shares",
                                String.format("You only own %d shares of %s.", stock.getQuantity(), stock.getSymbol()));
                        return;
                    }

                    // Calculate total sale value
                    double totalValue = price * quantity;

                    // Calculate profit/loss
                    double profitLoss = (price - stock.getPurchasePrice()) * quantity;

                    // Proceed with the sale
                    // Add the funds to the balance
                    user.deposit(totalValue);

                    // Remove the shares from the portfolio
                    if (quantity == stock.getQuantity()) {
                        // If selling all shares, remove the stock from the portfolio
                        user.getPortfolio().removeStock(stock.getSymbol());
                    } else {
                        // Otherwise, just reduce the quantity
                        stock.removeShares(quantity);
                    }

                    // Add a transaction record
                    SellTransaction transaction = new SellTransaction(stock.getSymbol(), quantity, price, profitLoss, LocalDateTime.now());
                    user.addTransaction(transaction);

                    // Update the view
                    updatePortfolioData();
                    updateUserInfo();

                    String profitLossMessage = profitLoss >= 0
                            ? String.format(" with a profit of $%.2f", profitLoss)
                            : String.format(" with a loss of $%.2f", -profitLoss);

                    showInfoDialog("Sale Complete", "Stock Sold",
                            String.format("Successfully sold %d shares of %s for $%.2f%s.",
                                    quantity, stock.getSymbol(), totalValue, profitLossMessage));

                } catch (NumberFormatException e) {
                    showErrorDialog("Invalid Input", "Invalid Quantity", "Please enter a valid number for the quantity.");
                }
            }
        } catch (IOException e) {
            showErrorDialog("API Error", "Could not complete sale", "Error accessing stock market data: " + e.getMessage());
        }
    }

    /**
     * Shows detailed information for a stock
     *
     * @param stock The stock to show details for
     */
    private void showStockDetailsDialog(OwnedStock stock) {
        // Show the detailed stock view
        mainController.showStockDetails(stock.getSymbol());
    }

    @Override
    public void refreshView() {
        updatePortfolioData();
    }
}