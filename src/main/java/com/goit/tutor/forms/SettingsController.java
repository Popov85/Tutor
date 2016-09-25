package com.goit.tutor.forms;

import com.goit.tutor.session.Settings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by Андрей on 12.09.2016.
 */
public class SettingsController {

        private StartController startController;

        @FXML
        private Button ok;

        @FXML
        public TextField cards;

        @FXML
        public CheckBox check;

        @FXML
        public void apply() {
                Stage stage = (Stage) this.ok.getScene().getWindow();
                Settings settings = new Settings(
                        Integer.valueOf(this.cards.getText()),
                        this.check.isSelected()
                );
                //System.out.println(settings.toString());
                startController.setSettings(settings);
                stage.close();
        }

        public void initialize(StartController startController, Settings currentSettings) {
                this.startController = startController;
                this.cards.setText(String.valueOf(currentSettings.getPerSession()));
                this.check.setSelected(currentSettings.isCheckMethod());
        }
}
