package com.goit.tutor.forms;

import com.goit.tutor.session.*;
import com.goit.tutor.session.Comparator;
import com.goit.tutor.xml.XMLParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.StopWatch;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Tutor app GUI
 */
public class StartController implements Initializable{

        private final User user= new User(System.getProperty("user.name"));

        private long startTime;

        private long stopTime;

        private String path;

        private Settings settings;

        private ArrayList<Card> listOfCards;

        private List<StatisticsByCard> listOfMistakes = new ArrayList<StatisticsByCard>();

        /* List of Cards whose attributes were changed during a session*/
        private HashMap<Integer, Card> cardsToBeSentBackIntoXML = new HashMap<Integer, Card>();

        /*Cards that have changed their status during a small startSmallSession*/
        private ArrayList<Card> cardsHavingChangedStatus = new ArrayList<Card>();

        private int currentId;

        private Card currentCard;

        private int finishId;

        // needs to change EN-RU/RU-EN
        private int counter = 0;

        private int counterOfPrompts = 0;

        private int counterOfCorrect = 0;

        private int counterOfIncorrect = 0;

        private int counterOfCardsHavingChangedStatus = 0;

        private Prompt prompt;

        private UtilitySession utility = new UtilitySession();

        private Comparator comparator;

        private Timer timer;

        private long timeElapsed;

        @FXML
        private Label question;

        @FXML
        private TextField translation;

        @FXML
        private Button start;

        @FXML
        private Button checkButton;

        @FXML
        private Button nextButton;

        @FXML
        private Button startoverButton;

        @FXML
        private Button promptButton;

        @FXML
        private Label result;

        @FXML
        private Label answer;

        @FXML
        private Label time;

        @FXML
        private Label prompts;

        @FXML
        private Label correct;

        @FXML
        private Label incorrect;

        @FXML
        private Label effectiveness;

        @FXML
        private Label left;

        private StopWatch stopWatch = new StopWatch();

        @FXML
        private ProgressBar progress;

        @FXML
        private MenuItem set;

        @FXML
        private MenuItem file;

        @Override// set default settings
        public void initialize(URL location, ResourceBundle resources) {
                this.settings = new Settings();
        }

        public void setSettings(Settings newSettings) {
                this.settings = newSettings;
        }

        @FXML
        private void upload(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open XML dictionary");
                Stage stage = new Stage();
                File file = fileChooser.showOpenDialog(stage);
                if(file.exists()){
                        path = file.getPath();
                        listOfCards = new XMLParser().getCardsFromXML(path);
                }else{
                        System.out.println("File not found!");
                }
        }

        @FXML
        private void startSession(ActionEvent actionEvent) {
                startSession();
        }

