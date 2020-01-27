package library.assistant.ui.listbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.BookAddController;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;

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
		
		tableView.setItems(list);;
	}

	private void initCol() {
//		Values of PropertyValueFactory must reflect member variables of Book class
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));		
		
	}

	public static class Book{
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
