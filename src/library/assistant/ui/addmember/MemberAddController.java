package library.assistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listmembers.MemberListController;

import java.net.URL;
import java.util.ResourceBundle;

public class MemberAddController implements Initializable {

    DatabaseHandler handler;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton saveButton;
    @FXML
    private JFXButton cancelButton;

    private Boolean isInEditMode = Boolean.FALSE;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = DatabaseHandler.getInstance();

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();

    }

    @FXML
    private void addMember(ActionEvent event) {
        String mName = name.getText();
        String mID = id.getText();
        String mMobile = mobile.getText();
        String mEmail = email.getText();

        Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
        if (flag) {
            AlertMaker.showErrorMessage("Error", "Please Enter All Fields");
            return;
        }

        if (isInEditMode) {
            handleMemberEdit();
            return;
        }

        String st = "INSERT INTO MEMBER VALUES ("
                + "'" + mID + "',"
                + "'" + mName + "',"
                + "'" + mMobile + "',"
                + "'" + mEmail + "'"
                + ")";

        System.out.println(st);
        if (handler.execAction(st)) {
            AlertMaker.showSimpleAlert("Success", "Member Added Successfully");
        } else {
            AlertMaker.showErrorMessage("Error", "Member Addition Failed");
        }
        return;
    }

    private void handleMemberEdit() {
        MemberListController.Member member = new MemberListController.Member(id.getText(), name.getText(),
                mobile.getText(), email.getText());

        if (DatabaseHandler.getInstance().updateMember(member)) {
            AlertMaker.showSimpleAlert("Success", "Member updated successfully");
        } else {
            AlertMaker.showErrorMessage("Failed", "Can't Update member");
        }
    }


    public void inflateUI(MemberListController.Member member) {
        name.setText(member.getName());
        mobile.setText(member.getMobile());
        id.setText(member.getId());
        email.setText(member.getEmail());
        id.setEditable(false);
        isInEditMode = Boolean.TRUE;
    }
}
