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
}
