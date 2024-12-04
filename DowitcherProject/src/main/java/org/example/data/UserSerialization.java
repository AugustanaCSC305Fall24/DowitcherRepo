package org.example.data;

import com.google.gson.Gson;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;


// Utility class for serialization and deserialization of User objects
public class UserSerialization {

    // Method to serialize the User object to a file
    public static void serializeUser(User user, String filename) throws IOException {
        Gson gson = new Gson();  // Create Gson object for JSON conversion
        String json = gson.toJson(user);  // Convert User object to JSON

        // Write JSON to file
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(json);
            System.out.println("User has been serialized to " + filename);
        }
    }

    // Method to deserialize the User object from a file
    public static User deserializeUser(String filename) throws IOException, ClassNotFoundException {
        Gson gson = new Gson(); // Create Gson object for JSON conversion
        String json = new String(Files.readAllBytes(Paths.get(filename))); // Read the JSON from file

        return gson.fromJson(json, User.class);  // Convert JSON back to User object
    }

    // Method to check if a username already exists in the system
    public static boolean userExists(String username, String filename) {
        try {
            User user = deserializeUser(filename);  // Deserialize the user from file
            return User.getUsername().equals(username);   // Check if username matches
        } catch (IOException e) {
            System.out.println("Error reading user data: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to update the existing user (for login or modification)
    public static void updateUser(User updatedUser, String filename) throws IOException {
        serializeUser(updatedUser, filename);  // Serialize the updated user back to the file
        System.out.println("User data has been updated.");
    }

}
