package library.assistant.ui.addbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class BookAddController implements Initializable {
	
	@FXML
	private JFXTextField title;
	
	@FXML
	private JFXTextField id;
	
	@FXML
	private JFXTextField publisher;
	
	@FXML
	private JFXTextField author;
	
	@FXML
	private JFXButton saveButton;
	
	@FXML
	private JFXButton cancelButton;
	
	@FXML
	private AnchorPane rootPane;
	
	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler =  DatabaseHandler.getInstance();

		checkData();
		
	}
	
	private void checkData() {
		String qu = "SELECT title FROM BOOK";
		ResultSet rs = databaseHandler.execQuery(qu);
		try {
			while (rs.next()) {
				String titlex = rs.getString("title");
				System.out.println(titlex);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@FXML
	public void addBooks(ActionEvent event) {
		String bookID = id.getText();
		String bookAuthor = author.getText();
		String bookTitle = title.getText();
		String bookPublisher = publisher.getText();
		
		if (bookID.isEmpty()||
				bookAuthor.isEmpty()||bookPublisher.isEmpty()||bookTitle.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText(null);
			alert.setContentText("Please Enter in all fields");
			alert.showAndWait();
			return;
		} else {
			
			String qu = "INSERT INTO BOOK VALUES ("
					+ "'" + bookID + "',"
					+ "'" + bookTitle + "',"
					+ "'" + bookAuthor + "',"
					+ "'" + bookPublisher + "',"
					+ "'" + true + "'"
							+ ")";
			
			System.out.println(qu);
			if(databaseHandler.execAction(qu)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setHeaderText(null);
				alert.setContentText("Sucess");
				alert.showAndWait();
				return;
				
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText(null);
				alert.setContentText("Failed");
				alert.showAndWait();
				return;
			}
			
		}
		
	}
	
	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();
		
	}
	

}
