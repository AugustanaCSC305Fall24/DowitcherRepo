package org.example.data;

import org.example.App;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class VertexWebScrapper {

    // Use the API URL for your specific endpoint
    private static final String API_URL = "https://us-central1-aiplatform.googleapis.com/v1/projects/dowitchercwbot/locations/us-central1/endpoints/2951881956834410496:predict";

    // Use the API Key directly in the request
    private static final String API_KEY = "AIzaSyAPvZIak-euYn7sZnkOFpMDZs39YgqQu5k";  // Example API Key

    public static String getChatBotResponse(String userMessage) {

        try {
            URL url = new URL(API_URL + "?key=" + API_KEY);  // Append the API key to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send the request body with user message
            String requestBody = String.format("{\"instances\":[{\"userMessage\":\"%s\"}]}", userMessage);
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response and return
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }
            return response.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: Could not retrieve chatbot response.";
        }
    }
}
