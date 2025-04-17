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
}