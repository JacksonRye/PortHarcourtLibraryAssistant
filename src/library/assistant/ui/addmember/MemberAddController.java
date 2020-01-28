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
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		handler =  DatabaseHandler.getInstance();
		
	}
	
	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
		
	}
	
	@FXML
	private void addMember(ActionEvent event) {
		String mName= name.getText();
		String mID = id.getText();
		String mMobile = mobile.getText();
		String mEmail = email.getText();
		
		Boolean flag = mName.isEmpty() || mID.isEmpty() || mMobile.isEmpty() || mEmail.isEmpty();
		if (flag) {
            AlertMaker.showErrorMessage("Error", "Please Enter All Fields");
            return;
        }
		
		String st = "INSERT INTO MEMBER VALUES ("
				+ "'" + mID + "',"
				+ "'" + mName + "',"
				+ "'" + mMobile + "',"
				+ "'" + mEmail + "'"
						+ ")";
		
		System.out.println(st);
		if(handler.execAction(st)) {
            AlertMaker.showSimpleAlert("Success", "Member Added Successfully");
        } else {
            AlertMaker.showErrorMessage("Error", "Member Addition Failed");
        }

		
		
		
	}
	
	
	

}
