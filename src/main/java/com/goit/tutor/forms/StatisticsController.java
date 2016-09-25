package com.goit.tutor.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by Андрей on 11.09.2016.
 */
public class StatisticsController{

        @FXML
        private Button ok;

        @FXML
        public Label time;

        @FXML
        private Label correct;

        @FXML
        private Label incorrect;

        @FXML
        private Label effectiveness;

        @FXML
        public void close() {
                Stage stage = (Stage) ok.getScene().getWindow();
                stage.close();
        }

        public void initialize(String time, String correct, String incorrect, String effectiveness) {
                this.time.setText(time);
                this.correct.setText(correct);
                this.incorrect.setText(incorrect);
                this.effectiveness.setText(effectiveness);
        }

}
