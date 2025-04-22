# 📈 **StockSnap – Stock Portfolio Management System**

## CPSC 233 – Final Project (Winter 2025)

### Submitted by: Omar Almishri & AbduMalek Ben Issa

### **Overview**

StockSnap is a JavaFX-based desktop application designed to help users manage their personal stock portfolios. This application enables users to:
Track owned stocks and total portfolio value
Record and view buy/sell transactions
Monitor real-time stock data via the Financial Modeling Prep API
Maintain a watchlist (in case you are unsure of putting money in)
Visualize portfolio performance and market trends
Save/load user data to/from file
Customize application settings (e.g., username)


This app follows Object Oriented principles and is organized into clearly defined packages for model, controller, service, analysis, and utility components.  It also follows using Model-View-Controller (MVC) style for the GUI layout, and is handled through FXML and styled with a CSS file.

### **Package Structure**


stocktracker - Main application launcher and all FXML controllers 

stocktracker.model - Core business models (e.g., User, Stock, Transaction, Portfolio, Watchlist, Settings)

stocktracker.service - Stock data fetching service (uses HTTP API, handles Stock Market Information, Settings Management)

stocktracker.analysis - Classes for analyzing stock/portfolio trends

stocktracker.util - File persistence utilities

resources/stocktracker/views/ - FXML files for each view  || resources/stocktracker/css/ - Styling (style.css)


### **Technologies Used**

Java 21 (compatible with Java 23+)
JavaFX (GUI)
Maven (build tool for dependencies/modules like the API, etc.)
FXML (layout)
CSS (UI styling)
API/HTTP Requests (via FMP API + OKHttp3 module)
JSON Parsing (using JSON module)
File I/O (for save/load user state)


### **Running the Application**

_Prerequisites_

JDK 21+

JavaFX SDK (for local execution or rebuilding)

If rebuilding: Maven must be installed and configured


### **To Run (via JAR file)**

From terminal/command prompt, run this in the Terminal/Git Bash within the directory where the file is located after downloading.

####  *_java -jar StockTrackerProject-1.0-SNAPSHOT.jar_*

The .jar file also exists inside the /target/ folder.


### **GitLab Repository**

https://csgit.ucalgary.ca/omar.almishri/stocktrackerprojectdemo3

### **Contact**

Omar Almishri — omar.almishri@ucalgary.ca 

AbduMalek Ben Issa — abdumalek.benissa@ucalgary.ca



Please reach out if any file is unreadable or you require clarification.

