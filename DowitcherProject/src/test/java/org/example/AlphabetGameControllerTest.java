//package org.example;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.example.ui.practice.AlphabetGameController;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import javafx.scene.text.TextFlow;
//
//import java.util.Stack;
//
//public class AlphabetGameControllerTest {
//
//    private AlphabetGameController controller;
//
//    @BeforeEach
//    void setUp() {
//        controller = new AlphabetGameController();
//        controller.cwStack = new Stack<>();
//        controller.letterStack = new Stack<>();
//        controller.currentLetterTextFlow = new TextFlow();
//    }
//
//    @Test
//    void testGenerateNewLetter() {
//        controller.cwStack.push(".-");
//        controller.letterStack.push("A");
//        controller.generateNewLetter();
//        assertEquals(".-", controller.currentCW, "currentCW should be '.-'");
//        assertEquals("A", controller.currentLetter, "currentLetter should be 'A'");
//
//        controller.cwStack.push("-...");
//        controller.letterStack.push("B");
//        controller.generateNewLetter();
//        assertEquals("-...", controller.currentCW, "currentCW should be '-...'");
//        assertEquals("B", controller.currentLetter, "currentLetter should be 'B'");
//
//        controller.cwStack.push("-.-.");
//        controller.letterStack.push("C");
//        controller.generateNewLetter();
//        assertEquals("-.-.", controller.currentCW, "currentCW should be '-.-.'");
//        assertEquals("C", controller.currentLetter, "currentLetter should be 'C'");
//
//        controller.cwStack.push("-..");
//        controller.letterStack.push("D");
//        controller.generateNewLetter();
//        assertEquals("-..", controller.currentCW, "currentCW should be '-..'");
//        assertEquals("D", controller.currentLetter, "currentLetter should be 'D'");
//
//        controller.cwStack.push(".");
//        controller.letterStack.push("E");
//        controller.generateNewLetter();
//        assertEquals(".", controller.currentCW, "currentCW should be '.'");
//        assertEquals("E", controller.currentLetter, "currentLetter should be 'E'");
//
//        controller.cwStack.push("..-.");
//        controller.letterStack.push("F");
//        controller.generateNewLetter();
//        assertEquals("..-.", controller.currentCW, "currentCW should be '..-.'");
//        assertEquals("F", controller.currentLetter, "currentLetter should be 'F'");
//
//        controller.cwStack.push("--.");
//        controller.letterStack.push("G");
//        controller.generateNewLetter();
//        assertEquals("--.", controller.currentCW, "currentCW should be '--.'");
//        assertEquals("G", controller.currentLetter, "currentLetter should be 'G'");
//
//        controller.cwStack.push("....");
//        controller.letterStack.push("H");
//        controller.generateNewLetter();
//        assertEquals("....", controller.currentCW, "currentCW should be '....'");
//        assertEquals("H", controller.currentLetter, "currentLetter should be 'H'");
//
//        controller.cwStack.push("..");
//        controller.letterStack.push("I");
//        controller.generateNewLetter();
//        assertEquals("..", controller.currentCW, "currentCW should be '..'");
//        assertEquals("I", controller.currentLetter, "currentLetter should be 'I'");
//
//        controller.cwStack.push(".---");
//        controller.letterStack.push("J");
//        controller.generateNewLetter();
//        assertEquals(".---", controller.currentCW, "currentCW should be '.---'");
//        assertEquals("J", controller.currentLetter, "currentLetter should be 'J'");
//
//        controller.cwStack.push("-.-");
//        controller.letterStack.push("K");
//        controller.generateNewLetter();
//        assertEquals("-.-", controller.currentCW, "currentCW should be '-.-'");
//        assertEquals("K", controller.currentLetter, "currentLetter should be 'K'");
//
//        controller.cwStack.push(".-..");
//        controller.letterStack.push("L");
//        controller.generateNewLetter();
//        assertEquals(".-..", controller.currentCW, "currentCW should be '.-..'");
//        assertEquals("L", controller.currentLetter, "currentLetter should be 'L'");
//
//        controller.cwStack.push("--");
//        controller.letterStack.push("M");
//        controller.generateNewLetter();
//        assertEquals("--", controller.currentCW, "currentCW should be '--'");
//        assertEquals("M", controller.currentLetter, "currentLetter should be 'M'");
//
//        controller.cwStack.push("-.");
//        controller.letterStack.push("N");
//        controller.generateNewLetter();
//        assertEquals("-.", controller.currentCW, "currentCW should be '-.'");
//        assertEquals("N", controller.currentLetter, "currentLetter should be 'N'");
//
//        controller.cwStack.push("---");
//        controller.letterStack.push("O");
//        controller.generateNewLetter();
//        assertEquals("---", controller.currentCW, "currentCW should be '---'");
//        assertEquals("O", controller.currentLetter, "currentLetter should be 'O'");
//
//        controller.cwStack.push(".--.");
//        controller.letterStack.push("P");
//        controller.generateNewLetter();
//        assertEquals(".--.", controller.currentCW, "currentCW should be '.--.'");
//        assertEquals("P", controller.currentLetter, "currentLetter should be 'P'");
//
//        controller.cwStack.push("--.-");
//        controller.letterStack.push("Q");
//        controller.generateNewLetter();
//        assertEquals("--.-", controller.currentCW, "currentCW should be '--.-'");
//        assertEquals("Q", controller.currentLetter, "currentLetter should be 'Q'");
//
//        controller.cwStack.push(".-.");
//        controller.letterStack.push("R");
//        controller.generateNewLetter();
//        assertEquals(".-.", controller.currentCW, "currentCW should be '.-.'");
//        assertEquals("R", controller.currentLetter, "currentLetter should be 'R'");
//
//        controller.cwStack.push("...");
//        controller.letterStack.push("S");
//        controller.generateNewLetter();
//        assertEquals("...", controller.currentCW, "currentCW should be '...'");
//        assertEquals("S", controller.currentLetter, "currentLetter should be 'S'");
//
//        controller.cwStack.push("-");
//        controller.letterStack.push("T");
//        controller.generateNewLetter();
//        assertEquals("-", controller.currentCW, "currentCW should be '-'");
//        assertEquals("T", controller.currentLetter, "currentLetter should be 'T'");
//
//        controller.cwStack.push("..-");
//        controller.letterStack.push("U");
//        controller.generateNewLetter();
//        assertEquals("..-", controller.currentCW, "currentCW should be '..-'");
//        assertEquals("U", controller.currentLetter, "currentLetter should be 'U'");
//
//        controller.cwStack.push("...-");
//        controller.letterStack.push("V");
//        controller.generateNewLetter();
//        assertEquals("...-", controller.currentCW, "currentCW should be '...-'");
//        assertEquals("V", controller.currentLetter, "currentLetter should be 'V'");
//
//        controller.cwStack.push(".--");
//        controller.letterStack.push("W");
//        controller.generateNewLetter();
//        assertEquals(".--", controller.currentCW, "currentCW should be '.--'");
//        assertEquals("W", controller.currentLetter, "currentLetter should be 'W'");
//
//        controller.cwStack.push("-..-");
//        controller.letterStack.push("X");
//        controller.generateNewLetter();
//        assertEquals("-..-", controller.currentCW, "currentCW should be '-..-'");
//        assertEquals("X", controller.currentLetter, "currentLetter should be 'X'");
//
//        controller.cwStack.push("-.--");
//        controller.letterStack.push("Y");
//        controller.generateNewLetter();
//        assertEquals("-.--", controller.currentCW, "currentCW should be '-.--'");
//        assertEquals("Y", controller.currentLetter, "currentLetter should be 'Y'");
//
//        controller.cwStack.push("--..");
//        controller.letterStack.push("Z");
//        controller.generateNewLetter();
//        assertEquals("--..", controller.currentCW, "currentCW should be '--..'");
//        assertEquals("Z", controller.currentLetter, "currentLetter should be 'Z'");
//
//    }
//
//    @Test
//    void testGenerateRandomOrder() {
//        controller.generateRandomOrder();
//        assertFalse(controller.cwStack.isEmpty(), "cwStack should not be empty.");
//        assertFalse(controller.letterStack.isEmpty(), "letterStack should not be empty.");
//    }
//}