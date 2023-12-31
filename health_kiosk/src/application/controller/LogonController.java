package application.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import application.utils.DBUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

public class LogonController implements Initializable{

    @FXML
    private TextField id, passwd, chkPasswd, name, email, phoneNum;
    @FXML
    private DatePicker birth; // 생년월일
    @FXML
    private ToggleGroup gender; // 성별
    @FXML
    private Button reg; // 가입하기 버튼

    // DB와 연결하기 위한 객체 생성
    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    private UserDAO dao = new UserDAOImpl();
    private User member;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // 가입 버튼 클릭
        reg.setOnAction(e-> {
            boolean chkFlag = false;
            

            // 성별 선택
            RadioButton value = (RadioButton)gender.selectedToggleProperty().getValue();
            
            // 회원가입 규칙
            char[] char_id= id.getText().toCharArray();
            char[] char_passwd = passwd.getText().toCharArray();

            chkFlag = chk(char_id, id, "아이디 조건 알림", "아이디는 영문(소문자) 또는 숫자로만 이루어질 수 있습니다.", "아이디 입력 필수", "아이디는 공백일 수 없습니다.", "아이디 다시 입력", "아이디는 12자 이내로 작성해주세요."); // id 체크 완료
            
            if (chkFlag) return; // id 문제 발생하면 종료

            chkFlag = chk(char_passwd, passwd, "비밀번호 조건 알림", "비밀번호는 영문(소문자) 또는 숫자로만 이루어질 수 있습니다.", "비밀번호 입력 필수", "비밀번호는 공백일 수 없습니다.", "비밀번호 다시 입력", "비밀번호는 12자 이내로 작성해주세요."); // 비밀번호 체크 완료

            if (chkFlag) return; // 비밀번호 문제 발생하면 종료

            if (!dao.selectMember(id.getText())) {
                warnPage(id, "아이디 중복", "아이디가 이미 존재합니다. 다른 아이디를 입력해주세요.");
                return;
            } else if (!passwd.getText().equals(chkPasswd.getText())) {
                warnPage(chkPasswd, "비밀번호 불일치", "비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
                return;
            }

            


            
            

            // 회원가입 규칙

            member = new User(id.getText(), passwd.getText(),name.getText(), value.getText()); // id, passwd, name, gender가 저장

            // email, phoneNum, birthDate가 공백이어도 회원가입 가능
            // 만약, 저장되어 있다면, set으로 설정


        }); // 가입버튼 클릭
    } // end initialize
    
    
    // ID 체크, Passwd 체크
    public boolean chk(char[] charArray, TextField textField, String... text) {
        for (char c : charArray) {
                if (!((c>=48 && c<=57) || (c>=97 && c<=122))) {
                    warnPage(textField,text[0], text[1]);
                    return true;
                }
            }
        if (textField.getText().equals("")) {
            warnPage(textField,text[2], text[3]);
            return true;
        } else if (textField.getText().length()>12) {
            warnPage(textField,text[4], text[5]);
            return true;
        } // ID 체크, Passwd 체크
        return false; // ID 문제 없음
    }

    // warn 페이지
    public void warnPage(TextField textField,String title, String header) {
        Alert warn = new Alert(AlertType.WARNING);
        warn.setTitle(title);
        warn.setHeaderText(header);
        warn.show();
        textField.clear();
        textField.requestFocus();
    }


}
