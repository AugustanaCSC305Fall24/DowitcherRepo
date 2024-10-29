package org.example;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

public class GenerativeAIChat {
    private static final String API_KEY = "AIzaSyAJDvLBGMSynARLQMCN3xCn3SfVmuWD7hw";
    private static final String BASE_URL = "https://aistudio.google.com/app/u/1/prompts/15Af2n_6eaBV46ECgzuxt5y8_-qs7unWZ";
    private static final String PROJECT_NUMBER = "495075050720";

    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    // Fields for JavaFX components
    private TextField inputField;
    private TextArea outputArea;

    // Constructor to pass JavaFX components
    public GenerativeAIChat(TextField inputField, TextArea outputArea) {
        this.inputField = inputField;
        this.outputArea = outputArea;
    }

    // Method to start a chat session with user input
    public void startChatSession() {
        String inputMessage = inputField.getText();
        try {
            String response = fetchChatResponse(inputMessage);
            outputArea.setText(response); // Display the response in the TextArea
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Private helper method to handle API call
    private String fetchChatResponse(String input) throws IOException {
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 1);
        generationConfig.put("topP", 0.95);
        generationConfig.put("topK", 64);
        generationConfig.put("maxOutputTokens", 8192);
        generationConfig.put("responseMimeType", "text/plain");

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("parts", new String[]{input});

        Map<String, Object> historyMessage = new HashMap<>();
        historyMessage.put("role", "model");
        historyMessage.put("parts", new String[]{
                "Okay, I'm ready to start making contacts. Let's see what parks we can find! \n\n**CQ POTA DE [YOUR CALL SIGN]**"
        });

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("generationConfig", generationConfig);
        requestBody.put("history", new Object[]{message, historyMessage});

        String jsonBody = gson.toJson(requestBody);

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        }
    }
}
