package stocktracker;

import stocktracker.model.User;
import stocktracker.service.StockMarket;

/**
 * Base controller class that other view controllers will inherit from
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 3.0
 * @Tutorial T04
 */

public abstract class BaseController {

    protected User user;
    protected StockMarket stockMarket;
    protected MainViewController mainController;

    /**
     * Initializes the controller with user, stock market service, and main controller
     *
     * @param user The user
     * @param stockMarket The stock market service
     * @param mainController The main view controller
     */
    public void initialize(User user, StockMarket stockMarket, MainViewController mainController) {
        this.user = user;
        this.stockMarket = stockMarket;
        this.mainController = mainController;

        // Call the subclass-specific initialization
        onInitialize();
    }

    /**
     * Refreshes the UI with the latest data
     * This method should be implemented by subclasses to update their views
     */
    public void refreshView() {
        // Default implementation does nothing
        // Subclasses should override this method to update their views
    }

}
