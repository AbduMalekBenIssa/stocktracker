package stocktracker.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class FinancialModelPrepAPI implements StockMarket {
    private static final String API_KEY = "TgDs7BqSg954lxHfiIjBwT8JLk2mi1Dg"; // Our API key (dont use blease :()
    private static final String API_URL = "https://financialmodelingprep.com/api/v3/";
    private static final OkHttpClient httpClient = new OkHttpClient(); // HTTP client for API requests

    // Cache for company names to reduce API calls
    private final Map<String, String> companyNameCache;

    /**
     * Constructor for the FinancialModelPrepAPI class
     */
    public FinancialModelPrepAPI() {
        this.companyNameCache = new HashMap<>();
    }

    /**
     * Gets the current price of a stock
     */
    @Override
    public double getStockPrice(String symbol) throws IOException {
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

    @Override
    public double getDailyChangePercentage(String symbol) throws IOException {
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
     * Gets the name of a company by its stock symbol
     */
    @Override
    public String getCompanyName(String symbol) throws IOException {
        // Check cache first
        if (companyNameCache.containsKey(symbol)) {
            return companyNameCache.get(symbol);
        }
        String url = API_URL + "profile/" + symbol + "?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();


        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);


            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();
            if (jsonArray.isEmpty()) throw new IOException("Stock symbol not found: " + symbol);


            JsonObject profileData = jsonArray.get(0).getAsJsonObject();
            String name = profileData.get("companyName").getAsString();

            // Cache the result
            companyNameCache.put(symbol, name);

            return name;
        }
    }

    /**
     * Gets the top gainers in the market
     */
    @Override
    public List<String> getTopGainers() throws IOException {
        String url = API_URL + "stock_market/gainers?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> symbols = new ArrayList<>();


        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);


            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();


            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                symbols.add(stock.get("symbol").getAsString());
            }
        }
        return symbols;
    }

    /**
     * Gets the top losers in the market
     */
    @Override
    public List<String> getTopLosers() throws IOException {
        String url = API_URL + "stock_market/losers?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> symbols = new ArrayList<>();


        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);


            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();


            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                symbols.add(stock.get("symbol").getAsString());
            }
        }
        return symbols;
    }

    /**
     * Gets the most actively traded stocks
     */
    @Override
    public List<String> getMostActivelyTraded() throws IOException {
        // Note the URL is different from the others according to your original code
        String url = "https://financialmodelingprep.com/stable/most-actives?apikey=" + API_KEY;
        Request request = new Request.Builder().url(url).build();
        List<String> symbols = new ArrayList<>();


        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected API response: " + response);


            String jsonData = response.body().string();
            JsonArray jsonArray = JsonParser.parseString(jsonData).getAsJsonArray();


            for (int i = 0; i < Math.min(5, jsonArray.size()); i++) {
                JsonObject stock = jsonArray.get(i).getAsJsonObject();
                symbols.add(stock.get("symbol").getAsString());
            }
        }
        return symbols;
    }

    /**
     * Validates if a stock symbol exists
     */
    @Override
    public boolean isValidSymbol(String symbol) throws IOException {
        try {
            getStockPrice(symbol);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
