package library.assistant.ui.login;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    Preferences preferences;
    @FXML
    private JFXTextField username;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label titleLabel;

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        titleLabel.setText("Library Assistant Login");
        titleLabel.setStyle("-fx-background-color:black;-fx-text-fill:white");


        String uName = username.getText();
        String pwrd = DigestUtils.sha1Hex(password.getText());

        if (uName.equals(preferences.getUsername()) && pwrd.equals(preferences.getPassword())) {
            closeStage();
            loadMain();

        } else {
            titleLabel.setText("Invalid Credentials");
            titleLabel.setStyle("-fx-background-color:#d32f2f;-fx-text-fill:white");
        }

    }

    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preferences = Preferences.getPreferences();

    }

    void loadMain() {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/Main.fxml"));

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
