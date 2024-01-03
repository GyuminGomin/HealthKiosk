package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.dao.ManagerDAO;
import application.dao.ManagerDAOImpl;
import application.dto.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{

    // fxml 연동
    @FXML
    private TextField id, passwd;
    @FXML
    private Button logOn, logIn, searchIdandPasswd;


    // DB에서 받아온 데이터를 저장하는 객체(VO)
    ManagerDAO dao = new ManagerDAOImpl();
    // 로그인한 사람 정보 저장
    static Manager loginManager;

    @Override
    public void initialize(URL location, ResourceBundle bundle) {
        
        // 회원가입 버튼 클릭
        logOn.setOnAction(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent logonPage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/LogonPage.fxml"));
                logonPage = loader.load();

                stage.setScene(new Scene(logonPage));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("회원가입 페이지");
                Stage loginPage = (Stage)logIn.getScene().getWindow();
                double x = loginPage.getX();
                stage.setX(x+loginPage.getWidth());
                stage.setY(loginPage.getY());
                stage.setResizable(false);
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 회원가입 버튼 클릭

        // 로그인 버튼 클릭
        logIn.setOnAction(e -> {
            // 로그인 아이디랑 비밀번호 맞는지 확인하고 페이지 넘기기
            Manager m = null;
            String str_id = id.getText().trim();
            String str_passwd = passwd.getText().trim();
            m = dao.selectMember(str_id, str_passwd);
            if (m == null) {
                warnPage("아이디, 패스워드 오류", "존재하지 않는 아이디이거나 패스워드 입니다. 다시 입력해주세요.");
                return;
            }
            loginManager = m;
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("로그인 완료");
            alert.setHeaderText("로그인에 성공했습니다.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();

            Stage stage = null;
            FXMLLoader loader = null;
            Parent homePage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                homePage = loader.load();

                stage.setScene(new Scene(homePage));
                stage.setTitle("홈 페이지");
                Stage loginPage = (Stage)logIn.getScene().getWindow();
                stage.setResizable(false);
                stage.show();
                loginPage.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }

            // 만약 아이디랑 비밀번호가 틀리다면, 다시 입력 경고창 띄우기
                // 아이디가 존재하지 않는다면, 존재하지 않는 아이디
                // 비밀번호가 틀리다면, 비밀번호가 틀립니다
                // 위 처럼 구현하면 보안에 별로 안좋기 때문에
                // 아이디 비밀번호 둘 중 하나라도 맞지 않으면, 아이디 또는 비밀번호가 틀립니다.
        });

        // 아이디/비밀번호 찾기 버튼 클릭
        searchIdandPasswd.setOnAction(e -> {
            // 웹 페이지로 구성된 것이 아니기에 DB 서버가 자신의 컴퓨터에서 가동되고(찾을 수 있음) 다른 이메일과 휴대폰번호와 연동되게 구현하지 않아서 구현하기가 쉽지가 않습니다.
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("접근 불가");
            alert.setHeaderText("아직 구현되지 않은 페이지 입니다. 관리자에게 요청하세요.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        });

    } // end initialize

    // warn 페이지
    public void warnPage(String title, String header) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.show();
    }

}
