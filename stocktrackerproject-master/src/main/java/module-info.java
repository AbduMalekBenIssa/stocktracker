module StockTrackerProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Other required modules based on dependencies
    requires com.fasterxml.jackson.databind;
    requires okhttp3;
    requires org.slf4j;
    requires com.google.gson;
    requires org.junit.jupiter.api;

    opens stocktracker to javafx.fxml, javafx.graphics;

    exports stocktracker;
}