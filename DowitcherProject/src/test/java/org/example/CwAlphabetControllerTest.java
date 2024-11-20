package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.ui.practice.CwAlphabetController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.text.TextFlow;

import java.util.Stack;

public class CwAlphabetControllerTest {

    private CwAlphabetController controller;

    @BeforeEach
    void setUp() {
        controller = new CwAlphabetController();
        controller.cwStack = new Stack<>();
        controller.letterStack = new Stack<>();
        controller.currentLetterTextFlow = new TextFlow();
    }

    @Test
    void testGenerateNewLetter() {
        controller.cwStack.push(".-");
        controller.letterStack.push("A");
        controller.generateNewLetter();
        assertEquals(".-", controller.currentCW, "currentCW should be '.-'");
        assertEquals("A", controller.currentLetter, "currentLetter should be 'A'");
    }

    @Test
    void testGenerateRandomOrder() {
        controller.generateRandomOrder();
        assertFalse(controller.cwStack.isEmpty(), "cwStack should not be empty.");
        assertFalse(controller.letterStack.isEmpty(), "letterStack should not be empty.");
    }
}