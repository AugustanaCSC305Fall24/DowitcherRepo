package org.example;

import java.util.*;

public class MorseTranslator {


    Map<String, Character> morseToEnglish = new HashMap<>() {{
        morseToEnglish.put(".-", 'A');
        morseToEnglish.put("-...", 'B');
        morseToEnglish.put("-.-.", 'C');
        morseToEnglish.put("-..", 'D');
        morseToEnglish.put(".", 'E');
        morseToEnglish.put("..-.", 'F');
        morseToEnglish.put("--.", 'G');
        morseToEnglish.put("....", 'H');
        morseToEnglish.put("..", 'I');
        morseToEnglish.put(".---", 'J');
        morseToEnglish.put("-.-", 'K');
        morseToEnglish.put(".-..", 'L');
        morseToEnglish.put("--", 'M');
        morseToEnglish.put("-.", 'N');
        morseToEnglish.put("---", 'O');
        morseToEnglish.put(".--.", 'P');
        morseToEnglish.put("--.-", 'Q');
        morseToEnglish.put(".-.", 'R');
        morseToEnglish.put("...", 'S');
        morseToEnglish.put("-", 'T');
        morseToEnglish.put("..-", 'U');
        morseToEnglish.put("...-", 'V');
        morseToEnglish.put(".--", 'W');
        morseToEnglish.put("-..-", 'X');
        morseToEnglish.put("-.--", 'Y');
        morseToEnglish.put("--..", 'Z');
    }};

    //translating cw into english/letters
    public String translate(String morseCode){
        StringBuilder sb = new StringBuilder();
        String[] morseCodeArray = morseCode.split(" ");
        for (String morse : morseCodeArray){
            Character letter = morseToEnglish.get(morse);
            sb.append(letter);
        }
        return sb.toString();
    }







}
