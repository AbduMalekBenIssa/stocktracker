package stocktracker;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles API requests to Financial Modeling Prep (FMP) to fetch real-time stock data.
 * It provides methods to retrieve stock prices, daily price changes, top gainers/losers, and most actively traded stocks.
 *
 * @author Omar Almishri, AbduMalek Ben Issa
 * @version 1.0
 * @Tutorial T04
 */
public class StockDataFetcher {
    private static final String API_KEY = "TgDs7BqSg954lxHfiIjBwT8JLk2mi1Dg"; // API Key for FMP
    private static final String API_URL = "https://financialmodelingprep.com/api/v3/";
    private static final OkHttpClient httpClient = new OkHttpClient(); // HTTP client for API requests

    /**
     * Fetches the real-time stock price for a given symbol.
     *
     * @param symbol The stock ticker symbol (e.g., "AAPL").
     * @return The current price of the stock.
     * @throws IOException If there is an issue with the API request.
     */
    public static double getStockPrice(String symbol) throws IOException {
        String url = API_URL + "quote/" + symbol + "?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);

            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
            if (jsonArray.isEmpty()) throw new IOException("Stock symbol not found: " + symbol);

            JsonObject stockObject = jsonArray.get(0).getAsJsonObject();
            return stockObject.get("price").getAsDouble(); // Extracts price from API response
        }
    }

    /**
     * Retrieves the daily percentage change in stock price.
     *
     * @param symbol The stock ticker symbol.
     * @return The daily percentage change in stock price.
     * @throws IOException If the API request fails.
     */
    public static double getDailyChangePercentage(String symbol) throws IOException {
        String url = API_URL + "quote/" + symbol + "?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);

            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
            if (jsonArray.isEmpty()) throw new IOException("Stock symbol not found: " + symbol);

            JsonObject stockObject = jsonArray.get(0).getAsJsonObject();
            return stockObject.get("changesPercentage").getAsDouble(); // Extracts percentage change
        }
    }

    /**
     * Retrieves the top 5 gainers in the market today.
     *
     * @return A list of formatted strings containing the top 5 gainers.
     * @throws IOException If the API request fails.
     */
    public static List<String> getTopGainers() throws IOException {
        String url = API_URL + "stock_market/gainers?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> topGainers = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);

            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                String symbol = stock.get("symbol").getAsString();
                double price = stock.get("price").getAsDouble();
                double changePercentage = stock.get("changesPercentage").getAsDouble();
                topGainers.add(String.format("%s - Price: $%.2f, Change: %.2f%%", symbol, price, changePercentage));
            }
        }
        return topGainers;
    }

    /**
     * Retrieves the top 5 losers in the market today.
     *
     * @return A list of formatted strings containing the top 5 losers.
     * @throws IOException If the API request fails.
     */
    public static List<String> getTopLosers() throws IOException {
        String url = API_URL + "stock_market/losers?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> topLosers = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);

            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                String symbol = stock.get("symbol").getAsString();
                double price = stock.get("price").getAsDouble();
                double changePercentage = stock.get("changesPercentage").getAsDouble();
                topLosers.add(String.format("%s - Price: $%.2f, Change: %.2f%%", symbol, price, changePercentage));
            }
        }
        return topLosers;
    }

    /**
     * Retrieves the top 5 most actively traded stocks in the market today.
     *
     * @return A list of formatted strings containing the most actively traded stocks.
     * @throws IOException If the API request fails.
     */
    public static List<String> getMostActivelyTraded() throws IOException {
        String url = "https://financialmodelingprep.com/stable/most-actives?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> mostActiveStocks = new ArrayList<>();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);

            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();

            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                String symbol = stock.get("symbol").getAsString();
                String name = stock.get("name").getAsString();
                double price = stock.get("price").getAsDouble();
                double change = stock.get("change").getAsDouble();
                double changePercentage = stock.get("changesPercentage").getAsDouble();
                String exchange = stock.get("exchange").getAsString();

                mostActiveStocks.add(String.format(
                        "%s (%s) | Exchange: %s | Price: $%.2f | Change: %.2f (%.2f%%)",
                        name, symbol, exchange, price, change, changePercentage
                ));
            }
        }
        return mostActiveStocks;
    }

}
