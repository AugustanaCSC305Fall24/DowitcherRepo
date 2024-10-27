package org.example;

import okhttp3.*;
import com.google.gson.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenerativeAIChat {
    private static final String API_KEY = System.getenv("GEMINI_API_KEY");
    private static final String BASE_URL = "https://api.generativeai.google.com/v1/models/gemini-1.5-flash:chat";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        try {
            String inputMessage = "INSERT_INPUT_HERE";
            String response = startChatSession(inputMessage);
            System.out.println("Response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String startChatSession(String input) throws IOException {
        // Request body setup
        Map<String, Object> generationConfig = new HashMap<>();
        generationConfig.put("temperature", 1);
        generationConfig.put("topP", 0.95);
        generationConfig.put("topK", 64);
        generationConfig.put("maxOutputTokens", 8192);
        generationConfig.put("responseMimeType", "text/plain");

        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("parts", new String[] { input });

        Map<String, Object> historyMessage = new HashMap<>();
        historyMessage.put("role", "model");
        historyMessage.put("parts", new String[] {
                "Okay, I'm ready to start making contacts. Let's see what parks we can find! \n\n**CQ POTA DE [YOUR CALL SIGN]**"
        });

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("generationConfig", generationConfig);
        requestBody.put("history", new Object[] { message, historyMessage });

        String jsonBody = gson.toJson(requestBody);

        // Creating the request
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
