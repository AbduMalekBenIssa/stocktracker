package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import stocktracker.analysis.MarketAnalyzer;
import stocktracker.model.WatchlistStock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the Market view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class MarketViewController extends BaseController {

    @FXML
    private TableView<MarketStock> gainersTable;

    @FXML
    private TableView<MarketStock> losersTable;

    @FXML
    private TableView<MarketStock> activeTable;

    @FXML
    private TableColumn<MarketStock, String> gainerSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> gainerNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> gainerPriceColumn;

    @FXML
    private TableColumn<MarketStock, Number> gainerChangeColumn;

    @FXML
    private TableColumn<MarketStock, String> loserSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> loserNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> loserPriceColumn;

    @FXML
    private TableColumn<MarketStock, Number> loserChangeColumn;

    @FXML
    private TableColumn<MarketStock, String> activeSymbolColumn;

    @FXML
    private TableColumn<MarketStock, String> activeNameColumn;

    @FXML
    private TableColumn<MarketStock, Number> activePriceColumn;

    @FXML
    private VBox marketMovementsContainer;

    private MarketAnalyzer marketAnalyzer;
    private ObservableList<MarketStock> gainersData;
    private ObservableList<MarketStock> losersData;
    private ObservableList<MarketStock> activeData;

    /**
     * Helper class to store market stock data for tables
     */
    public static class MarketStock {
        private final String symbol;
        private final String name;
        private final double price;
        private final double change;

        public MarketStock(String symbol, String name, double price, double change) {
            this.symbol = symbol;
            this.name = name;
            this.price = price;
            this.change = change;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public double getChange() {
            return change;
        }
    }

    @Override
    protected void onInitialize() {
        this.marketAnalyzer = new MarketAnalyzer(stockMarket);

        setupTables();
        loadMarketData();
    }

    /**
     * Sets up the table columns and data binding
     */
    private void setupTables() {
        // Initialize data lists
        gainersData = FXCollections.observableArrayList();
        losersData = FXCollections.observableArrayList();
        activeData = FXCollections.observableArrayList();

        // Set data sources
        gainersTable.setItems(gainersData);
        losersTable.setItems(losersData);
        activeTable.setItems(activeData);

        // Setup gainers table columns
        setupTableColumns(
                gainerSymbolColumn, gainerNameColumn, gainerPriceColumn, gainerChangeColumn, true);

        // Setup losers table columns
        setupTableColumns(
                loserSymbolColumn, loserNameColumn, loserPriceColumn, loserChangeColumn, false);

        // Setup active table columns
        setupActiveTableColumns(
                activeSymbolColumn, activeNameColumn, activePriceColumn);

        // Set row factories for context menus
        setupRowFactory(gainersTable);
        setupRowFactory(losersTable);
        setupRowFactory(activeTable);
    }

    /**
     * Sets up the standard table columns (symbol, name, price, change)
     *
     * @param symbolColumn The symbol column
     * @param nameColumn The name column
     * @param priceColumn The price column
     * @param changeColumn The change column
     * @param isPositiveChange Whether the change is expected to be positive
     */
    private void setupTableColumns(
            TableColumn<MarketStock, String> symbolColumn,
            TableColumn<MarketStock, String> nameColumn,
            TableColumn<MarketStock, Number> priceColumn,
            TableColumn<MarketStock, Number> changeColumn,
            boolean isPositiveChange) {

        // Symbol column
        symbolColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        // Name column
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        // Price column with currency formatting
        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));
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

        // Change column with formatting and colors
        changeColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getChange()));
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
    }

    /**
     * Sets up the active stocks table columns (no change column)
     *
     * @param symbolColumn The symbol column
     * @param nameColumn The name column
     * @param priceColumn The price column
     */
    private void setupActiveTableColumns(
            TableColumn<MarketStock, String> symbolColumn,
            TableColumn<MarketStock, String> nameColumn,
            TableColumn<MarketStock, Number> priceColumn) {

        // Symbol column
        symbolColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getSymbol()));

        // Name column
        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getName()));

        // Price column with currency formatting
        priceColumn.setCellValueFactory(data ->
                new SimpleDoubleProperty(data.getValue().getPrice()));
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
    }

    /**
     * Sets up the row factory for a table to handle context menus and double clicks
     *
     * @param table The table to set up
     */
    private void setupRowFactory(TableView<MarketStock> table) {
        table.setRowFactory(tv -> {
            TableRow<MarketStock> row = new TableRow<>();

            // Handle double click
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    showStockDetails(row.getItem());
                }
            });

            // Create context menu
            ContextMenu contextMenu = new ContextMenu();

            MenuItem addToWatchlistItem = new MenuItem("Add to Watchlist");
            addToWatchlistItem.setOnAction(e -> addToWatchlist(row.getItem()));

            MenuItem buyStockItem = new MenuItem("Buy Stock");
            buyStockItem.setOnAction(e -> buyStock(row.getItem()));

            MenuItem viewDetailsItem = new MenuItem("View Details");
            viewDetailsItem.setOnAction(e -> showStockDetails(row.getItem()));

            contextMenu.getItems().addAll(addToWatchlistItem, buyStockItem, new SeparatorMenuItem(), viewDetailsItem);

            // Only display for non-empty rows
            row.contextMenuProperty().bind(
                    javafx.beans.binding.Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });
    }

    /**
     * Loads market data from the API
     */
    private void loadMarketData() {
        try {
            // Clear existing data
            gainersData.clear();
            losersData.clear();
            activeData.clear();
            marketMovementsContainer.getChildren().clear();

            // Get market data
            loadTopGainers();
            loadTopLosers();
            loadMostActive();
            showMarketMovements();

        } catch (IOException e) {
            showErrorDialog("Market Data Error", "Failed to load market data", e.getMessage());
        }
    }

    /**
     * Loads the top gainers data
     */
    private void loadTopGainers() throws IOException {
        List<String> gainers = stockMarket.getTopGainers();

        for (String symbol : gainers) {
            try {
                String name = stockMarket.getCompanyName(symbol);
                double price = stockMarket.getStockPrice(symbol);
                double change = stockMarket.getDailyChangePercentage(symbol);

                gainersData.add(new MarketStock(symbol, name, price, change));
            } catch (IOException e) {
                // Skip this stock if there's an error
                System.out.println("Error loading gainer: " + symbol + " - " + e.getMessage());
            }
        }
    }

}
