package library.assistant.ui.listbook;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import library.assistant.ui.main.MainController;
import library.assistant.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookListController implements Initializable {
	
	ObservableList<Book> list = FXCollections.observableArrayList();
	
	@FXML
	private AnchorPane rootPane;
	@FXML
	private TableView<Book> tableView;
	@FXML
	private TableColumn<Book, String> titleCol;
	@FXML
	private TableColumn<Book, String> idCol;
	@FXML
	private TableColumn<Book, String> authorCol;
	@FXML
	private TableColumn<Book, String> publisherCol;
	@FXML
	private TableColumn<Book, Boolean> availabilityCol;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initCol();
		
		loadData();
	}
	
	private void loadData() {
		list.clear();
		DatabaseHandler handler = DatabaseHandler.getInstance();

		String qu = "SELECT * FROM BOOK";
		ResultSet rs = handler.execQuery(qu);
		try {
			while (rs.next()) {
				String titlex = rs.getString("title");
				String author = rs.getString("author");
				String id = rs.getString("id");
				String publisher = rs.getString("publisher");
				Boolean avail = rs.getBoolean("isAvail");
				System.out.println(titlex);

				list.add(new Book(titlex, id, author, publisher, avail));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Print Error before Set Items");
			Logger.getLogger(BookAddController.class.getName())
					.log(Level.SEVERE, null, e);
		}

		tableView.setItems(list);
	}

	private void initCol() {
//		Values of PropertyValueFactory must reflect member variables of Book class
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));

	}

	@FXML
	private void handleBookDeleteOption(ActionEvent event) {
		Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
		if (selectedForDeletion == null) {
			AlertMaker.showErrorMessage("No Book Selected", "Select a book for selection");
			return;
		}
		if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion)) {
			AlertMaker.showErrorMessage("Can't be deleted", "This book is already issued and can't be deleted");
			return;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Deleting Book");
		alert.setContentText("Are you sure you want to delete the book "
				+ selectedForDeletion.getTitle() + " ?");
		Optional<ButtonType> answer = alert.showAndWait();
		if (answer.get() == ButtonType.OK) {
			boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
			if (result) {
				AlertMaker.showSimpleAlert("Book Delete", selectedForDeletion.getTitle() + " was deleted successfully");
				list.remove(selectedForDeletion);
			} else {
				AlertMaker.showErrorMessage("Failed", "Book Deletion Failed");
			}
		} else {
			AlertMaker.showSimpleAlert("Deletion Cancelled", "Deletion Process Cancelled");
		}
	}

	@FXML
	private void handleBookEditOption(ActionEvent event) {
		Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
		if (selectedForEdit == null) {
			AlertMaker.showErrorMessage("No book selected", "Please select a book to edit.");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"));
			Parent parent = loader.load();

			BookAddController controller = loader.getController();
			controller.inflateUI(selectedForEdit);

			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit Book");
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);

			stage.setOnHiding((e) -> {
				handleRefresh(new ActionEvent());
			});
		} catch (IOException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	private void handleRefresh(ActionEvent event) {
		list.clear();
		loadData();
	}

	public static class Book {
		private final SimpleStringProperty title;
		private final SimpleStringProperty id;
		private final SimpleStringProperty publisher;
		private final SimpleStringProperty author;
		private final SimpleBooleanProperty availability;

		public Book(String title, String id, String author,
					String publisher, Boolean avail) {
			this.title = new SimpleStringProperty(title);
			this.id = new SimpleStringProperty(id);
			this.publisher = new SimpleStringProperty(publisher);
			this.author = new SimpleStringProperty(author);
			this.availability = new SimpleBooleanProperty(avail);
		}

		public String getTitle() {
			return title.get();
		}


		public String getId() {
			return id.get();
		}


		public String getPublisher() {
			return publisher.get();
		}


		public String getAuthor() {
			return author.get();
		}


		public Boolean getAvailability() {
			return availability.get();
		}

	}

}
