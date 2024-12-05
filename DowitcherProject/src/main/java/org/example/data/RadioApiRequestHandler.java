package org.example.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RadioApiRequestHandler {

    private String apiKey = "AIzaSyAifj81HJ-xRqkKi-izzQ-k_FfwgcTR8F4";  // Your API key
    private String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

    // Method to send a request to the API
    public String sendRequest(String prompt) throws IOException {
        // Prepare the JSON body using the expected format
        Map<String, Object> body = new HashMap<>();

        // Add contents to the request body
        List<Map<String, Object>> contents = new ArrayList<>();

        // User input as the first content
        Map<String, Object> userInputPart = new HashMap<>();
        userInputPart.put("text", prompt);  // User's input
        contents.add(createContent("user", userInputPart));

        body.put("contents", contents);

        // Create an ObjectMapper to convert the Java object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(body);

        // Create the connection
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // Write the JSON payload to the request body
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonPayload.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Get the response code
        int responseCode = connection.getResponseCode();

        // Handle the response
        String responseMessage = "";
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read and print the response from the API (the bot's response)
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Extract and print the "text" part of the response
                responseMessage = extractTextFromResponse(response.toString());
            }
        } else {
            // Handle error response
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                String inputLine;
                StringBuilder errorResponse = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                responseMessage = "Error Response: " + errorResponse.toString();
            }
        }

        connection.disconnect();
        return responseMessage;
    }


    // Helper method to structure content parts
    private Map<String, Object> createContent(String role, Map<String, Object> part) {
        Map<String, Object> content = new HashMap<>();
        content.put("role", role);
        content.put("parts", Collections.singletonList(part));
        return content;
    }

    // Method to extract and return the "text" part from the JSON response
    private String extractTextFromResponse(String responseJson) {
        try {
            // Parse the JSON response
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);

            // Get the candidates array
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseMap.get("candidates");

            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> firstCandidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");

                if (parts != null && !parts.isEmpty()) {
                    Map<String, Object> firstPart = parts.get(0);
                    String text = (String) firstPart.get("text");

                    // Return the text part
                    return text;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Error extracting response text.";
    }
}
