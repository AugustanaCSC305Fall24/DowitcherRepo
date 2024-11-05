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

public class VertexWebScrapper {

    // Google OAuth Information
    // Client ID Below
    // 1014730854479-q01sh3maokh0jod60rs9oi7ebl1v51ug.apps.googleusercontent.com
    // Client Secret Below
    // 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

    private static final String API_URL = "https://us-central1-aiplatform.googleapis.com/v1/projects/1014730854479/locations/us-central1/endpoints/2951881956834410496";
    private final Gson gson;

    public VertexWebScrapper() {
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
            // Replace 'YOUR_ACCESS_TOKEN' with the token retrieved via 'gcloud auth application-default print-access-token' ya29.a0AeDClZBTKr3PAr71_NhvSwkNk2YUrMSuwSXQY48cuqSVIEVn3F9hBIkTDtP3CJ2jKYVxCnx2_bK6494RrptAbjv7diBfnTawRipt4-t3O4h1Yl1jRFyNhiShhbrCxpSGoX3vSMB0OvJxDu8eB5yuC0L2YHYiVPRqxgS56Gi4-Pyxh9vSkbuWX2TNVUfhuwfbozeKOPVvQAQrukq-nByXBW4RunolBnmtaITms1hfHXph0WHIUTpwtNOaqhK-aQxli_RJjno2fiLV0SpL42S9saG3rSs_dstxQOEhK1v6LeR_O-Vjvm3KPXkl04MXUAQ9l-3GYB1dyqoe05ydsjr9k7Fe2KOA_OKHiNSdrbrnQHk7Pf2LjhMPQAc00Gr6bh0ftC6bTlRIJ1t0EHa0fl4dX_BZv7GzHbdfnTn5aCgYKAVgSARESFQHGX2Mi6OVKQjdR28v5agSYSaREJg0427
            connection.setRequestProperty("Authorization", "Bearer ya29.a0AeDClZBTKr3PAr71_NhvSwkNk2YUrMSuwSXQY48cuqSVIEVn3F9hBIkTDtP3CJ2jKYVxCnx2_bK6494RrptAbjv7diBfnTawRipt4-t3O4h1Yl1jRFyNhiShhbrCxpSGoX3vSMB0OvJxDu8eB5yuC0L2YHYiVPRqxgS56Gi4-Pyxh9vSkbuWX2TNVUfhuwfbozeKOPVvQAQrukq-nByXBW4RunolBnmtaITms1hfHXph0WHIUTpwtNOaqhK-aQxli_RJjno2fiLV0SpL42S9saG3rSs_dstxQOEhK1v6LeR_O-Vjvm3KPXkl04MXUAQ9l-3GYB1dyqoe05ydsjr9k7Fe2KOA_OKHiNSdrbrnQHk7Pf2LjhMPQAc00Gr6bh0ftC6bTlRIJ1t0EHa0fl4dX_BZv7GzHbdfnTn5aCgYKAVgSARESFQHGX2Mi6OVKQjdR28v5agSYSaREJg0427");
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
