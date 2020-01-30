package library.assistant.ui.addbook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbook.BookListController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
	private Boolean isInEditMode = Boolean.FALSE;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		databaseHandler = DatabaseHandler.getInstance();

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

	public void inflateUI(BookListController.Book book) {
		title.setText(book.getTitle());
		id.setText(book.getId());
		author.setText(book.getAuthor());
		publisher.setText(book.getPublisher());
		id.setEditable(false);
		isInEditMode = Boolean.TRUE;
	}

	@FXML
	public void addBooks(ActionEvent event) {
		String bookID = id.getText();
		String bookAuthor = author.getText();
		String bookTitle = title.getText();
		String bookPublisher = publisher.getText();

		if (bookID.isEmpty() ||
				bookAuthor.isEmpty() || bookPublisher.isEmpty() || bookTitle.isEmpty()) {
			AlertMaker.showErrorMessage("Error", "Please enter all contents");
			return;
		}


		if (isInEditMode) {
			handleEditOperation();
			return;
		}

		String qu = "INSERT INTO BOOK VALUES ("
				+ "'" + bookID + "',"
				+ "'" + bookTitle + "',"
				+ "'" + bookAuthor + "',"
				+ "'" + bookPublisher + "',"
				+ "'" + true + "'"
				+ ")";

		System.out.println(qu);
		if (databaseHandler.execAction(qu)) {
			AlertMaker.showSimpleAlert("Success", "Book Added Successfully");

		} else {
			AlertMaker.showErrorMessage("Error", "Book Addition Failed");
		}
		return;


	}

	private void handleEditOperation() {

		BookListController.Book book = new BookListController.Book(title.getText(),
				id.getText(), author.getText(), publisher.getText(), true);

		if (databaseHandler.updateBook(book)) {
			AlertMaker.showSimpleAlert("Success", "Book Updated");
		} else {
			AlertMaker.showErrorMessage("Failed", "Can't update book");
		}


	}

	@FXML
	private void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPane.getScene().getWindow();
		stage.close();

	}
	

}
