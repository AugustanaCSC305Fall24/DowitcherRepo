package org.example;

import java.util.HashMap;
import java.util.Map;

public class MorseCodeTranslator {

    // Map to store Morse code translations
    private static final Map<String, String> morseCodeMap = new HashMap<>();

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
}
