package stocktracker;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import stocktracker.analysis.PortfolioAnalyzer;
import stocktracker.model.OwnedStock;
import stocktracker.model.Transaction;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for the Dashboard view
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */
public class DashboardController extends BaseController {

    @FXML
    private Label balanceLabel;

    @FXML
    private Label portfolioValueLabel;

    @FXML
    private Label totalValueLabel;

    @FXML
    private Label profitLossLabel;

    @FXML
    private PieChart assetAllocationChart;

    @FXML
    private LineChart<String, Number> portfolioHistoryChart;

    @FXML
    private VBox topStocksContainer;

    @FXML
    private VBox recentTransactionsContainer;

    private PortfolioAnalyzer portfolioAnalyzer;

    @Override
    protected void onInitialize() {
        this.portfolioAnalyzer = new PortfolioAnalyzer(user, stockMarket);

        // Update dashboard data
        updateDashboardData();
    }

    /**
     * Updates all dashboard data
     */
    private void updateDashboardData() {
        // Update summary values
        updateSummaryValues();

        // Update charts
        updateAssetAllocationChart();
        updatePortfolioHistoryChart();

        // Update lists
        updateTopStocks();
        updateRecentTransactions();
    }

}
