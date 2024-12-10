package org.example;

import org.example.data.User;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password123", "test@example.com");
    }

    @Test
    void testUserCreation() {
        assertEquals("testUser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testDefaultValues() {
        assertEquals(300, User.getCwSpeed());
        assertEquals(100.0, User.getVolume());
        assertEquals(50.0, user.getStaticAmount(), 0.001);
        assertTrue(user.getShowCWLetters());
        assertTrue(user.getShowCWAcronyms());
    }

    @Test
    void testPropertySettersAndGetters() {
        user.setUsername("newUsername");
        assertEquals("newUsername", user.getUsername());

        user.setPassword("newPassword");
        assertEquals("newPassword", user.getPassword());

        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmail());

        user.setCwSpeed(500);
        assertEquals(500, User.getCwSpeed());

        user.setVolume(75.0);
        assertEquals(75.0, User.getVolume(), 0.001);

        user.setStaticAmount(30.0);
        assertEquals(30.0, user.getStaticAmount(), 0.001);

        user.setShowCWLetters(false);
        assertFalse(user.getShowCWLetters());

        user.setShowCWAcronyms(false);
        assertFalse(user.getShowCWAcronyms());
    }

    @Test
    void testViewStack() {
        user.addView("SettingsView");
        user.addView("ProfileView");

        assertEquals("ProfileView", user.getLastView());
        assertEquals("ProfileView", user.popLastView());
        assertEquals("SettingsView", user.popLastView());
        assertEquals("HomeScreenView", user.popLastView()); // Empty stack should return "HomeScreenView"
    }

    @Test
    void testKeyFirstActionMap() {
        Map<KeyCode, String> keyMap = user.getKeyFirstActionMap();

        assertEquals("exitProgram", keyMap.get(KeyCode.ESCAPE));
        assertEquals("settingsKey", keyMap.get(KeyCode.TAB));
        assertEquals("dahKey", keyMap.get(KeyCode.D));
        assertEquals("ditKey", keyMap.get(KeyCode.A));
        assertEquals("frequencyUpKey", keyMap.get(KeyCode.RIGHT));
    }

    @Test
    void testActionFirstActionMap() {
        assertEquals(KeyCode.ESCAPE, User.getKeyForAction("exitProgram"));
        assertEquals(KeyCode.TAB, User.getKeyForAction("settingsKey"));
        assertEquals(KeyCode.D, User.getKeyForAction("dahKey"));
        assertEquals(KeyCode.A, User.getKeyForAction("ditKey"));
        assertEquals(KeyCode.RIGHT, User.getKeyForAction("frequencyUpKey"));
    }

//    @Test
//    void testSetActionMap() {
//        user.setActionMap(KeyCode.SPACE, KeyCode.ENTER, KeyCode.S, KeyCode.W, KeyCode.L, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT);
//
//        assertEquals("exitProgram", user.getActionForKey(KeyCode.SPACE));
//        assertEquals(KeyCode.SPACE, User.getKeyForAction("exitProgram"));
//        assertEquals("settingsKey", user.getActionForKey(KeyCode.ENTER));
//        assertEquals(KeyCode.ENTER, User.getKeyForAction("settingsKey"));
//    }
}
