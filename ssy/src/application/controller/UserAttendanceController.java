package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.dto.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserAttendanceController implements Initializable{

    @FXML
    private Label labelHome;
    @FXML
    private TableView<User> tableView; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // HealthKiosk 버튼 클릭
        labelHome.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent homePage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                homePage = loader.load();

                stage.setScene(new Scene(homePage));
                stage.setTitle("홈 페이지");
                stage.setResizable(false);
                stage.show();
                Stage oldStage = (Stage)labelHome.getScene().getWindow();
                oldStage.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        });
    }
    
}
