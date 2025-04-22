package stocktracker;

/**
 * Launcher class to work around JavaFX module issues in shaded JARs.
 */
public class Launcher {
    public static void main(String[] args) {
        StockTrackerGUI.main(args);
    }
} 