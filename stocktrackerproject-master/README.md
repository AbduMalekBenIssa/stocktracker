# StockSnap - Stock Portfolio Management System

## Authors

*   Omar Almishri (omar.almishri56@gmail.com)
*   AbduMalek Ben Issa (abdumalek.benissa@gmail.com)

## Program Description

StockSnap is a JavaFX desktop application designed to help users manage and track their stock investments. It allows users to:

*   Monitor their portfolio value and performance.
*   Maintain a watchlist of stocks they are interested in.
*   View detailed information about specific stocks (fetched via Financial Modeling Prep API).
*   Record buy and sell transactions.
*   Analyze portfolio allocation and market trends (top gainers, losers, most active).
*   Save and load user data (portfolio, watchlist, transactions, balance).
*   Customize settings like username.

## Class Information

The project follows a Model-View-Controller (MVC) pattern enhanced with services and utility classes:

*   **`stocktracker` (Root Package):** Contains the main application class (`StockTrackerGUI`), the launcher (`Launcher`), FXML controllers for the main views (`MainViewController`, `DashboardController`, `PortfolioController`, `WatchlistController`, `TransactionController`, `MarketViewController`, `StockDetailController`, `SettingsController`), and the base controller (`BaseController`).
*   **`stocktracker.model`:** Defines the core data structures (POJOs) like `User`, `Stock`, `Portfolio`, `Watchlist`, `Transaction`, `OwnedStock`, `WatchlistStock`, etc.
*   **`stocktracker.models`:** Contains the `Settings` data model.
*   **`stocktracker.service`:** Includes the `StockMarket` interface and its implementation `FinancialModelPrepAPI` for fetching real-time market data.
*   **`stocktracker.services`:** Contains the `SettingsService` for managing application settings.
*   **`stocktracker.analysis`:** Provides classes like `PortfolioAnalyzer` and `MarketAnalyzer` for processing and presenting data insights.
*   **`stocktracker.util`:** Contains utility classes, notably `FileManager` for handling data persistence (saving/loading user data).
*   **`src/main/resources/stocktracker/views`:** Contains the FXML files defining the GUI layout.
*   **`src/main/resources/stocktracker/css`:** Contains the CSS file (`style.css`) for styling the application.

## Running the Application (Executable JAR)

1.  **Prerequisites:** Make sure you have Java Runtime Environment (JRE) version 23 or later installed on your system.
2.  **Build (if necessary):** If you haven't built the JAR file yet, navigate to the `stocktrackerproject-master` directory in your terminal and run the Maven command:
    ```bash
    mvn clean package
    ```
    (Ensure Maven is installed and configured in your PATH).
3.  **Locate the JAR:** The executable JAR file will be created in the `target/` subdirectory. It will likely be named `StockTrackerProject-1.0-SNAPSHOT.jar` (the version might differ).
4.  **Execute:** Open a terminal or command prompt, navigate to the `stocktrackerproject-master/target/` directory, and run the JAR file using the following command:

    ```bash
    java -jar StockTrackerProject-1.0-SNAPSHOT.jar
    ```
    (Replace `StockTrackerProject-1.0-SNAPSHOT.jar` with the actual name of the generated JAR file if it's different).

The StockSnap application window should then launch. 