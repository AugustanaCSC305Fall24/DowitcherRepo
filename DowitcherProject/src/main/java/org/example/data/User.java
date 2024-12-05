package org.example.data;

import javafx.scene.input.KeyCode;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // User Identity Data
    private String callSign;

    // User Setting Data
    private Stack<String> viewStack; // Stack to hold last views
    private static long cwSpeed; // Slider for CW speed
    private static double volume; // Slider for volume
    private double staticAmount; // Slider for static amount
    private boolean showCWLetters; // Boolean to show CW letters
    private boolean showCWAcronyms; // Boolean to show CW acronyms

    // Static Maps for actions and keys (to be shared across all instances)
    private static Map<KeyCode, String> keyFirstActionMap = new HashMap<>();
    private static Map<String, KeyCode> actionFirstActionMap = new HashMap<>();

    //All user made bots
    public static List<ChatBot> chatBotRegistry = new ArrayList<>();
    private boolean isStatic;

    // Constructor
    public User(String callSign) {
        this.callSign = callSign;
        this.viewStack = new Stack<>();
        this.cwSpeed = 300; // Default value
        this.volume = 100.0; // Default value
        this.staticAmount = 50.0; // Default value
        this.showCWLetters = true; // Default value
        this.showCWAcronyms = true; // Default value

        // Setting default keys for the action map
        KeyCode exitProgramKey = KeyCode.valueOf("ESCAPE");
        KeyCode settingsKey = KeyCode.valueOf("TAB");
        KeyCode dahKey = KeyCode.valueOf("D");
        KeyCode ditKey = KeyCode.valueOf("A");
        KeyCode straightKey = KeyCode.valueOf("L");
        KeyCode frequencyUpKey = KeyCode.valueOf("RIGHT");
        KeyCode frequencyDownKey = KeyCode.valueOf("LEFT");
        KeyCode filterUpKey = KeyCode.valueOf("UP");
        KeyCode filterDownKey = KeyCode.valueOf("DOWN");

        // Call setActionMap with KeyCode
        setActionMap(exitProgramKey, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
    }

    // Default constructor
    public User() {
        this("Default");
    }

    // View Stack Methods
    public void addView(String view) {
        viewStack.push(view);  // Add a view to the stack
    }

    // Pop the last view
    public String popLastView() {
        if (!viewStack.isEmpty()) {
            return viewStack.pop();  // Remove and return the last view
        } else {
            return "HomeScreenView";  // Default view if stack is empty
        }
    }

    // Getters and Setters (for other fields)
    public String getCallSign() {
        return callSign;
    }

    public static long getCwSpeed() {
        return cwSpeed;
    }

    public static double getVolume() {
        return volume;
    }

    public double getStaticAmount() {
        return staticAmount;
    }

    public boolean getShowCWLetters() {
        return showCWLetters;
    }

    public boolean getShowCWAcronyms() {
        return showCWAcronyms;
    }

    // Static Methods for Maps (since they are static variables)
    public static Map<KeyCode, String> getKeyFirstActionMap() {
        return keyFirstActionMap;
    }

    public static Map<String, KeyCode> getActionFirstActionMap() {
        return actionFirstActionMap;
    }

    // Setter Methods for User Settings
    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    public void setCwSpeed(long cwSpeed) {
        this.cwSpeed = cwSpeed;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setStaticAmount(double staticAmount) {
        this.staticAmount = staticAmount;
    }

    public void setShowCWLetters(boolean showCWLetters) {
        this.showCWLetters = showCWLetters;
    }

    public void setShowCWAcronyms(boolean showCWAcronyms) {
        this.showCWAcronyms = showCWAcronyms;
    }

    // Set Action Maps for keys and actions
    public void setActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                             KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        setKeyFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
        setActionFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
    }

    private void setKeyFirstActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                                      KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        // Map keyboard keys to actions
        keyFirstActionMap.put(exitProgram, "exitProgram");
        keyFirstActionMap.put(settingsKey, "settingsKey");
        keyFirstActionMap.put(dahKey, "dahKey");
        keyFirstActionMap.put(ditKey, "ditKey");
        keyFirstActionMap.put(straightKey, "straightKey");
        keyFirstActionMap.put(frequencyUpKey, "frequencyUpKey");
        keyFirstActionMap.put(frequencyDownKey, "frequencyDownKey");
        keyFirstActionMap.put(filterUpKey, "filterUpKey");
        keyFirstActionMap.put(filterDownKey, "filterDownKey");
    }

    private void setActionFirstActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                                         KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        // Map actions to keyboard keys
        actionFirstActionMap.put("exitProgram", exitProgram);
        actionFirstActionMap.put("settingsKey", settingsKey);
        actionFirstActionMap.put("dahKey", dahKey);
        actionFirstActionMap.put("ditKey", ditKey);
        actionFirstActionMap.put("straightKey", straightKey);
        actionFirstActionMap.put("frequencyUpKey", frequencyUpKey);
        actionFirstActionMap.put("frequencyDownKey", frequencyDownKey);
        actionFirstActionMap.put("filterUpKey", filterUpKey);
        actionFirstActionMap.put("filterDownKey", filterDownKey);
    }

    // Example usage: Get action or key from the map
    public String getActionForKey(KeyCode keyCode) {
        return keyFirstActionMap.get(keyCode);
    }

    public static KeyCode getKeyForAction(String action) {
        return actionFirstActionMap.get(action);
    }

    @Override
    public String toString() {
        return "User{" +
                "callSign='" + callSign + '\'' +
                ", cwSpeed=" + cwSpeed +
                ", volume=" + volume +
                ", staticAmount=" + staticAmount +
                ", showCWLetters=" + showCWLetters +
                ", showCWAcronyms=" + showCWAcronyms +
                '}';
    }

    public void setStaticEnabled(boolean selected) {
    this.isStatic = selected;
    }

    public boolean isStaticEnabled() {
        return isStatic;
    }
}
