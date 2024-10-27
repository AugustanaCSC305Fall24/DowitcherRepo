package org.example;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class RadioFunctions {

    public static List<Object> checkTranslation(String userTranslation, String currentCW, String textSize) {
        TextFlow checkedUserInput = new TextFlow();
        TextFlow correctTranslation = new TextFlow();
        Text checkedUserLetter;
        Text checkedCorrectLetter;
        int numIncorrect = 0;

        for (int i = 0; i < userTranslation.length(); i++) {

            // Prevents program from crashing if message put in by user
            // is longer than the correct message
            checkedUserLetter = new Text(Character.toString(userTranslation.charAt(i)).toUpperCase());
            if (i >= currentCW.length()) {
                checkedUserLetter.setStyle("-fx-fill: red;");
                checkedCorrectLetter = new Text("");
            } else {

                // Determines if the character is correct or incorrect
                // and sets it to the appropriate color
                if (Character.toUpperCase(userTranslation.charAt(i)) == Character.toUpperCase(currentCW.charAt(i))) {
                    checkedUserLetter.setStyle("-fx-fill: green;");
                    checkedCorrectLetter = new Text(Character.toString(currentCW.charAt(i)));
                } else {
                    checkedUserLetter.setStyle("-fx-fill: red;");
                    checkedCorrectLetter = new Text("_");
                    numIncorrect++;
                }
            }

            checkedUserInput.setStyle("-fx-font-size: " + textSize + "px;");
            checkedCorrectLetter.setStyle("-fx-font-size: " + textSize + "px;");

            checkedUserInput.getChildren().addAll(checkedUserLetter);
            correctTranslation.getChildren().addAll(checkedCorrectLetter);

        }

        // Displays the remaining cwMessage as incorrect if user's input
        // is shorter than the correct message.
        if (currentCW.length() > userTranslation.length()) {
            for (int i = userTranslation.length(); i < currentCW.length(); i++) {
                checkedCorrectLetter = new Text("_");
                checkedCorrectLetter.setStyle("-fx-font-size: " + textSize + "px;");
                correctTranslation.getChildren().addAll(checkedCorrectLetter);
                numIncorrect++;
            }
        }

        boolean isTrue = true;
        if (numIncorrect > 0) {
            isTrue = false;
        }

        List<Object> returnList = new ArrayList<>();
        returnList.add(checkedUserInput);
        returnList.add(correctTranslation);
        returnList.add(isTrue);

        return returnList;

    }


}
