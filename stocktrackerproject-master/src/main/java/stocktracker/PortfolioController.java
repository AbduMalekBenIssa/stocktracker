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
}
