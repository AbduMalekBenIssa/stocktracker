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

    /**
     * Updates the portfolio history chart
     * Note: In a real app, we would need to track portfolio value over time
     * For this demo, we'll just use mock data
     */
    private void updatePortfolioHistoryChart() {
        portfolioHistoryChart.getData().clear();

        // Create a series for portfolio value
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Portfolio Value");

        // For this demo, we'll create mock data based on the current portfolio value
        double currentValue = user.getTotalValue();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

        for (int i = 30; i >= 0; i--) {
            LocalDateTime date = now.minusDays(i);
            // Generate a mock value that fluctuates around the current value
            double mockValue = currentValue * (0.9 + Math.random() * 0.2);
            series.getData().add(new XYChart.Data<>(date.format(formatter), mockValue));
        }

        portfolioHistoryChart.getData().add(series);
    }

    /**
     * Updates the top stocks list
     */
    private void updateTopStocks() {
        topStocksContainer.getChildren().clear();

        List<OwnedStock> stocks = user.getPortfolio().getAllStocks();

        if (stocks.isEmpty()) {
            Label noStocksLabel = new Label("No stocks in portfolio");
            noStocksLabel.getStyleClass().add("text-secondary");
            topStocksContainer.getChildren().add(noStocksLabel);
            return;
        }

        // Sort stocks by total value
        stocks.sort((s1, s2) -> Double.compare(s2.getTotalValue(), s1.getTotalValue()));

        // Add top 5 stocks
        int count = Math.min(5, stocks.size());
        for (int i = 0; i < count; i++) {
            OwnedStock stock = stocks.get(i);
            String stockInfo = String.format("%s (%s) - $%.2f (%d shares)",
                    stock.getName(), stock.getSymbol(), stock.getTotalValue(), stock.getQuantity());

            Label stockLabel = new Label(stockInfo);
            stockLabel.getStyleClass().add("dashboard-item");
            topStocksContainer.getChildren().add(stockLabel);
        }
    }
}
