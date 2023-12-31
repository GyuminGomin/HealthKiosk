package application.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import application.dto.User;
import application.utils.DBUtil;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{

    // fxml 연동
    @FXML
    private TextField id, passwd;
    @FXML
    private Button logOn, logIn, searchIdandPasswd;

    // DB와 연결하기 위한 객체 생성
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;


    // DB에서 받아온 데이터를 저장하는 객체(VO)
    private User member = null;

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
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 회원가입 버튼 클릭

        
    } // end initialize

}
