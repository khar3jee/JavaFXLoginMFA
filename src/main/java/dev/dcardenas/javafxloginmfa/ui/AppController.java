package dev.dcardenas.javafxloginmfa.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    public Button logout;

    @FXML
    protected void userLogoutButtonClick(ActionEvent event) {
        ViewManager.switchView("/dev/dcardenas/javafxloginmfa/views/login-view.fxml");
        // on logout sign user out and return to login scene
        // should have a token or cookie which provides status
    }
}
