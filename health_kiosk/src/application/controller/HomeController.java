package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {

    @FXML
    private Button logout;
    @FXML
    private ImageView btnLayout1, btnLayout2;
    @FXML
    private VBox panel;
    @FXML
    private Label loginManager, totalUser;

    // DB에서 받아온 데이터를 저장하는 객체(VO)
    UserDAO dao = new UserDAOImpl();

    private AnchorPane custManage, attendance, salManage, salStatistic;
    private boolean custFlag = true, salFlag = true;
    Manager m = LoginController.loginManager; // 현재 로그인한 매니저
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {
        
        // 현재 로그인 한 매니저의 이름과 id를 가져와서 저장
        String loginM = m.getManagerId() + " : " + m.getManagerName();
        loginManager.setText(loginM);

        // 현재 회원 수 출력 해주기
        // 밑에 표시 창을 변경해야 하는데 (미션)
        String countUser = "전체 회원 "+dao.countUser()+"명";
        totalUser.setText(countUser);

        // 현재 활성화된 회원, 비활성화 된 회원
        

        custManage = (AnchorPane)panel.getChildren().get(2);
        attendance = (AnchorPane)panel.getChildren().get(3);
        salManage = (AnchorPane)panel.getChildren().get(6);
        salStatistic = (AnchorPane)panel.getChildren().get(7);

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

        btnLayout1.setOnMouseClicked(e -> {
            if (custFlag) {
                // custFlag가 true일 때
                panel.getChildren().removeAll(custManage, attendance);
            } else {
                // custFlag가 false일 때
                panel.getChildren().add(2, custManage);
                panel.getChildren().add(3, attendance);
            }
            custFlag = !custFlag;
        });

        btnLayout2.setOnMouseClicked(e -> {
            if (salFlag) {
                // salFlag가 true일 때
                panel.getChildren().removeAll(salManage,salStatistic);
            } else {
                // salFlag가 false일 때
                panel.getChildren().addAll(salManage, salStatistic);
            }
            salFlag = !salFlag;
        });
        
        




	}

}

