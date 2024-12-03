package org.example.data;


import javafx.scene.input.KeyCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    // User Identity Data
    private static String username;
    private String password;
    private String email;

    // User Setting Data
    private Stack<String> viewStack; // Stack to hold last views
    private static long cwSpeed; // Slider for CW speed
    private static double volume; // Slider for volume
    private double staticAmount; // Slider for static amount
    private boolean showCWLetters; // Boolean to show CW letters
    private boolean showCWAcronyms; // Boolean to show CW acronyms
    private static Map<KeyCode, String> keyFirstActionMap;
    private static Map<String, KeyCode> actionFirstActionMap;

    //Constructor
    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.viewStack = new Stack<>();
        this.cwSpeed = 300; // Default value
        this.volume = 100.0; // Default value
        this.staticAmount = 50.0; // Default value
        this.showCWLetters = true; // Default value
        this.showCWAcronyms = true; // Default value
        this.keyFirstActionMap = new HashMap<>();
        this.actionFirstActionMap = new HashMap<>();
        //Setting default keys for the action map
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
        setActionMap(exitProgramKey, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);    }
    public User(){
        this("Default", "Default", "Default");
    }

    // View Stack Methods
    public void addView(String view) {viewStack.push(view);}
    public String popLastView() {if (!viewStack.isEmpty()){return viewStack.pop();} else{return "HomeScreenView";}}

    //User Data Get Methods
    public static String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getEmail(){return email;}
    public static long getCwSpeed() {return cwSpeed;}
    public static double getVolume() {return volume;}
    public double getStaticAmount() {return staticAmount;}
    public boolean getShowCWLetters() {return showCWLetters;}
    public boolean getShowCWAcronyms() {return showCWAcronyms;}
    public Map<KeyCode, String> getKeyFirstActionMap(){return this.keyFirstActionMap;}
    public static Map<String, KeyCode> getActionFirstActionMap(){return actionFirstActionMap;}
    public String getLastView() {if (!viewStack.isEmpty()) {return (String) viewStack.peek();}return "HomeScreenView";}

    //User Data Set Methods
    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setViewStack(Stack<String> lastView) {this.viewStack = lastView;}
    public void setCwSpeed(long cwSpeed) {
        this.cwSpeed = cwSpeed;
        System.out.println(cwSpeed);
    }
    public void setVolume(double volume) {this.volume = volume;}
    public void setStaticAmount(double staticAmount) {this.staticAmount = staticAmount;}
    public void setShowCWLetters(boolean showCWLetters) {this.showCWLetters = showCWLetters;}
    public void setShowCWAcronyms(boolean showCWAcronyms) {this.showCWAcronyms = showCWAcronyms;}

    public void setActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                             KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        setKeyFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
        setActionFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, straightKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
    }

    private void setKeyFirstActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                                      KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        // Map keyboard keys to actions
        keyFirstActionMap.put(exitProgram, "exitProgram");                      // Exit Program
        keyFirstActionMap.put(settingsKey, "settingsKey");                      // Settings
        keyFirstActionMap.put(dahKey, "dahKey");                                // Dah action
        keyFirstActionMap.put(ditKey, "ditKey");                                // Dit action
        keyFirstActionMap.put(straightKey, "straightKey");
        keyFirstActionMap.put(frequencyUpKey, "frequencyUpKey");                // Frequency Up
        keyFirstActionMap.put(frequencyDownKey, "frequencyDownKey");            // Frequency Down
        keyFirstActionMap.put(filterUpKey, "filterUpKey");                      // Filter Up
        keyFirstActionMap.put(filterDownKey, "filterDownKey");                  // Filter Down
    }

    private void setActionFirstActionMap(KeyCode exitProgram, KeyCode settingsKey, KeyCode dahKey, KeyCode ditKey, KeyCode straightKey,
                                         KeyCode frequencyUpKey, KeyCode frequencyDownKey, KeyCode filterUpKey, KeyCode filterDownKey) {
        // Map actions to keyboard keys
        actionFirstActionMap.put("exitProgram", exitProgram);                      // Exit Program
        actionFirstActionMap.put("settingsKey", settingsKey);                      // Settings
        actionFirstActionMap.put("dahKey", dahKey);                                // Dah action
        actionFirstActionMap.put("ditKey", ditKey);                                // Dit action
        actionFirstActionMap.put("straightKey", straightKey);
        actionFirstActionMap.put("frequencyUpKey", frequencyUpKey);                // Frequency Up
        actionFirstActionMap.put("frequencyDownKey", frequencyDownKey);            // Frequency Down
        actionFirstActionMap.put("filterUpKey", filterUpKey);                      // Filter Up
        actionFirstActionMap.put("filterDownKey", filterDownKey);                  // Filter Down
    }

    // Example usage: Get action or key from the map
    public String getActionForKey(KeyCode keyCode) {
        return keyFirstActionMap.get(keyCode);
    }

    public static KeyCode getKeyForAction(String action) {
        return actionFirstActionMap.get(action);
    }

}

