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

    /**
     * Updates the summary values
     */
    private void updateSummaryValues() {
        double balance = user.getBalance();
        double portfolioValue = user.getPortfolio().getTotalValue();
        double totalValue = balance + portfolioValue;
        double profitLoss = user.getPortfolio().getTotalProfitLoss();

        balanceLabel.setText(String.format("$%.2f", balance));
        portfolioValueLabel.setText(String.format("$%.2f", portfolioValue));
        totalValueLabel.setText(String.format("$%.2f", totalValue));

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
     * Updates the asset allocation chart
     */
    private void updateAssetAllocationChart() {
        assetAllocationChart.getData().clear();

        double balance = user.getBalance();
        double portfolioValue = user.getPortfolio().getTotalValue();

        // Add data to chart
        if (portfolioValue > 0 || balance > 0) {
            assetAllocationChart.getData().add(new PieChart.Data("Cash", balance));

            // Get stock allocation
            Map<String, Double> stockAllocation = new HashMap<>();
            List<OwnedStock> stocks = user.getPortfolio().getAllStocks();

            for (OwnedStock stock : stocks) {
                stockAllocation.put(stock.getSymbol(), stock.getTotalValue());
            }

            // If there are many stocks, group smaller ones into "Other"
            if (stockAllocation.size() > 5) {
                // Sort stocks by value
                List<Map.Entry<String, Double>> sortedStocks = stockAllocation.entrySet()
                        .stream()
                        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                        .toList();

                // Add top 4 stocks
                for (int i = 0; i < 4 && i < sortedStocks.size(); i++) {
                    Map.Entry<String, Double> entry = sortedStocks.get(i);
                    assetAllocationChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
                }

                // Group the rest as "Other"
                double otherValue = 0;
                for (int i = 4; i < sortedStocks.size(); i++) {
                    otherValue += sortedStocks.get(i).getValue();
                }

                if (otherValue > 0) {
                    assetAllocationChart.getData().add(new PieChart.Data("Other", otherValue));
                }
            } else {
                // Add all stocks
                for (Map.Entry<String, Double> entry : stockAllocation.entrySet()) {
                    assetAllocationChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
                }
            }
        } else {
            // If there are no assets, show a placeholder
            assetAllocationChart.getData().add(new PieChart.Data("No Assets", 1));
        }
    }

}
