package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class User {
    // User Identity Data
    private String username;
    private String password;
    private String email;

    // User Setting Data
    private Stack<String> viewStack; // Stack to hold last views
    private double cwSpeed; // Slider for CW speed
    private double volume; // Slider for volume
    private double staticAmount; // Slider for static amount
    private boolean showCWLetters; // Boolean to show CW letters
    private boolean showCWAcronyms; // Boolean to show CW acronyms
    private Map<String, String> keyFirstActionMap;
    private Map<String, String> actionFirstActionMap;

    //Constructor
    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
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
    public User(){
        this("Default", "Default", "Default");
    }

    // View Stack Methods
    public void addView(String view) {viewStack.push(view);}
    public String popLastView() {if (!viewStack.isEmpty()){return viewStack.pop();} else{return "HomeScreenView";}}

    //User Data Get Methods
    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getEmail(){return email;}
    public double getCwSpeed() {return cwSpeed;}
    public double getVolume() {return volume;}
    public double getStaticAmount() {return staticAmount;}
    public boolean getShowCWLetters() {return showCWLetters;}
    public boolean getShowCWAcronyms() {return showCWAcronyms;}
    public Map<String, String> getKeyFirstActionMap(){return this.keyFirstActionMap;}
    public Map<String, String> getActionFirstActionMap(){return actionFirstActionMap;}
    public String getLastView() {if (!viewStack.isEmpty()) {return viewStack.peek();}return "HomeScreenView";}

    //User Data Set Methods
    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setEmail(String email) {this.email = email;}
    public void setViewStack(Stack<String> lastView) {this.viewStack = lastView;}
    public void setCwSpeed(double cwSpeed) {this.cwSpeed = cwSpeed;}
    public void setVolume(double volume) {this.volume = volume;}
    public void setStaticAmount(double staticAmount) {this.staticAmount = staticAmount;}
    public void setShowCWLetters(boolean showCWLetters) {this.showCWLetters = showCWLetters;}
    public void setShowCWAcronyms(boolean showCWAcronyms) {this.showCWAcronyms = showCWAcronyms;}

    // User Action Map Set Methods
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

}

