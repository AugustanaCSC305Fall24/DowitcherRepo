package org.example.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoogleApiRequest {
    // Correct API endpoint with the specific endpoint ID for your project.
    private static final String API_ENDPOINT = "https://us-central1-aiplatform.googleapis.com/v1/projects/1014730854479/locations/us-central1/endpoints/9049404008573173760:predict";

    // The project and location are also defined here for clarity.
    private static final String PROJECT_ID = "dowitchercwbot";
    private static final String LOCATION_ID = "us-central1";
    private static final String ENDPOINT_ID = "9049404008573173760"; // This is your endpoint ID.

    public String sendApiRequest(String requestBody, String accessToken) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(API_ENDPOINT);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Send request body
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    responseCode == 200 ? connection.getInputStream() : connection.getErrorStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            System.out.println("Raw JSON Response: " + response.toString());

            // If the response is HTML (i.e., error page like 404), extract the error message
            if (responseCode != 200) {
                String errorMessage = extractErrorMessage(response.toString());
                return errorMessage;
            }

            // Clean and parse the JSON response if the response code is 200
            String cleanedResponse = removeWelcomeMessage(response.toString());

            JsonReader reader = new JsonReader(new StringReader(cleanedResponse));
            reader.setLenient(true);
            JsonObject jsonResponse = JsonParser.parseReader(reader).getAsJsonObject();

            if (jsonResponse.has("candidates")) {
                JsonArray candidates = jsonResponse.getAsJsonArray("candidates");

                if (candidates.size() > 0) {
                    JsonObject candidate = candidates.get(0).getAsJsonObject();
                    JsonArray parts = candidate.getAsJsonArray("parts");

                    if (parts != null && parts.size() > 0) {
                        return parts.get(0).getAsJsonObject().get("text").getAsString();
                    }
                }
            }

            if (jsonResponse.has("error")) {
                return "Error: " + jsonResponse.getAsJsonObject("error").get("message").getAsString();
            }

            return "Error: Unexpected response format.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not process the request.";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String removeWelcomeMessage(String responseContent) {
        int jsonStartIndex = responseContent.indexOf("{");
        if (jsonStartIndex != -1) {
            return responseContent.substring(jsonStartIndex);
        } else {
            return "{}"; // return empty JSON object if no valid content
        }
    }

    // Method to extract error message from HTML response (e.g., 404 error page)
    private String extractErrorMessage(String htmlResponse) {
        // Look for error message text inside <title> or <h1> tag
        if (htmlResponse.contains("<title>") && htmlResponse.contains("</title>")) {
            int startIdx = htmlResponse.indexOf("<title>") + 7;
            int endIdx = htmlResponse.indexOf("</title>");
            return htmlResponse.substring(startIdx, endIdx).trim();
        }
        // If no specific title tag is found, return generic error message
        return "Error: Unable to reach the server or invalid endpoint.";
    }
}
