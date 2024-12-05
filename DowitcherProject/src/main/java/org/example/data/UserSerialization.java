package org.example.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSerialization {
    private static final String USER_DATA_FILE = "userdata.json";

    // Add a user to the JSON file
    public static void addUser(User user) {
        List<User> users = loadUsers();
        users.add(user);

        try (FileWriter writer = new FileWriter(USER_DATA_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(users, writer);  // Serialize users with the user instance's non-static data
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load users from the JSON file
    public static List<User> loadUsers() {
        try (FileReader reader = new FileReader(USER_DATA_FILE)) {
            Gson gson = new Gson();
            User[] usersArray = gson.fromJson(reader, User[].class);
            return usersArray == null ? new ArrayList<>() : new ArrayList<>(List.of(usersArray));  // Convert to ArrayList
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Check if the call sign already exists
    public static boolean userExistsByCallSign(String callSign) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getCallSign().equals(callSign)) {
                return true;
            }
        }
        return false;
    }
}
