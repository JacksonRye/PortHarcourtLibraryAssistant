package library.assistant.ui.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/login.fxml"));

        Scene scene = new Scene(root);

        LibraryAssistantUtil.setStageIcon(primaryStage);

        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            DatabaseHandler.getInstance();
        }).start();

    }

	public static void main(String[] args) {
		launch(args);

	}

}
