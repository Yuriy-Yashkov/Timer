package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Timers {
    @FXML
    Button cancelButton;
    @FXML
    Button minutes10;
    @FXML
    Button minutes15;

    private Main fxmlLoader;
    Main main = fxmlLoader;

    @FXML
    public String onActionMinutes10() {
        return "10";
    }
    @FXML
    public String onActionMinutes15() {
        return "15";
    }

    @FXML
    public void onActionCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
