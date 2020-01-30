package library.assistant.ui.listmembers;

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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addmember.MemberAddController;
import library.assistant.util.LibraryAssistantUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberListController implements Initializable {

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
        list.clear();
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

	@FXML
	private void handleMemberDelete(ActionEvent event) {
		Member selectedMember = tableView.getSelectionModel().getSelectedItem();
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirm Deletion");
		alert.setContentText("Are you sure you want to delete " + selectedMember.getName() + " ?");
		Optional<ButtonType> answer = alert.showAndWait();

		if (answer.get() == ButtonType.OK) {
			if (DatabaseHandler.getInstance().deleteMember(selectedMember)) {
				list.remove(selectedMember);
				return;
			} else {
				AlertMaker.showErrorMessage("Failed", "Member Deletion Failed");
			}
		}

		AlertMaker.showSimpleAlert("Cancelled", "Deletion Canceled");
		return;

	}

	@FXML
	private void handleMemberEdit(ActionEvent event) {

		Member selectedMember = tableView.getSelectionModel().getSelectedItem();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"));
		Parent parent = null;
		try {
			parent = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		MemberAddController controller = loader.getController();
		controller.inflateUI(selectedMember);


		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setTitle("Edit Member");
		stage.setScene(new Scene(parent));
		LibraryAssistantUtil.setStageIcon(stage);
		stage.show();

		stage.setOnHiding((e) -> {
			handleRefresh(new ActionEvent());
		});


	}

	@FXML
	private void handleRefresh(ActionEvent event) {
		list.clear();
		loadData();
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
