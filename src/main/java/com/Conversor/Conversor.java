package com.Conversor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Conversor {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/c23dcc62189aa52e9032e404/latest/USD";
    private static final HttpClient client = HttpClient.newHttpClient();

    //Available currencies
    protected static final String[] CURRENCIES = {"USD", "BRL", "EUR", "AUD", "GBP", "ARS", "OTHER"};

    //Get available currencies
    protected static String[] getCurrencies() {
        return CURRENCIES;
    }

    //Convert currency
    protected static double convertCurrency(String fromCurrency, String toCurrency, double amount) {
        double exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        if (exchangeRate == -1) {
            return -1;
        }
        return amount * exchangeRate;
    }

    //Get exchange rate
    private static double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(API_URL)).header("Accept", "application/json").GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            JsonObject conversionRates = json.getAsJsonObject("conversion_rates");
            double fromRate = conversionRates.get(fromCurrency).getAsDouble();
            double toRate = conversionRates.get(toCurrency).getAsDouble();
            return toRate / fromRate;
        } catch (Exception e) {
            return -1;
        }
    }
}
