package dev.dcardenas.javafxloginmfa.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class AppController {
    public Button logout;

    @FXML
    protected void userLogoutButtonClick(ActionEvent event) {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/logout-view.fxml");
        // on logout sign user out and return to login scene
    }
}
