package application.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.dao.ManagerDAO;
import application.dao.ManagerDAOImpl;
import application.dto.Manager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

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

    private ManagerDAO dao = new ManagerDAOImpl();
    private Manager member;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // 가입 버튼 클릭
        reg.setOnAction(e-> {
            boolean chkFlag = false;
            
            // 성별 선택
            RadioButton value = (RadioButton)gender.selectedToggleProperty().getValue();
            
            // 회원가입 규칙
            char[] char_id= id.getText().trim().toCharArray();
            char[] char_passwd = passwd.getText().trim().toCharArray();
            String string_name = name.getText().trim();
            String string_email = email.getText().trim();
            String string_phoneNum = phoneNum.getText().trim(); 

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
            } // 아이디 중복, 비밀번호 불일치 체크 완료

            // 한글이 아닌경우
            if (string_name != null &&!Pattern.matches("[가-힣]*", string_name)) {
                warnPage(name, "이름 잘못입력", "이름은 한글로만 작성해주세요.");
                return;
            } else if (string_name.length() > 4 || string_name.equals("")) {
                warnPage(name, "이름 조건 알림", "이름은 공백이거나 4자리를 초과할 수 없습니다.");
                return;
            } // 이름 조건 체크 완료
            
            if (string_email.length() > 20) {
            	warnPage(email, "이메일 조건 알림", "이메일은 20자리를 초과할 수 없습니다. ");
            	return;
            }
            
            if (string_phoneNum.length() > 13) {
            	warnPage(phoneNum, "휴대전화번호 조건 알림", "휴대전화번호는 13자리를 초과활 수 없습니다.");
            	return;
            }
            
            // email, phoneNum, birthDate가 공백이어도 회원가입 가능하므로 이름조건 까지 체크 완료하면, 회원가입 가능
            
            // ------------------------------------------------
            // 근데 여기서 또 추가로 email은 20자 이내, 휴대전화는 13자 이내로 만들어야 함
            // 또 처음 시작할 때 id 버튼으로 포커스 이동하고 Enter 클릭하면 다른 포커스로 이동하는 것 구현
            // ------------------------------------------------

            String str_id = id.getText().trim();
            String str_pwd = passwd.getText().trim();
            String str_name = string_name;
            LocalDate date_birthDate = birth.getValue();
            String str_gender = value.getText(); // 성별
            String str_email = email.getText();
            String str_phone = phoneNum.getText();

            
            member = new Manager(str_id, str_pwd, str_name, str_gender, date_birthDate, str_email, str_phone);
            // UserVO 에 저장

            dao.join(member); // DB에 유저 정보 넣기

            Alert info = new Alert(AlertType.INFORMATION);
            info.setHeaderText("회원가입 완료");
            info.showAndWait();
            
            Stage stage = (Stage)reg.getScene().getWindow();
            stage.close(); // DB에 유저 넣고 나면, 창 종료
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
        if (textField.getText().trim().equals("")) {
            warnPage(textField,text[2], text[3]);
            return true;
        } else if (textField.getText().trim().length()>12) {
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
