package stocktracker;

import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.application.Platform;

import stocktracker.BaseController;
import stocktracker.MainViewController;
import stocktracker.model.User;
import stocktracker.service.StockMarket;
import stocktracker.model.*;
import stocktracker.model.Stock;
import stocktracker.model.WatchlistStock;
import stocktracker.model.OwnedStock;
import stocktracker.model.BuyTransaction;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class StockDetailController extends BaseController {

    @FXML
    private Label symbolLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label changeLabel;

    @FXML
    private AreaChart<Number, Number> priceChart;

    @FXML
    private NumberAxis xAxis;

    @FXML
    private NumberAxis yAxis;
