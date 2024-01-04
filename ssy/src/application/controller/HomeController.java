package application.controller;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.Manager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {

    @FXML
    private Button logout, btnAddUser;
    @FXML
    private ImageView btnLayout1, btnLayout2;
    @FXML
    private VBox panel;
    @FXML
    private Label loginManager, totalUser, activateUser, inactivateUser, allUser, actUser, inactUser;
    @FXML
    private ProgressBar allUserBar, actUserBar, inactUserBar;
    @FXML
    private AnchorPane locker;
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
        String countUser = "전체 회원 "+dao.countUser()+"명";
        totalUser.setText(countUser);
        // ProgressBar 전체 회원 수
        double maxValue = 12;
        double progress = dao.countUser() / maxValue;
        allUserBar.setProgress(progress);
		String count = "만료를 포함한 등록된 회원은 총 " +dao.countUser()+ "명 입니다";
		allUser.setText(count);
        
        // 현재 활성화된 회원, 비활성화 된 회원
        String activateU = "활성 회원 "+dao.statusUserNum(true)+"명";
        activateUser.setText(activateU);
        // ProgressBar 활성 회원 수
        maxValue = dao.countUser();
        progress = dao.statusUserNum(true) / maxValue;
        actUserBar.setProgress(progress);
		count = "전체 회원 중 활성 회원은 " +dao.statusUserNum(true)+ "명 입니다";
		actUser.setText(count);

        String inactivateU = "비활성 회원 "+dao.statusUserNum(false)+"명";
        inactivateUser.setText(inactivateU);
        // ProgressBar 비활성 회원 수
        progress = dao.statusUserNum(false) / maxValue;
        inactUserBar.setProgress(progress);
		count = "전체 회원 중 비활성화 회원은 " +dao.statusUserNum(false)+ "명 입니다";
		inactUser.setText(count);

        // f5를 눌러서 Stage 리다이렉트
        Platform.runLater(()-> {
            Stage curStage = (Stage)panel.getScene().getWindow();

            curStage.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F5) {
                    curStage.close();
                    try {
                        // 새로운 Stage 열기
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                        Parent root = loader.load();
                        Stage newStage = new Stage();
                        newStage.setScene(new Scene(root));
                        newStage.setTitle("홈 페이지");
                        newStage.setResizable(false);
                        newStage.show();        
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        });
        
        custManage = (AnchorPane)panel.getChildren().get(2);
        attendance = (AnchorPane)panel.getChildren().get(3);
        salManage = (AnchorPane)panel.getChildren().get(6);
        salStatistic = (AnchorPane)panel.getChildren().get(7);

        // 로그아웃 버튼 클릭
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
        }); // 로그아웃 버튼 클릭
        
//        locker.setOnMouseMoved(new EventHandler<Event>() {
//            @Override
//            public void handle(MouseDragEvent event) {
//            	locker.setStyle("-fx-background-color: red;");
//            }
//        });

        // 회원 세모박스 클릭
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
        }); // 회원 세모박스 클릭

        // 회계 세모박스 클릭
        btnLayout2.setOnMouseClicked(e -> {
            if (salFlag) {
                // salFlag가 true일 때
                panel.getChildren().removeAll(salManage,salStatistic);
            } else {
                // salFlag가 false일 때
                panel.getChildren().addAll(salManage, salStatistic);
            }
            salFlag = !salFlag;
        }); // 회계 세모박스 클릭

        // 회원 등록 버튼 클릭
        btnAddUser.setOnAction(e -> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent createUserPage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/CreateUser.fxml"));
                createUserPage = loader.load();

                stage.setScene(new Scene(createUserPage));
                stage.setTitle("회원 등록 페이지");
                stage.setResizable(false);
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 회원 등록 버튼 클릭
        
	}

}

