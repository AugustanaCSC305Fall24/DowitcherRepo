//package org.example.data;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//public class GoogleApiTest {
//
//    private static final String API_KEY = "AIzaSyAKcjscMCpTR8gcXBMr02mLOL9kT1AuQjY";
//    //AIzaSyBEHJPnCQzaL8NFC4T_nY-YQS4hERiI_Dc
//    public static void main(String[] args) {
//        try {
//            // Example endpoint using Google Books API
//            String endpoint = "https://www.googleapis.com/books/v1/volumes?q=isbn:0747532699&key=" + API_KEY;
//
//            // Make the request and get the response
//            String response = makeApiRequest(endpoint);
//
//            // Parse and print the JSON response
//            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
//            System.out.println("API Response: " + jsonResponse);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Method to make the API request
//    public static String makeApiRequest(String endpoint) throws Exception {
//        URL url = new URL(endpoint);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//
//        // Send the request and get the response
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//            return response.toString();
//        }
//    }
//}
//