        private void startSession() {
                try {// checkButton if a dict was uploaded
                        if (listOfCards==null) throw new IllegalStateException();
                        // shuffle
                        listOfCards = utility.shuffleCards(listOfCards,
                                settings.getPerSession());
                        // set the last card as the current one
                        currentId = listOfCards.size();
                        finishId = currentId - settings.getPerSession();
                        nextQuestion();
                        startTime = System.nanoTime();
                        stopWatch.start();
                        startTimer();
                        this.start.setVisible(false);
                        this.checkButton.setVisible(true);
                        this.file.setDisable(true);
                        this.set.setDisable(true);
                } catch (IllegalStateException e) {
                        displayMessage("Warning!","Description",
                                "Please, upload an XML-based dictionary compatible " +
                                "with Abbyy Lingvo Tutor x3");
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        @FXML
        private void nextQuestion(ActionEvent actionEvent) {
                this.translation.setText("");
                this.result.setText("");
                this.answer.setText("");
                nextQuestion();
        }

        private void nextQuestion() {
                counter++;
                currentId--;
                currentCard = listOfCards.get(currentId);
                displayQuestion(utility.getShownPart(currentCard, counter));
                prompt = new Prompt(utility.getHiddenPart(currentCard, counter));
                this.promptButton.setDisable(false);
                this.checkButton.setVisible(true);
                this.nextButton.setVisible(false);
        }

        @FXML
        private void checkAnswer(ActionEvent actionEvent) {
                comparator = new Comparator(prompt, settings.isCheckMethod());
                boolean result = comparator.compareWords(getUsersAnswer(),
                        utility.getHiddenPart(currentCard, counter));
                if (result) {
                        counterOfCorrect++;
                        if (utility.incrementCardsAttributes(currentCard)) {
                                counterOfCardsHavingChangedStatus++;
                                cardsHavingChangedStatus.add(currentCard);
                                counterOfCardsHavingChangedStatus++;
                        }
                        showPositiveResult();
                } else {
                        counterOfIncorrect++;
                        utility.decrementCardsAttributes(currentCard);
                        showNegativeResult();
                        // fill statistics
                        StatisticsByCard mistake = new StatisticsByCard(currentCard.wordId,
                                utility.getHiddenPart(currentCard, counter),
                                getUsersAnswer(), prompt.getCurrentPrompt());
                        listOfMistakes.add(mistake);
                }
                showAnswer(utility.getHiddenPart(currentCard, counter));
                updateEffectiveness();
                updateProgress();
                updateHashMap(currentCard);
                if (currentId==finishId) {
                        finishSmallSession();
                } else {
                        this.checkButton.setVisible(false);
                        this.nextButton.setVisible(true);
                }
        }

        public String getUsersAnswer() {
                return this.translation.getText();
        }

        private void showAnswer(String hiddenPart) {
                this.answer.setText(hiddenPart);
        }

        private void updateHashMap(Card card) {
                cardsToBeSentBackIntoXML.put(card.wordId, card);
        }

        private void updateProgress() {
                double fill = (double) counter/settings.getPerSession();
                progress.setProgress(fill);
                int cardsLeft = settings.getPerSession()-counter;
                left.setText(String.valueOf(cardsLeft));
        }

        private void finishSmallSession() {
                timer.cancel();
                stopTime = System.nanoTime();
                timeElapsed = (stopTime - startTime)/1000000;
                this.checkButton.setVisible(false);
                this.nextButton.setVisible(false);
                this.startoverButton.setVisible(true);
                saveCardsIntoXML();
                utility.saveStatistics(counterOfIncorrect, counterOfPrompts, counterOfCorrect,
                        counterOfCardsHavingChangedStatus, listOfMistakes, timeElapsed);
                this.file.setDisable(false);
                this.set.setDisable(false);
                showStatistics();
        }

        private void displayQuestion(String question) {
                if (this.question!=null) {
                        this.question.setText(question);
                }
        }

        private void emptyCardsToBeSentBackIntoXML() {
                cardsToBeSentBackIntoXML.clear();
        }

        private void showNegativeResult() {
                this.result.setStyle("-fx-text-fill: red;");
                this.result.setText("Sorry...");
                this.incorrect.setText(String.valueOf(counterOfIncorrect));
        }

        private void updateEffectiveness() {
                this.effectiveness.setText(utility.convertEffectiveness(counterOfCorrect, counter));
        }

        private void showPositiveResult() {
                this.result.setStyle("-fx-text-fill: green;");
                this.result.setText("Well done!!!");
                this.correct.setText(String.valueOf(counterOfCorrect));
        }

        @FXML // prompts processor
        public void showPrompt(ActionEvent actionEvent) {
                if (prompt.showPrefix()) {
                        this.translation.setText(prompt.getCurrentPrompt());
                        this.counterOfPrompts++;
                        this.prompts.setText(String.valueOf(counterOfPrompts));
                } else {//run out of prompts
                        this.promptButton.setDisable(true);
                }
        }

        private void displayMessage(String title, String header, String content) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle(title);
                alert.setHeaderText(header);
                alert.setContentText(content);
                alert.showAndWait();
        }

        private void saveCardsIntoXML() {
                utility.saveSession(cardsToBeSentBackIntoXML, path);
        }

        @FXML
        private void startOver(){
                emptyCardsToBeSentBackIntoXML();
                resetControls();
                resetIntersessionCounters();
                stopWatch.reset();
                startSession();
                this.startoverButton.setVisible(false);
                this.checkButton.setVisible(true);
        }

        private void resetIntersessionCounters() {
                counterOfPrompts = 0;
                counterOfCorrect = 0;
                counterOfIncorrect = 0;
                listOfMistakes.clear();
                counterOfCardsHavingChangedStatus = 0;
                cardsHavingChangedStatus.clear();
                counter = 0;
        }

        private void resetControls() {
                this.translation.setText("");
                this.result.setText("");
                this.answer.setText("");
                this.question.setText("...");
                this.effectiveness.setText("0%");
                this.prompts.setText("0");
                this.correct.setText("0");
                this.incorrect.setText("0");
                this.left.setText("_");
                this.progress.setProgress(0);
                this.time.setText("00:00:00");
        }

        private void startTimer(){
                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                                Platform.runLater(new Runnable(){
                                        public void run(){
                                                time.setText(stopWatch.toString());
                                        }
                                });
                        }
                };
                timer.schedule(timerTask, 0, 50);
        }

        @FXML
        private void showAbout() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("About.fxml"));
                Parent root1 = null;
                try {
                        root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.setTitle("About");
                stage.setScene(new Scene(root1));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
        }

        private void showStatistics() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Statistics.fxml"));
                Parent root1 = null;
                try {
                        root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Stage statistics = new Stage();
                statistics.setTitle("Statistics");
                statistics.setScene(new Scene(root1));
                StatisticsController controller = fxmlLoader.<StatisticsController>getController();
                controller.initialize(utility.convertTime(timeElapsed),
                        String.valueOf(counterOfCorrect),
                        String.valueOf(counterOfIncorrect),
                        utility.convertEffectiveness(counterOfCorrect, counter));
                statistics.initModality(Modality.APPLICATION_MODAL);
                statistics.show();
        }

        @FXML
        private void showSettings() {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Settings.fxml"));
                Parent root1 = null;
                try {
                        root1 = (Parent) fxmlLoader.load();
                } catch (IOException e) {
                        e.printStackTrace();
                }
                Stage settings = new Stage();
                settings.setTitle("Settings");
                settings.setScene(new Scene(root1));
                SettingsController controller = fxmlLoader.<SettingsController>getController();
                controller.initialize(this, this.settings);
                settings.initModality(Modality.APPLICATION_MODAL);
                settings.show();
        }

        @FXML
        private void exit() {
                Platform.exit();
        }
}
