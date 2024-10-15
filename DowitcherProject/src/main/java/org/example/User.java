package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class User {
    // Fields
    private Stack<String> viewStack; // Stack to hold last views
    private double cwSpeed; // Slider for CW speed
    private double volume; // Slider for volume
    private double staticAmount; // Slider for static amount
    private boolean showCWLetters; // Boolean to show CW letters
    private boolean showCWAcronyms; // Boolean to show CW acronyms
    private Map<String, String> keyFirstActionMap;
    private Map<String, String> actionFirstActionMap;

    // Constructor
    public User() {
        this.viewStack = new Stack<>();
        this.cwSpeed = 3; // Default value
        this.volume = 50.0; // Default value
        this.staticAmount = 50.0; // Default value
        this.showCWLetters = true; // Default value
        this.showCWAcronyms = true; // Default value
        this.keyFirstActionMap = new HashMap<>();
        this.actionFirstActionMap = new HashMap<>();
        //Setting default keys for the action map
        setActionMap("ESC", "TAB", "D", "A", "RIGHT_ARROW", "LEFT_ARROW", "UP_ARROW", "DOWN_ARROW");
    }

    //exitProgram, settingsKey, dahKey, ditKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey

    public void setActionMap(String exitProgram, String settingsKey, String dahKey, String ditKey,
                             String frequencyUpKey, String frequencyDownKey, String filterUpKey, String filterDownKey
    ) {
        setKeyFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
        setActionFirstActionMap(exitProgram, settingsKey, dahKey, ditKey, frequencyUpKey, frequencyDownKey, filterUpKey, filterDownKey);
    }

    private void setKeyFirstActionMap(String exitProgram, String settingsKey, String dahKey, String ditKey,
                                      String frequencyUpKey, String frequencyDownKey, String filterUpKey, String filterDownKey
    ) {
        // Map keyboard keys to actions
        keyFirstActionMap.put(exitProgram, "exitProgram");                      // Exit Program
        keyFirstActionMap.put(settingsKey, "settingsKey");                      // Settings
        keyFirstActionMap.put(dahKey, "dahKey");                                // Dah action
        keyFirstActionMap.put(ditKey, "ditKey");                                // Dit action
        keyFirstActionMap.put(frequencyUpKey, "frequencyUpKey");                // Frequency Up
        keyFirstActionMap.put(frequencyDownKey, "frequencyDownKey");            // Frequency Down
        keyFirstActionMap.put(filterUpKey, "filterUpKey");                      // Filter Up
        keyFirstActionMap.put(filterDownKey, "filterDownKey");                  // Filter Down
    }

    private void setActionFirstActionMap(String exitProgram, String settingsKey, String dahKey, String ditKey,
                                         String frequencyUpKey, String frequencyDownKey, String filterUpKey, String filterDownKey
    ) {
        // Map keyboard keys to actions
        keyFirstActionMap.put("exitProgram", exitProgram);                      // Exit Program
        keyFirstActionMap.put("settingsKey", settingsKey);                      // Settings
        keyFirstActionMap.put("dahKey", dahKey);                                // Dah action
        keyFirstActionMap.put("ditKey", ditKey);                                // Dit action
        keyFirstActionMap.put("frequencyUpKey", frequencyUpKey);                // Frequency Up
        keyFirstActionMap.put("frequencyDownKey", frequencyDownKey);            // Frequency Down
        keyFirstActionMap.put("filterUpKey", filterUpKey);                      // Filter Up
        keyFirstActionMap.put("filterDownKey", filterDownKey);                  // Filter Down
    }


    public Map<String, String> getKeyFirstActionMap(){
        return this.keyFirstActionMap;
    }
    public Map<String, String> getActionFirstActionMap(){
        return actionFirstActionMap;
    }

    // Getters and Setters
    public String getLastView() {
        if (!viewStack.isEmpty()) {
            return viewStack.peek();
        }
        return "HomeScreenView";
    }

    public void setviewStack(Stack<String> lastView) {
        this.viewStack = lastView;
    }

    public double getCwSpeed() {
        return cwSpeed;
    }

    public void setCwSpeed(double cwSpeed) {
        this.cwSpeed = cwSpeed;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getStaticAmount() {
        return staticAmount;
    }

    public void setStaticAmount(double staticAmount) {
        this.staticAmount = staticAmount;
    }

    public boolean isShowCWLetters() {
        return showCWLetters;
    }

    public void setShowCWLetters(boolean showCWLetters) {
        this.showCWLetters = showCWLetters;
    }

    public boolean isShowCWAcronyms() {
        return showCWAcronyms;
    }

    public void setShowCWAcronyms(boolean showCWAcronyms) {
        this.showCWAcronyms = showCWAcronyms;
    }

    // Method to add a view to the stack
    public void addView(String view) {
        viewStack.push(view);
    }

    // Method to pop the last view
    public String popLastView() {
        if (!viewStack.isEmpty()){
            return viewStack.pop();
        }
        else{
            return "HomeScreenView";
        }
    }
}

