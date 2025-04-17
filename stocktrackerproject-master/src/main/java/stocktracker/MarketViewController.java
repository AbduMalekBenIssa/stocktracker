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

}
