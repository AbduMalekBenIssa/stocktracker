package stocktracker.model;

import javafx.beans.property.*;

/**
 * Model class for application settings.
 */
public class Settings {

    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty currency = new SimpleStringProperty();

    // Default constructor - values will be set by SettingsService
    public Settings() {
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public String getCurrency() {
        return currency.get();
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }
}