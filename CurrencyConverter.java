import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {

    private static final String API_KEY = "YOUR_API_KEY"; // Replace with your actual API key
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt user for base currency and amount
        System.out.print("Enter the base currency (e.g., USD, EUR): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter the amount in " + baseCurrency + ": ");
        double amount = scanner.nextDouble();

        // Prompt user for target currency
        System.out.print("Enter the target currency (e.g., USD, EUR): ");
        String targetCurrency = scanner.next().toUpperCase();

        // Fetch and display the converted amount
        try {
            double convertedAmount = convertCurrency(baseCurrency, targetCurrency, amount);
            System.out.printf("%.2f %s is equal to %.2f %s%n", amount, baseCurrency, convertedAmount, targetCurrency);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    private static double convertCurrency(String baseCurrency, String targetCurrency, double amount) throws Exception {
        String urlString = API_URL + baseCurrency;

        // Create a URL object
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Check the response code
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
        }

        // Read the response
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        connection.disconnect();

        // Parse the JSON response
        JSONObject jsonResponse = new JSONObject(response.toString());
        JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

        // Get the exchange rate for the target currency
        double exchangeRate = conversionRates.getDouble(targetCurrency);

        // Convert the amount
        return amount * exchangeRate;
    }
}