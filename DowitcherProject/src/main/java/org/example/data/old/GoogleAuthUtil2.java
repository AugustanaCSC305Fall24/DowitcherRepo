//package org.example.data;
//
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import java.io.OutputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.charset.StandardCharsets;
//
//public class GoogleAuthUtil2 {
//
//    private static final String CLIENT_ID = "dowitchercwbot";
//    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
//    private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";
//    private static final String GRANT_TYPE = "authorization_code";
//
//    // Method to make API requests using POST with required OAuth parameters
//    public static String makePostApiRequest(String endpoint, String authorizationCode) throws Exception {
//        // Create the URL
//        URL url = new URL(endpoint);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//
//        // Set the request method to POST
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        connection.setDoOutput(true); // Enables sending a POST body
//
//        // Construct the POST body with required parameters
//        String requestBody = String.format(
//                "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=%s",
//                authorizationCode, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI, GRANT_TYPE
//        );
//
//        // Send the request
//        try (OutputStream os = connection.getOutputStream()) {
//            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
//            os.write(input, 0, input.length);
//        }
//
//        // Handle response errors
//        int responseCode = connection.getResponseCode();
//        if (responseCode != 200) {
//            throw new Exception("Failed to fetch data from Google API: HTTP request failed with response code: " + responseCode);
//        }
//
//        // Read the response
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            return response.toString();
//        }
//    }
//
//    // Example: Fetching JSON data from Google API
//    public static JsonObject fetchJsonFromApi(String endpoint, String authorizationCode) throws Exception {
//        String response = makePostApiRequest(endpoint, authorizationCode);
//        return JsonParser.parseString(response).getAsJsonObject();
//    }
//}
