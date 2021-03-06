package library.assistant.ui.main;

import com.jfoenix.effects.JFXDepthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainController implements Initializable {
	
	@FXML
    private TextField txtFieldBookID;

    @FXML
    private Text txtBookName;

    @FXML
    private Text txtBookAuthor;

    @FXML
    private Text txtBookStatus;

    @FXML
    private HBox memberInfo;	
	
	@FXML
    private HBox bookInfo;
	
	@FXML
	private Text txtMemberName;
	
	@FXML
	private Text txtMemberContact;
	
	@FXML
	private TextField txtFieldMemberID;

	@FXML
	private TextField txtRenewBookID;


    @FXML
    private ResourceBundle resources;

	@FXML
	private ListView<String> issueDataList;


	@FXML
	private URL location;

	Boolean isReadyForSubmission = false;

	DatabaseHandler databaseHandler;
	@FXML
	private StackPane rootPane;

	@FXML
	private void loadAddBook(ActionEvent event) {
		loadWindow("/library/assistant/ui/addbook/add_book.fxml",
				"Add Books");

	}

	@FXML
	void loadAddMember(ActionEvent event) {
		loadWindow("/library/assistant/ui/addmember/member_add.fxml",
				"Add New Member");

    }

    @FXML
    void loadBookTable(ActionEvent event) {
    	loadWindow("/library/assistant/ui/listbook/book_list.fxml",
    			"Show Books");

    }

    @FXML
    void loadMemberTable(ActionEvent event) {
        loadWindow("/library/assistant/ui/listmembers/member_list.fxml",
                "Show Members");

    }

    
    void loadWindow(String loc, String title) {
    	try {
            Parent parent = FXMLLoader.load(getClass().getResource(loc));

            Stage stage = new Stage(StageStyle.DECORATED);
            LibraryAssistantUtil.setStageIcon(stage);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException e) {
			e.printStackTrace();
		}

    }
    
    @FXML
    private void loadBookInfo() {
    	clearBookCache();
		String id = txtFieldBookID.getText();
		String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
		ResultSet rs = databaseHandler.execQuery(qu);
		
		Boolean flag = true;
		
		try {
			while (rs.next()) {
				String bName = rs.getString("title");
				String bAuthor = rs.getString("author");
				Boolean bStatus = rs.getBoolean("isAvail");
				
				txtBookName.setText(bName);
				txtBookAuthor.setText(bAuthor);
				String status = (bStatus)?"Available" : "Not Available";
				txtBookStatus.setText(status);
				
				flag = false;
			}
			if (flag) {
				txtBookName.setText("No Such Book Available");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
    @FXML
    private void loadMemberInfo() {
    	clearMemberCache();
    	String id = txtFieldMemberID.getText();
		String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
		ResultSet rs = databaseHandler.execQuery(qu);
		
		Boolean flag = true;
		
		try {
			while (rs.next()) {
				String mName = rs.getString("name");
				String mMobile = rs.getString("mobile");
				
				txtMemberName.setText(mName);
				txtMemberContact.setText(mMobile);
				
				flag = false;
			}
			if (flag) {
				txtMemberName.setText("No Such Member Available");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    @FXML
	private void loadRenewOperation(ActionEvent event){

    	if (!isReadyForSubmission) {
			AlertMaker.showErrorMessage("Failed", "Please Enter a book to renew");
			return;
		}

    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    	alert.setTitle("Confirm Renew Operation");
    	alert.setHeaderText(null);
    	alert.setContentText("Are you sure you want to renew the book?");

    	Optional<ButtonType> response = alert.showAndWait();
    	if (response.get() == ButtonType.OK) {
    		String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP" +
					", renew_count = renew_count+1" +
					" WHERE bookID = '" + txtRenewBookID.getText()  +
					"'";
    		System.out.println(ac);
    		if (databaseHandler.execAction(ac)) {
				AlertMaker.showSimpleAlert("Success", "Book has been renewed");
			} else {
				AlertMaker.showErrorMessage("Error", "Book renewal failed");
			}
		} else {
			AlertMaker.showSimpleAlert("Cancelled", "Book renewal cancelled");
		}
	}
    
    private void clearBookCache() {
    	txtBookAuthor.setText("");
    	txtBookName.setText("");
    	txtBookStatus.setText("");
    	
	}
    
    private void clearMemberCache() {
    	txtMemberContact.setText("");
    	txtMemberName.setText("");
    	
	}

	@FXML
	private void loadIssueOperation(ActionEvent event){

    	loadMemberInfo();
    	loadBookInfo();

		String memberID = txtFieldMemberID.getText();
		String bookID = txtFieldBookID.getText();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Issue Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to issue the book "
		+ txtBookName.getText() + " to " + txtMemberName.getText());

		Optional<ButtonType> response = alert.showAndWait();
		if (response.get() == ButtonType.OK){
			String str = "INSERT INTO ISSUE (memberID, bookID) VALUES ( " +
					"'" + memberID + "'," +
					"'" + bookID + "'" +
					")";

			String str2 = "UPDATE BOOK SET isAvail = false WHERE id = " +
					"'" + bookID + "'";

			if (databaseHandler.execAction(str)&&databaseHandler.execAction(str2)) {
				AlertMaker.showSimpleAlert("Success", "Book Issue successful");

			} else {
				AlertMaker.showErrorMessage("Error", "Book Issue Failed");
			}

		} else {
			AlertMaker.showSimpleAlert("Cancelled", "Book Issue cancelled");
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JFXDepthManager.setDepth(bookInfo, 1);
		JFXDepthManager.setDepth(memberInfo, 1);
		databaseHandler = DatabaseHandler.getInstance();

	}

	@FXML
	public void loadBookInfo2(ActionEvent event) {

		ObservableList<String> issueData = FXCollections.observableArrayList();

		isReadyForSubmission = false;

		String id = txtRenewBookID.getText();
		String qu = "SELECT * FROM ISSUE WHERE BOOKID = '" + id + "'";
		ResultSet rsIssue = databaseHandler.execQuery(qu);

		try {
			while (rsIssue.next()) {
				String mBookID = id;
				String mMemberID = rsIssue.getString("memberID");
				Timestamp mIssueTime = rsIssue.getTimestamp("issueTime");
				int mRenewCount = rsIssue.getInt("renew_count");

				issueData.add("Issue Data and Time : " + mIssueTime.toString());
				issueData.add("Renew Count : " + mRenewCount);

				issueData.add("Book Information:- ");

				qu = "SELECT * FROM BOOK WHERE ID = '" + mBookID + "'";
				ResultSet rsBook = databaseHandler.execQuery(qu);
				while (rsBook.next()){
					issueData.add("		Book Name : " + rsBook.getString("title"));
					issueData.add("		Book ID : " + rsBook.getString("id"));
					issueData.add("		Book Author : " + rsBook.getString("author"));
					issueData.add("		Book Publisher : " + rsBook.getString("publisher"));

				}

				qu = "SELECT * FROM MEMBER WHERE ID = '" + mMemberID + "'";
				ResultSet rsMember = databaseHandler.execQuery(qu);
				issueData.add("Member Info:-");
				while (rsMember.next()){
					issueData.add("		Name : " + rsMember.getString("name"));
					issueData.add("		Mobile : " + rsMember.getString("mobile"));
					issueData.add("		Email : " + rsMember.getString("email"));
				}

				isReadyForSubmission = true;

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		issueDataList.getItems().setAll(issueData);
	}

	@FXML
	private void loadSubmissionOperation(ActionEvent event) {
		if (!isReadyForSubmission) {
			AlertMaker.showErrorMessage("Failed", "Please enter a book to submit");
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Submission");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to submit the book?");

		Optional<ButtonType> response = alert.showAndWait();

		String id = txtRenewBookID.getText();
		String ac1 = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
		String ac2 = "UPDATE BOOK SET isAvail = TRUE WHERE ID = '" + id + "'";

		if (response.get() == ButtonType.OK) {

			if (databaseHandler.execAction(ac1) && databaseHandler.execAction(ac2)) {
				AlertMaker.showSimpleAlert("Success", "Book has been submitted");
			} else {
				AlertMaker.showErrorMessage("Failed", "Book Submission Failed");
			}

			issueDataList.setItems(null);
		} else {
			AlertMaker.showSimpleAlert("Cancelled", "Submission Operation Cancelled");

		}
	}

	@FXML
	private void loadSettings(ActionEvent event) {
		loadWindow("/library/assistant/settings/settings.fxml", "Settings");
	}

	@FXML
	private void handleMenuClose(ActionEvent event) {
		((Stage) rootPane.getScene().getWindow()).close();
	}

	@FXML
	private void handleMenuFullScreen(ActionEvent event) {
		Stage stage = ((Stage) rootPane.getScene().getWindow());
		stage.setFullScreen(!stage.isFullScreen());
	}
}
