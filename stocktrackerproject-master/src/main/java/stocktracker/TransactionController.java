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
}