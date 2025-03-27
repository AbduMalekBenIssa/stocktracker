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


}
