# StockSnap - Portfolio Management System

## Project Overview

StockSnap is a comprehensive desktop application designed to help users track and manage their stock portfolios, monitor market trends, and view their transaction history. This application was developed as a custom class project with the help of Omar Almishri.

It provides a user-friendly interface built with JavaFX, offering features for both novice and experienced investors to stay informed about their investments.

## Key Features

*   **Dashboard:** Provides a quick overview of your current balance, total portfolio value, and recent activities.
*   **Portfolio Management:** View your current stock holdings, including quantity, average cost, current value, and overall gain/loss.
*   **Watchlist:** Keep track of stocks you are interested in but do not currently own.
*   **Market View:** Browse and search for stocks, view current market prices, and access detailed stock information.
*   **Stock Details:** In-depth view for individual stocks, including price charts (if implemented), key statistics, latest news, and buy/sell options.
*   **Transaction History:** Log and review all your past buy and sell transactions.
*   **Settings:** Customize application settings, including theme preferences (e.g., Dark Mode) and potentially user profile details.
*   **Real-time Data (Conceptual):** Designed to integrate with stock market APIs for up-to-date pricing and news (API key management required).
*   **User Management:** Secure login and management of user data (balance, portfolio, etc.).

## Technology Stack

*   **Language:** Java
*   **Framework:** JavaFX (for the Graphical User Interface)
*   **Build Tool:** Apache Maven (likely, based on standard Java project structure)
*   **Styling:** CSS (for customizing the JavaFX UI appearance)

## Setup and Running the Application

1.  **Prerequisites:**
    *   Java Development Kit (JDK) - Version 11 or higher recommended.
    *   Apache Maven.
2.  **Clone the Repository:**
    ```bash
    git clone <repository-url>
    cd stocktrackerproject-master
    ```
3.  **Build the Project:**
    *   Open a terminal or command prompt in the project's root directory (`stocktrackerproject-master`).
    *   Run the Maven build command:
        ```bash
        mvn clean package
        ```
4.  **Run the Application:**
    *   After a successful build, you can run the application using the Maven exec plugin or by directly executing the generated JAR file (configuration might vary based on `pom.xml`):
        ```bash
        # Example using Maven exec plugin (if configured)
        mvn javafx:run 
        
        # Or running the JAR (check target directory and pom.xml for specifics)
        # java -jar target/stocktracker-*.jar 
        ```

## Important Note on API Keys

If this application integrates with external stock market APIs (like Alpha Vantage, Finnhub, etc.), it likely requires an API key. 

**DO NOT commit your API key directly into the codebase or version control (like Git).**

*   **Best Practice:** Use environment variables, configuration files (added to `.gitignore`), or a secure secrets management system to handle API keys.
*   **Before Committing:** Double-check your code (especially configuration files or constants) to ensure no sensitive information like API keys is included.

## Acknowledgements

This project was developed as a custom class project. Special thanks to **Omar Almishri** for the collaboration and contributions.

