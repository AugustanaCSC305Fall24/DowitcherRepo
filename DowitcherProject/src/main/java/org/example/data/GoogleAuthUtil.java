package org.example.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleAuthUtil {

    private static final String API_KEY = "AIzaSyAPvZIak-euYn7sZnkOFpMDZs39YgqQu5k";

    // Method to make API requests using the API key
    public static String makeApiRequest(String endpoint) throws Exception {
        // Create the full URL with the API key
        String urlString = endpoint + "?key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        // Handle response errors
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("Failed to fetch data from Google API: HTTP request failed with response code: " + responseCode);
        }

        // Read the response
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    // Example: Fetching JSON data from Google API
    public static JsonObject fetchJsonFromApi(String endpoint) throws Exception {
        String response = makeApiRequest(endpoint);
        JsonParser parser = new JsonParser();
        return parser.parse(response).getAsJsonObject();
    }
}
