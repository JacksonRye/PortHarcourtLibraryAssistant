package library.assistant.ui.listusers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import library.assistant.database.DatabaseHandler;
import javafx.scene.control.TableColumn;

public class UserListController implements Initializable {
	
	ObservableList<Member> list = FXCollections.observableArrayList();
	
	@FXML
	private TableView<Member> tableView;
	@FXML
	private TableColumn<Member, String> idCol;
	@FXML
	private TableColumn<Member, String> nameCol;
	@FXML
	private TableColumn<Member, String> mobileCol;
	@FXML
	private TableColumn<Member, String> emailCol;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initCol();
		loadData();
		
	}

	
	private void loadData() {
		DatabaseHandler handler = DatabaseHandler.getInstance();
		
		String qu = "SELECT * FROM MEMBER";
		ResultSet rSet = handler.execQuery(qu);
		
		try {
			while (rSet.next()) {
				String id = rSet.getString("id");
				String name = rSet.getString("name");
				String mobile = rSet.getString("mobile");
				String email = rSet.getString("email");

				list.add(new Member(id, name, mobile, email));
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		tableView.setItems(list);
		
	}


	private void initCol() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
	}
	
	public static class Member {
		private final SimpleStringProperty id;
		private final SimpleStringProperty name;
		private final SimpleStringProperty mobile;
		private final SimpleStringProperty email;
		
		
		
		public Member(String id, String name, String mobile,
				String email) {
			super();
			this.id = new SimpleStringProperty(id);
			this.name = new SimpleStringProperty(name);
			this.mobile = new SimpleStringProperty(mobile);
			this.email = new SimpleStringProperty(email);
		}
		public String getId() {
			return id.get();
		}
		public String getName() {
			return name.get();
		}
		public String getMobile() {
			return mobile.get();
		}
		public String getEmail() {
			return email.get();
		}
		
		
		
		
	}

}
