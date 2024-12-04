package org.example.data;

public class ChatMessage {
    private String text;
    private String sender;
    private int frequency;

    public ChatMessage(String text, String sender, int frequency) {
        this.text = text;
        this.sender = sender;
        this.frequency = frequency;
    }

    public String getText() {
        return text;
    }

    public String getSender() {
        return sender;
    }

    public int getFrequency() {
        return frequency;
    }
}
