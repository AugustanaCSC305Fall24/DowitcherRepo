package org.example.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class RadioApiRequestHandler {

    private String apiKey = "AIzaSyAifj81HJ-xRqkKi-izzQ-k_FfwgcTR8F4";  // Your API key
    private String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

    // Define the context for the request, including instruction to avoid \n
    private String context = "You are a ham radio operator participating in the Parks on the Waves program, "
            + "where operators earn badges by making contact with others in unique parks across the USA. "
            + "You’re using Continuous Wave (CW) Morse code to communicate, which involves short, simple interactions due to the nature of Morse code. "
            + "Communication Style: Respond as a friendly ham radio enthusiast who enjoys quick, casual exchanges. "
            + "Keep messages brief and straightforward, using simple English that’s easy to translate into Morse code. "
            + "These interactions are conversational, acknowledging the other operator’s location and signal quality, and include any necessary codes for clarity. "
            + "Do not include newline characters or unnecessary breaks in the message. Keep everything on a single line."
            + "Even if you are spoken to in normal English, always respond in CW.";

    // Method to send a request to the API
    public String sendRequest(String userInput) throws IOException {
        // Prepare the JSON body using Jackson
        Map<String, Object> body = new HashMap<>();

        // Add contents to the request body
        List<Map<String, Object>> contents = new ArrayList<>();

        // User input as the first content
        Map<String, Object> userPart = new HashMap<>();
        userPart.put("text", "CQ POTA DE W1XYZ");  // Example user message
        contents.add(createContent("user", userPart));

        // Model response as second content (simulated response)
        Map<String, Object> modelPart = new HashMap<>();
        modelPart.put("text", "W1ABC DE W1XYZ RST 599 K-0001");
        contents.add(createContent("model", modelPart));

        // New user message to simulate the input
        Map<String, Object> userInputPart = new HashMap<>();
        userInputPart.put("text", userInput);  // User's input
        contents.add(createContent("user", userInputPart));

        body.put("contents", contents);

        // Add system instruction with context
        Map<String, Object> systemInstruction = new HashMap<>();
        Map<String, Object> systemInstructionPart = new HashMap<>();
        systemInstructionPart.put("text", context);
        systemInstruction.put("role", "user");
        systemInstruction.put("parts", Collections.singletonList(systemInstructionPart));
        body.put("systemInstruction", systemInstruction);

        // Generation configuration (temperature, etc.)
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 1);
        generationConfig.put("topK", 40);
        generationConfig.put("topP", 0.95);
        generationConfig.put("maxOutputTokens", 8192);
        generationConfig.put("responseMimeType", "text/plain");

        body.put("generationConfig", generationConfig);

        // Create an ObjectMapper to convert the Java object to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonPayload = objectMapper.writeValueAsString(body);

        // Create the connection
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);  // Indicating that we will send data in the request body

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
