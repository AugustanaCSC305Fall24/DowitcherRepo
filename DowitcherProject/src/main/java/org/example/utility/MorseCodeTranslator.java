package org.example.utility;

import java.util.HashMap;
import java.util.Map;

public class MorseCodeTranslator {

    // Map to store Morse code translations
    private static final Map<String, String> morseCodeMap = new HashMap<>();
    private static final Map<String, String> reversedMorseCodeMap = new HashMap<>();
    private static final Map<String, String> cwMessagesMap = new HashMap<>();

    // Initialize Morse code map
    static {
        morseCodeMap.put(".-", "A");
        morseCodeMap.put("-...", "B");
        morseCodeMap.put("-.-.", "C");
        morseCodeMap.put("-..", "D");
        morseCodeMap.put(".", "E");
        morseCodeMap.put("..-.", "F");
        morseCodeMap.put("--.", "G");
        morseCodeMap.put("....", "H");
        morseCodeMap.put("..", "I");
        morseCodeMap.put(".---", "J");
        morseCodeMap.put("-.-", "K");
        morseCodeMap.put(".-..", "L");
        morseCodeMap.put("--", "M");
        morseCodeMap.put("-.", "N");
        morseCodeMap.put("---", "O");
        morseCodeMap.put(".--.", "P");
        morseCodeMap.put("--.-", "Q");
        morseCodeMap.put(".-.", "R");
        morseCodeMap.put("...", "S");
        morseCodeMap.put("-", "T");
        morseCodeMap.put("..-", "U");
        morseCodeMap.put("...-", "V");
        morseCodeMap.put(".--", "W");
        morseCodeMap.put("-..-", "X");
        morseCodeMap.put("-.--", "Y");
        morseCodeMap.put("--..", "Z");
        morseCodeMap.put("-----", "0");
        morseCodeMap.put(".----", "1");
        morseCodeMap.put("..---", "2");
        morseCodeMap.put("...--", "3");
        morseCodeMap.put("....-", "4");
        morseCodeMap.put(".....", "5");
        morseCodeMap.put("-....", "6");
        morseCodeMap.put("--...", "7");
        morseCodeMap.put("---..", "8");
        morseCodeMap.put("----.", "9");

        for (Map.Entry<String, String> entry : morseCodeMap.entrySet()) {
            reversedMorseCodeMap.put(entry.getValue(), entry.getKey());
        }

        cwMessagesMap.put("CQ", "-.-. --.-");
        cwMessagesMap.put("DE", "-.. .");
        cwMessagesMap.put("QTH", "--.- - ....");
        cwMessagesMap.put("RST", ".-. ... -");
        cwMessagesMap.put("QSL", "--.- ... .-..");
        cwMessagesMap.put("QRZ?", "--.- .-. --..");
        cwMessagesMap.put("QRL?", "--.- .-. .-..");
        cwMessagesMap.put("73", "--... ...--");
        cwMessagesMap.put("88", "---.. ---..");
        cwMessagesMap.put("QSB", "--.- ... -...");
        cwMessagesMap.put("QRP", "--.- .-. .--.");
        cwMessagesMap.put("QSY", "--.- ... -.--");
        cwMessagesMap.put("QSO", "--.- ... ---");
        cwMessagesMap.put("QRM", "--.- .-. --");
        cwMessagesMap.put("QRN", "--.- .-. -.");
        cwMessagesMap.put("QRX", "--.- .-. -..-");
        cwMessagesMap.put("QRV", "--.- .-. ...-");
        cwMessagesMap.put("QRO", "--.- .-. ---");
        cwMessagesMap.put("SK", "... -.-");
    }

    // Translate Morse code to English
    public static String translateMorseCode(String morseCode) {
        StringBuilder translated = new StringBuilder();
        String[] words = morseCode.trim().split("/");  // / to separate words

        for (String word : words) {
            String[] letters = word.split(" ");  // 1 space to separate letters
            for (String letter : letters) {
                String translatedLetter = morseCodeMap.get(letter);
                if (translatedLetter != null) {
                    translated.append(translatedLetter);
                } else {
                    // If the Morse code is not found, add a space (instead of '?' or ignoring it)
                    translated.append(" ");  // To handle cases like unknown symbols
                }
            }
            translated.append(" ");
        }

        return translated.toString().trim();
    }

    /**
     *
     * @param text - The english string that is to be turned into morse code
     * @return the corresponding morse code for the text
     */
    public static String translateToMorseCode(String text) {
        StringBuilder translated = new StringBuilder();

        // Convert the text to uppercase to match the map keys
        text = text.toUpperCase();

        // Iterate over each character in the text
        for (char letter : text.toCharArray()) {
            if (letter == ' ') {
                translated.append("/ ");  // Use / to separate words in Morse code
            } else {
                String morseCode = reversedMorseCodeMap.get(String.valueOf(letter));
                if (morseCode != null) {
                    translated.append(morseCode).append(" ");
                } else {
                    // Handle special characters (numbers, punctuation) here, e.g., append their Morse code
                    // Add handling for special cases, like numbers, if needed
                    translated.append("? ");  // Placeholder for unrecognized characters
                }
            }
        }

        return translated.toString().trim();
    }



    public static Map<String, String> getCwAlphabet() {
        return new HashMap<>(morseCodeMap);
    }
    public static Map<String, String> getCwMessagesMap() {
        return new HashMap<>(cwMessagesMap);
    }
}
