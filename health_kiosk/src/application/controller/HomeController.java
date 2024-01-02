package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {

    @FXML
    private Button logout;
    @FXML
    private ImageView btnLayout1;
    @FXML
    private AnchorPane 고객관리;
    @FXML
    private VBox panel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        logout.setOnAction(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent loginPage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/LoginPage.fxml"));
                loginPage = loader.load();

                stage.setScene(new Scene(loginPage));
                stage.setTitle("홈 페이지");
                Stage homePage = (Stage)logout.getScene().getWindow();
                stage.setResizable(false);
                stage.show();
                homePage.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        });

        // 세모창 누르면 창 사라졌다가 생기게 하기
        btnLayout1.setOnMouseClicked(e -> {
        });
        




	}

}

