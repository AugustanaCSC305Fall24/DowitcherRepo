package org.example.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatBot {
    private String Name;
    private String Context;
    private String ChatLog;
    private String callSign;
    private Double Frequency;
    private final String defaultInfo =
            "You’re using Continuous Wave (CW) Morse code to communicate, which involves short, simple interactions due to the nature of Morse code. "
                    + "Keep messages brief and straightforward, using simple English that’s easy to translate into Morse code. "
                    + "Do not include newline characters or unnecessary breaks in the message. Keep everything on a single line. "
                    + "Even if you are spoken to in normal English, always respond in CW. "
                    + "The following is information that the user filled out specific to your character. "
                    + "In order, separated by a period, it will be your name, call sign, context, and the conversation up to this point.";

    public static List<ChatBot> chatBotRegistry = new ArrayList<>();

    public ChatBot(String name, String context, String chatLog) {
        this.Name = name;
        this.Context = context;
        this.ChatLog = chatLog;
        this.callSign = generateUniqueCallSign(name);
        this.Frequency = generateUniqueFrequency();

        chatBotRegistry.add(this);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContext() {
        return Context;
    }

    public void setContext(String context) {
        Context = context;
    }

    public String getChatLog() {
        return ChatLog;
    }

    public void setChatLog(String chatLog) {
        ChatLog = chatLog;
    }

    public Double getFrequency() {
        return Frequency;
    }

    public void setFrequency(Double frequency) {
        Frequency = frequency;
    }

    public String getCallSign() {
        return callSign;
    }

    public void setCallSign(String callSign) {
        this.callSign = callSign;
    }

    /**
     * Adds a message to the bot's conversation log.
     * @param newMessage The message to add in the format: "User: [message]".
     */
    public void addNewMessage(String newMessage) {
        ChatLog += " " + newMessage + "\n";
    }

    /**
     * Combines static instructions with dynamic bot data for the API request.
     * @return The full prompt for the bot, ready to send to the API.
     */
    public String formatPrompt() {
        return defaultInfo
                + " Your name: " + Name + "."
                + " Your call sign: " + callSign + "."
                + " Your character context: " + Context + "."
                + " Conversation history so far: " + ChatLog;
    }

    // Generate a unique call sign based on name
    private String generateUniqueCallSign(String name) {
        String baseCallSign = "K" + name.substring(0, Math.min(name.length(), 3)).toUpperCase();
        String callSign = baseCallSign;
        int i = 1;

        // Ensure the call sign is unique
        while (isCallSignTaken(callSign)) {
            callSign = baseCallSign + i;
            i++;
        }
        return callSign;
    }

    // Check if the call sign is already taken
    private boolean isCallSignTaken(String callSign) {
        for (ChatBot bot : chatBotRegistry) {
            if (bot.getCallSign().equals(callSign)) {
                return true;
            }
        }
        return false;
    }

    // Generate a unique frequency
    private Double generateUniqueFrequency() {
        Random random = new Random();
        Double frequency;
        boolean validFrequency = false;

        // Generate a random frequency between 1.8 MHz and 29.7 MHz for CW
        do {
            frequency = 1.8 + (29.7 - 1.8) * random.nextDouble();
            validFrequency = !isFrequencyTooClose(frequency);
        } while (!validFrequency);

        return frequency;
    }

    // Check if the frequency is too close to others
    private boolean isFrequencyTooClose(Double frequency) {
        double threshold = 0.05; // 50 kHz threshold to consider frequencies too close
        for (ChatBot bot : chatBotRegistry) {
            if (Math.abs(bot.getFrequency() - frequency) < threshold) {
                return true;
            }
        }
        return false;
    }
}
