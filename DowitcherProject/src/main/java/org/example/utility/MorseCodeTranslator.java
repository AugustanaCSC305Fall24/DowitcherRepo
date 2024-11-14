package org.example.utility;

import java.util.HashMap;
import java.util.Map;

public class MorseCodeTranslator {

    // Map to store Morse code translations
    private static final Map<String, String> morseCodeMap = new HashMap<>();
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
        String[] words = morseCode.trim().split("/");  //   / to separate words

        for (String word : words) {
            String[] letters = word.split(" ");  // 1 space to separate letters
            for (String letter : letters) {
                String translatedLetter = morseCodeMap.get(letter);
                if (translatedLetter != null) {
                    translated.append(translatedLetter);
                } else {
                    translated.append("?");  // If not found, use '?' as placeholder
                }
            }
            translated.append(" ");
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
