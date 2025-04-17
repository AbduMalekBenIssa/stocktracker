package stocktracker;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import stocktracker.model.BuyTransaction;
import stocktracker.model.SellTransaction;
import stocktracker.model.Transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for the Transaction view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class TransactionController extends BaseController {
    @FXML
    private Label totalTransactionsLabel;

    @FXML
    private Label buyTransactionsLabel;

    @FXML
    private Label sellTransactionsLabel;

    @FXML
    private Label totalVolumeLabel;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> symbolColumn;

    @FXML
    private TableColumn<Transaction, Integer> quantityColumn;

    @FXML
    private TableColumn<Transaction, Double> priceColumn;

    @FXML
    private TableColumn<Transaction, Double> totalColumn;

    @FXML
    private TableColumn<Transaction, Double> profitLossColumn;

    @FXML
    private TableColumn<Transaction, LocalDateTime> dateColumn;

    @FXML
    private ComboBox<String> filterComboBox;

    private ObservableList<Transaction> allTransactions;
    private ObservableList<Transaction> filteredTransactions;

    @Override
    protected void onInitialize() {
        setupTable();
        setupFilter();
        loadTransactions();
    }
    /**
     * Sets up the transaction table columns
     */
    private void setupTable() {
        // Initialize the observable lists
        allTransactions = FXCollections.observableArrayList();
        filteredTransactions = FXCollections.observableArrayList();
        transactionTable.setItems(filteredTransactions);

        // Setup columns
        typeColumn.setCellValueFactory(cellData -> {
            String type = cellData.getValue().getClass().getSimpleName().replace("Transaction", "");
            return new SimpleStringProperty(type);
        });

        symbolColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSymbol()));

        quantityColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getQuantity()).asObject());

        priceColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        priceColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value));
                }
            }
        });
        totalColumn.setCellValueFactory(cellData -> {
            double total = cellData.getValue().getPrice() * cellData.getValue().getQuantity();
            return new SimpleDoubleProperty(total).asObject();
        });
        totalColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", value));
                }
            }
        });

        profitLossColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof SellTransaction) {
                return new SimpleDoubleProperty(((SellTransaction) cellData.getValue()).getProfitLoss()).asObject();
            } else {
                return new SimpleDoubleProperty(0).asObject();
            }
        });
        profitLossColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("$%.2f", value));

                    if (value > 0) {
                        setStyle("-fx-text-fill: -fx-success;");
                    } else if (value < 0) {
                        setStyle("-fx-text-fill: -fx-danger;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTimestamp()));
        dateColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            @Override
            protected void updateItem(LocalDateTime value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(formatter.format(value));
                }
            }
        });

        // Set up sorting
        transactionTable.getSortOrder().add(dateColumn);
        dateColumn.setSortType(TableColumn.SortType.DESCENDING);
    }
    /**
     * Sets up the filter combo box
     */
    private void setupFilter() {
        // Add filter options
        filterComboBox.getItems().addAll("All Transactions", "Buy Transactions", "Sell Transactions");
        filterComboBox.getSelectionModel().selectFirst();

        // Add listener for filter changes
        filterComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            applyFilter(newValue);
        });
    }
    /**
     * Loads all transactions from the user
     */
    private void loadTransactions() {
        // Get all transactions from the user
        List<Transaction> transactions = user.getTransactions();

        // Sort by date (newest first)
        transactions.sort(Comparator.comparing(Transaction::getTimestamp).reversed());

        // Add to observable list
        allTransactions.clear();
        allTransactions.addAll(transactions);

        // Apply filter
        applyFilter(filterComboBox.getSelectionModel().getSelectedItem());

        // Update summary information
        updateSummary();
    }
}