package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RootController implements Initializable{

    // fxml 연동
    @FXML
    private TextArea textArea;
    @FXML
    private TextField id, passwd;
    @FXML
    private Button logOn, logIn;

    // DB와 연결하기 위한 객체 생성
    Properties prop = null;
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;


    // DB에서 받아온 데이터를 저장하는 객체(VO)
    private User member = null;

    @Override
    public void initialize(URL location, ResourceBundle bundle) {
        try {
            this.prop = new Properties();
            this.prop.load(new FileReader("src/application/prop/mysql.properties"));

            conn = DriverManager.getConnection(prop.getProperty("url"), prop);

            // 간단한 테스트 (UI 제작해서 한번 창에 띄어보자)
            stmt = conn.createStatement();
            String sql = "SELECT * FROM user";
            rs = stmt.executeQuery(sql);

            Runnable run = new Runnable(()-> {
                while (rs.next()) {
                    this.member = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
                
                }
            });
            

        } catch (FileNotFoundException e) {
            System.out.println("properties 파일을 찾을 수 없습니다.");
        } catch (IOException e) {
            System.out.println("properties 파일을 읽을 수 없습니다.");
        } catch (SQLException e) {
            System.out.println("DB 연결 정보 오류");
        }
    } // end initialize

    public void printText(String text) {
        Platform.runLater(() -> {
            textArea.appendText(text+"\n");
        });
    } 

}
