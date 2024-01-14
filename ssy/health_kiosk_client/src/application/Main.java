package application;

import application.controller.AttendanceTimeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/AttendanceTimePage.fxml"));
            Parent root = loader.load(); 
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("출석시간 페이지");
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.setOnCloseRequest(e-> {
                AttendanceTimeController.isRun = false;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
