package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
// UserManagement Page
			// FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/UserManagementPage.fxml"));		
// Login Page
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/LoginPage.fxml"));
			Parent root = loader.load();
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("페이지");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
