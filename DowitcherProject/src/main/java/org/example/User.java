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
        setActionMap("R", "T", "N", "S", "M", "L", "A", "S", "ESC", "P");
    }

    public void setActionMap(String restartAudioKeyField, String checkTranslationKeyField, String newAudioKeyField, String settingsKeyField,
                             String mainMenuKeyField, String translateKeyField, String dahKeyField, String ditKeyField, String escKeyField, String pauseKeyField
    ) {
        setKeyFirstActionMap(restartAudioKeyField, checkTranslationKeyField, newAudioKeyField, settingsKeyField,
                mainMenuKeyField, translateKeyField, dahKeyField, ditKeyField, escKeyField, pauseKeyField
        );
        setActionFirstActionMap(restartAudioKeyField, checkTranslationKeyField, newAudioKeyField, settingsKeyField,
                mainMenuKeyField, translateKeyField, dahKeyField, ditKeyField,
escKeyField, pauseKeyField);
    }

    private void setKeyFirstActionMap(String restartAudioKey, String checkTranslationKey, String newAudioKey, String settingsKey, String mainMenuKey,
                                      String translateKey, String dahKey, String ditKey, String escKey, String pauseKey
    ) {
        // Map keyboard keys to actions
        keyFirstActionMap.put(restartAudioKey, "restartAudio");    // Restart audio
        keyFirstActionMap.put(checkTranslationKey, "checkTranslation"); // Check translation
        keyFirstActionMap.put(newAudioKey, "newAudio");        // New audio
        keyFirstActionMap.put(settingsKey, "settings");        // Settings
        keyFirstActionMap.put(mainMenuKey, "mainMenu");        // Main menu
        keyFirstActionMap.put(translateKey, "translate");       // Translate
        keyFirstActionMap.put(dahKey, "dahAction");             // Dah action
        keyFirstActionMap.put(ditKey, "ditAction");             // Dit action
        keyFirstActionMap.put(escKey, "escape");                // Escape action
        keyFirstActionMap.put(pauseKey, "pause");               // Pause action
    }

    private void setActionFirstActionMap(String restartAudioKey, String checkTranslationKey, String newAudioKey, String settingsKey, String mainMenuKey,
                                         String translateKey, String dahKey, String ditKey, String escKey, String pauseKey
    ) {
        // Map actions to keyboard keys
        actionFirstActionMap.put("restartAudio", restartAudioKey);    // Restart audio
        actionFirstActionMap.put("checkTranslation", checkTranslationKey); // Check translation
        actionFirstActionMap.put("newAudio", newAudioKey);        // New audio
        actionFirstActionMap.put("settings", settingsKey);        // Settings
        actionFirstActionMap.put("mainMenu", mainMenuKey);        // Main menu
        actionFirstActionMap.put("translate", translateKey);       // Translate
        actionFirstActionMap.put("dahAction", dahKey);             // dah action
        actionFirstActionMap.put("ditAction", ditKey);             // Dit action
        actionFirstActionMap.put("escape", escKey);                // Escape action
        actionFirstActionMap.put("pause", pauseKey);               // Pause action
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

