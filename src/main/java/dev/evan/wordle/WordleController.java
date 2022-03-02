package dev.evan.wordle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.util.Optional;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;
import java.util.HashMap;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class WordleController {

    @FXML
    private Rectangle rec0x0;

    @FXML
    private Rectangle rec0x1;

    @FXML
    private Rectangle rec0x2;

    @FXML
    private Rectangle rec0x3;

    @FXML
    private Rectangle rec0x4;

    @FXML
    private Rectangle rec1x0;

    @FXML
    private Rectangle rec1x1;

    @FXML
    private Rectangle rec1x2;

    @FXML
    private Rectangle rec1x3;

    @FXML
    private Rectangle rec1x4;

    @FXML
    private Rectangle rec2x0;

    @FXML
    private Rectangle rec2x1;

    @FXML
    private Rectangle rec2x2;

    @FXML
    private Rectangle rec2x3;

    @FXML
    private Rectangle rec2x4;

    @FXML
    private Rectangle rec3x0;

    @FXML
    private Rectangle rec3x1;

    @FXML
    private Rectangle rec3x2;

    @FXML
    private Rectangle rec3x3;

    @FXML
    private Rectangle rec3x4;

    @FXML
    private Rectangle rec4x0;

    @FXML
    private Rectangle rec4x1;

    @FXML
    private Rectangle rec4x2;

    @FXML
    private Rectangle rec4x3;

    @FXML
    private Rectangle rec4x4;

    @FXML
    private Rectangle rec5x0;

    @FXML
    private Rectangle rec5x1;

    @FXML
    private Rectangle rec5x2;

    @FXML
    private Rectangle rec5x3;

    @FXML
    private Rectangle rec5x4;

    @FXML
    private Text letter0x0;

    @FXML
    private Text letter0x1;

    @FXML
    private Text letter0x2;

    @FXML
    private Text letter0x3;

    @FXML
    private Text letter0x4;

    @FXML
    private Text letter1x0;

    @FXML
    private Text letter1x1;

    @FXML
    private Text letter1x2;

    @FXML
    private Text letter1x3;

    @FXML
    private Text letter1x4;

    @FXML
    private Text letter2x0;

    @FXML
    private Text letter2x1;

    @FXML
    private Text letter2x2;

    @FXML
    private Text letter2x3;

    @FXML
    private Text letter2x4;

    @FXML
    private Text letter3x0;

    @FXML
    private Text letter3x1;

    @FXML
    private Text letter3x2;

    @FXML
    private Text letter3x3;

    @FXML
    private Text letter3x4;

    @FXML
    private Text letter4x0;

    @FXML
    private Text letter4x1;

    @FXML
    private Text letter4x2;

    @FXML
    private Text letter4x3;

    @FXML
    private Text letter4x4;

    @FXML
    private Text letter5x0;

    @FXML
    private Text letter5x1;

    @FXML
    private Text letter5x2;

    @FXML
    private Text letter5x3;

    @FXML
    private Text letter5x4;

    @FXML
    private Text invalidText;

    @FXML
    private TextField wordIn;

    @FXML
    void resetGame(ActionEvent event) {
        gameState = 0;
        guessNum = 0;
        word = pickWord("/wordle-answers-alphabetical.txt");
        answerIndex = word.toCharArray();
        invalidText.setText(" ");
        resetColor();
        clearRows();
    }

    @FXML
    void askRules(ActionEvent event) {
        showDialog("rules");
    }

    @FXML
    void checkWord(ActionEvent event) {
        //Sytem.out.println("Word to guess: " + word);
        if (gameState == 0) {
            invalidText.setText(" ");
            guess = wordIn.getText().toLowerCase();
            if (validateLength(guess) == false) {
                invalidText.setText("Invalid input");
            } else if (validateWord(guess)) {
                wordIndex = guess.toCharArray();
                answerMap = generateDictionary(answerIndex);
                //System.out.println(word);
                fillRow();
                if (checkWin()) {
                    gameState = 1;
                    showDialog("win");
                }
                //printColor();
                resetColor();
                guessNum += 1;
            } else {
                invalidText.setText("Invalid word");
            }
            if (guessNum > 5) {
                gameState = 1;
                showDialog("loss");
            }
        }
    }

    int gameState = 0;
    int guessNum = 0;
    Color gold = Color.rgb(201, 180, 88);
    Color green = Color.rgb(106, 170, 100);
    Color gray = Color.rgb(120, 124, 126);
    char[] wordIndex;
    String word = pickWord("/wordle-answers-alphabetical.txt");
    char[] answerIndex = word.toCharArray();
    //0 is gray, 1 is yellow, 2 is green
    Integer[] rowColor = new Integer[]{0, 0, 0, 0, 0};
    String guess;
    HashMap<String, Integer> word_guesses = textfile_to_hashmap("/wordle-allowed-guesses.txt");
    HashMap<String, Integer> word_answers = textfile_to_hashmap("/wordle-answers-alphabetical.txt");
    HashMap<Integer, Color> colorAssign = assignColors();
    HashMap<Character, Integer> guessMap;
    HashMap<Character, Integer> answerMap;

    void showDialog(String outcome) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (outcome.equals("win")) {
            alert.setTitle("You Won!");
            alert.setContentText("You guessed the word!");
        } else if (outcome.equals("loss")) {
            alert.setTitle("You Lost...");
            alert.setContentText("You ran out of guesses.");
        } else if (outcome.equals("rules")) {
            alert.setTitle("Rules");
            alert.setContentText("You have to guess the Wordle in six goes or less.\n" +
                    "Every word you enter must be in the word list. That hasn't been disclosed, but presumably it's based on a dictionary.\n" +
                    "A correct letter turns green\n" +
                    "A correct letter in the wrong place turns yellow\n" +
                    "An incorrect letter turns gray\n" +
                    "Letters can be used more than once");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) {
            //System.out.println("Alert closed");
        } else if (result.get() == ButtonType.OK) {
            //System.out.println("OK!");
        } else if (result.get() == ButtonType.CANCEL) {
            //System.out.println("Never!");
        }

    }

    HashMap<Character, Integer> generateDictionary(char[] input) {
        HashMap<Character, Integer> wordMap = new HashMap<Character, Integer>();
        for (Character letter : input) {
            if (wordMap.containsKey(letter)) {
                wordMap.put(letter, (wordMap.get(letter) + 1));
            } else {
                wordMap.put(letter, 1);
            }
        }
        return wordMap;
    }

    boolean checkWin() {
        for (Integer i : rowColor) {
            if (i != 2) {
                return false;
            }
        }
        return true;
    }

    HashMap<Integer, Color> assignColors() {
        HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>();
        colorMap.put(0, gray);
        colorMap.put(1, gold);
        colorMap.put(2, green);
        return colorMap;
    }

    void printColor() {
        for (int i = 0; i < 5; i++) {
            System.out.print(rowColor[i] + " ");
        }
        System.out.print("\n");
    }

    void fillRow() {
        checkGreen();
        checkYellow();
        switch (guessNum) {
            case 0:
                letter0x0.setText(String.valueOf(wordIndex[0]));
                rec0x0.setFill(colorAssign.get(rowColor[0]));
                letter0x1.setText(String.valueOf(wordIndex[1]));
                rec0x1.setFill(colorAssign.get(rowColor[1]));
                letter0x2.setText(String.valueOf(wordIndex[2]));
                rec0x2.setFill(colorAssign.get(rowColor[2]));
                letter0x3.setText(String.valueOf(wordIndex[3]));
                rec0x3.setFill(colorAssign.get(rowColor[3]));
                letter0x4.setText(String.valueOf(wordIndex[4]));
                rec0x4.setFill(colorAssign.get(rowColor[4]));
                break;
            case 1:
                letter1x0.setText(String.valueOf(wordIndex[0]));
                rec1x0.setFill(colorAssign.get(rowColor[0]));
                letter1x1.setText(String.valueOf(wordIndex[1]));
                rec1x1.setFill(colorAssign.get(rowColor[1]));
                letter1x2.setText(String.valueOf(wordIndex[2]));
                rec1x2.setFill(colorAssign.get(rowColor[2]));
                letter1x3.setText(String.valueOf(wordIndex[3]));
                rec1x3.setFill(colorAssign.get(rowColor[3]));
                letter1x4.setText(String.valueOf(wordIndex[4]));
                rec1x4.setFill(colorAssign.get(rowColor[4]));
                break;
            case 2:
                letter2x0.setText(String.valueOf(wordIndex[0]));
                rec2x0.setFill(colorAssign.get(rowColor[0]));
                letter2x1.setText(String.valueOf(wordIndex[1]));
                rec2x1.setFill(colorAssign.get(rowColor[1]));
                letter2x2.setText(String.valueOf(wordIndex[2]));
                rec2x2.setFill(colorAssign.get(rowColor[2]));
                letter2x3.setText(String.valueOf(wordIndex[3]));
                rec2x3.setFill(colorAssign.get(rowColor[3]));
                letter2x4.setText(String.valueOf(wordIndex[4]));
                rec2x4.setFill(colorAssign.get(rowColor[4]));
                break;
            case 3:
                letter3x0.setText(String.valueOf(wordIndex[0]));
                rec3x0.setFill(colorAssign.get(rowColor[0]));
                letter3x1.setText(String.valueOf(wordIndex[1]));
                rec3x1.setFill(colorAssign.get(rowColor[1]));
                letter3x2.setText(String.valueOf(wordIndex[2]));
                rec3x2.setFill(colorAssign.get(rowColor[2]));
                letter3x3.setText(String.valueOf(wordIndex[3]));
                rec3x3.setFill(colorAssign.get(rowColor[3]));
                letter3x4.setText(String.valueOf(wordIndex[4]));
                rec3x4.setFill(colorAssign.get(rowColor[4]));
                break;
            case 4:
                letter4x0.setText(String.valueOf(wordIndex[0]));
                rec4x0.setFill(colorAssign.get(rowColor[0]));
                letter4x1.setText(String.valueOf(wordIndex[1]));
                rec4x1.setFill(colorAssign.get(rowColor[1]));
                letter4x2.setText(String.valueOf(wordIndex[2]));
                rec4x2.setFill(colorAssign.get(rowColor[2]));
                letter4x3.setText(String.valueOf(wordIndex[3]));
                rec4x3.setFill(colorAssign.get(rowColor[3]));
                letter4x4.setText(String.valueOf(wordIndex[4]));
                rec4x4.setFill(colorAssign.get(rowColor[4]));
                break;
            case 5:
                letter5x0.setText(String.valueOf(wordIndex[0]));
                rec5x0.setFill(colorAssign.get(rowColor[0]));
                letter5x1.setText(String.valueOf(wordIndex[1]));
                rec5x1.setFill(colorAssign.get(rowColor[1]));
                letter5x2.setText(String.valueOf(wordIndex[2]));
                rec5x2.setFill(colorAssign.get(rowColor[2]));
                letter5x3.setText(String.valueOf(wordIndex[3]));
                rec5x3.setFill(colorAssign.get(rowColor[3]));
                letter5x4.setText(String.valueOf(wordIndex[4]));
                rec5x4.setFill(colorAssign.get(rowColor[4]));
                break;
        }
    }

    void clearRows() {
        letter0x0.setText(" ");
        rec0x0.setFill(Color.WHITE);
        letter0x1.setText(" ");
        rec0x1.setFill(Color.WHITE);
        letter0x2.setText(" ");
        rec0x2.setFill(Color.WHITE);
        letter0x3.setText(" ");
        rec0x3.setFill(Color.WHITE);
        letter0x4.setText(" ");
        rec0x4.setFill(Color.WHITE);

        letter1x0.setText(" ");
        rec1x0.setFill(Color.WHITE);
        letter1x1.setText(" ");
        rec1x1.setFill(Color.WHITE);
        letter1x2.setText(" ");
        rec1x2.setFill(Color.WHITE);
        letter1x3.setText(" ");
        rec1x3.setFill(Color.WHITE);
        letter1x4.setText(" ");
        rec1x4.setFill(Color.WHITE);

        letter2x0.setText(" ");
        rec2x0.setFill(Color.WHITE);
        letter2x1.setText(" ");
        rec2x1.setFill(Color.WHITE);
        letter2x2.setText(" ");
        rec2x2.setFill(Color.WHITE);
        letter2x3.setText(" ");
        rec2x3.setFill(Color.WHITE);
        letter2x4.setText(" ");
        rec2x4.setFill(Color.WHITE);

        letter3x0.setText(" ");
        rec3x0.setFill(Color.WHITE);
        letter3x1.setText(" ");
        rec3x1.setFill(Color.WHITE);
        letter3x2.setText(" ");
        rec3x2.setFill(Color.WHITE);
        letter3x3.setText(" ");
        rec3x3.setFill(Color.WHITE);
        letter3x4.setText(" ");
        rec3x4.setFill(Color.WHITE);

        letter4x0.setText(" ");
        rec4x0.setFill(Color.WHITE);
        letter4x1.setText(" ");
        rec4x1.setFill(Color.WHITE);
        letter4x2.setText(" ");
        rec4x2.setFill(Color.WHITE);
        letter4x3.setText(" ");
        rec4x3.setFill(Color.WHITE);
        letter4x4.setText(" ");
        rec4x4.setFill(Color.WHITE);

        letter5x0.setText(" ");
        rec5x0.setFill(Color.WHITE);
        letter5x1.setText(" ");
        rec5x1.setFill(Color.WHITE);
        letter5x2.setText(" ");
        rec5x2.setFill(Color.WHITE);
        letter5x3.setText(" ");
        rec5x3.setFill(Color.WHITE);
        letter5x4.setText(" ");
        rec5x4.setFill(Color.WHITE);
    }

    void checkGreen() {
        for (int i = 0; i < 5; i++) {
            if (wordIndex[i] == answerIndex[i]) {
                //System.out.println("Found green on " + Integer.toString(i));
                if (answerMap.get(wordIndex[i]) > 0) {
                    answerMap.put(wordIndex[i], answerMap.get(wordIndex[i]) - 1);
                    if (answerMap.get(wordIndex[i]) == 0) {
                        answerMap.remove(wordIndex[i]);
                    }
                }
                rowColor[i] = 2;
            }
        }
    }

    void checkYellow() {
        //words with double letters are a problem, pupil
        for (int i = 0; i < 5; i++) {
            if (rowColor[i] == 0) {
                if (answerMap.containsKey(wordIndex[i])) {
                    if (answerMap.get(wordIndex[i]) > 0) {
                        answerMap.put(wordIndex[i], answerMap.get(wordIndex[i]) - 1);
                        if (answerMap.get(wordIndex[i]) == 0) {
                            answerMap.remove(wordIndex[i]);
                        }
                    }
                    //System.out.println("Found yellow on " + Integer.toString(i));
                    rowColor[i] = 1;
                }
            }
        }
    }

    void resetColor() {
        for (int i = 0; i < 5; i++) {
            rowColor[i] = 0;
        }
    }

    HashMap<String, Integer> textfile_to_hashmap(String filepath) {
        HashMap<String, Integer> hash_map = new HashMap<String, Integer>();
        try {
            InputStream inputStream = getClass().getResourceAsStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int c = 0;
            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                c++;
                hash_map.put(line, c);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("error, please insert the valid path / text files for textfile_to_hashmap");
        }
        return hash_map;
    }

    String pickWord(String filepath) {
        String chosen = "";
        int randomNum = ThreadLocalRandom.current().nextInt(1, 2316);
        try {
            InputStream inputStream = getClass().getResourceAsStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            int c = 0;

            String line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                c++;
                if (randomNum == c) {
                    chosen = line;
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("error, please insert the valid path / text files for pickWord");
        }
        return chosen;
    }

    boolean validateWord(String input) {
        if (word_guesses.containsKey(input))
            return true;
        if (word_answers.containsKey(input))
            return true;
        return false;
    }

    boolean validateLength(String input) {
        if (input.length() == 5)
            return true;
        return false;
    }
}