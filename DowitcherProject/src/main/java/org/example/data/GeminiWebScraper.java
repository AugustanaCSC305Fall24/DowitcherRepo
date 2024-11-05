package org.example.data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GeminiWebScraper {

    private static final String API_URL = "https://example.com/gemini-chat-api"; // Replace with actual API endpoint
    private final Gson gson;

    public GeminiWebScraper() {
        this.gson = new Gson();
    }

    public String getChatBotResponse(String userMessage) {
        ChatRequest request = new ChatRequest(userMessage);
        String requestBody = gson.toJson(request);

        try {
            // Set up the connection to the API
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Send the JSON request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read the response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // Deserialize the JSON response
            ChatResponse chatResponse = gson.fromJson(response.toString(), ChatResponse.class);

            // Return the bot's response message if available
            return chatResponse != null && chatResponse.getBotMessage() != null
                    ? chatResponse.getBotMessage()
                    : "No response from chatbot.";

        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing response: " + e.getMessage());
            return "Error: Invalid JSON format from chatbot response.";
        } catch (IOException e) {
            System.out.println("Error sending request: " + e.getMessage());
            return "Error: Could not retrieve chatbot response.";
        }
    }
}
