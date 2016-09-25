package com.goit.tutor.forms;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Created by Андрей on 11.09.2016.
 */
public class AboutController {

        @FXML
        private Button ok;

        @FXML
        public void close() {
                Stage stage = (Stage) ok.getScene().getWindow();
                // do what you have to do
                stage.close();
        }
}
