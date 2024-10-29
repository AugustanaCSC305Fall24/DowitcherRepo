package org.example.data;

import java.io.*;

// Utility class for serialization and deserialization of User objects
public class UserSerialization {

    // Method to serialize the User object to a file
    public static void serializeUser(User user, String filename) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(user);
            System.out.println("User object has been serialized to " + filename);
        }
    }

    // Method to deserialize the User object from a file
    public static User deserializeUser(String filename) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            User user = (User) in.readObject();
            System.out.println("User object has been deserialized from " + filename);
            return user;
        }
    }
}
