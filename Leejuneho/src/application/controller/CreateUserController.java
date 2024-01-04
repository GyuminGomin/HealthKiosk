package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

public class CreateUserController implements Initializable{

    @FXML
    private TextField txtName, firstNum, middleNum, lastNum;
    @FXML
    private ToggleGroup gender;
    @FXML
    private Button btnNext;
    @FXML
    private DatePicker startDate;

    // DB에서 받아온 데이터를 저장하는 객체(VO)
    private UserDAO dao = new UserDAOImpl();
    // User VO
    private User user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        LogonController.focusMove(txtName, firstNum, middleNum, lastNum);
        
        Platform.runLater(()->{
        	txtName.requestFocus();
        });

        txtName.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) firstNum.requestFocus();
        });
        firstNum.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) middleNum.requestFocus();
        });
        middleNum.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) lastNum.requestFocus();
        });
        lastNum.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) startDate.requestFocus();
        });
        startDate.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) btnNext.fire();
        });

        // 회원 추가 버튼 클릭
        btnNext.setOnAction(e -> {

            // 이름 설정 // 한글과 영어, 숫자로 입력가능하게 설정
            if (txtName.getText().trim().length() < 2 || txtName.getText().trim().length()>20) {
                LogonController.warnPage("이름 입력 잘못", "이름은 2글자 이상 20글자 이하로 제한됩니다.", txtName);
                return;
            }
            if(txtName.getText().trim() != null && !Pattern.matches("^[가-힣a-zA-Z0-9]*$",txtName.getText().trim())) {
            	LogonController.warnPage("특수문자 입력 불가","이름을 영어, 한글, 숫자로만 작성해주세요.",txtName);
            	return;
            };

            // 휴대폰 설정 // 숫자만 입력가능하게 설정
            if (firstNum.getText().trim().length() != 3) {
                LogonController.warnPage("휴대폰 앞자리 3자리", "대한민국 휴대폰 앞자리는 010, 011, 016, 017, 019 등이 존재합니다.", firstNum);
                return;
            }else if(firstNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", firstNum.getText().trim())) {
            	LogonController.warnPage("번호 재입력", "전화번호는 숫자로만 작성해주세요.", firstNum, middleNum, lastNum);
            	return;
            }
            if (middleNum.getText().trim().length() != 4) {
                LogonController.warnPage("휴대폰 지역별 할당번호는 4자리", "휴대폰 지역별 할당번호는 4자리입니다.", middleNum);
                return;
            }else if(middleNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", middleNum.getText().trim())) {
            	LogonController.warnPage("번호 재입력", "전화번호는 숫자로만 작성해주세요.", middleNum, lastNum);
            	return;
            }
            if (lastNum.getText().trim().length() != 4) {
                LogonController.warnPage("휴대폰 끝자리 4자리", "휴대폰 끝자리는 4자리입니다.", lastNum);
                return;
            }else if(lastNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", lastNum.getText().trim())) {
            	LogonController.warnPage("번호 재입력", "전화번호는 숫자로만 작성해주세요.", lastNum);
            	return;
            }

            // 시작 날짜 설정
            if (startDate.getValue() == null) {
                LogonController.warnPage("날짜 선택 필수", "시작할 날짜를 입력해 주세요.", startDate);
                return;
            }
            // 날짜가 현재 날짜보다 더 이전으로 설정한다면 경고발생
            Period period = Period.between(LocalDate.now(), startDate.getValue());
            if (period.getDays() < 0) {
                LogonController.warnPage("날짜 선택 오류", "시작 날짜가 현재보다 이전일 수 없습니다.", startDate);
                return;
            }

            // 성별 선택
            RadioButton value = (RadioButton)gender.selectedToggleProperty().getValue();

            // 데이터 받아오기
            String name = txtName.getText().trim();
            LocalDate date = startDate.getValue();
            String gen = value.getText();
            String firstPhone = firstNum.getText().trim();
            String middlePhone = middleNum.getText().trim();
            String lastPhone = lastNum.getText().trim();

            // User VO 생성
            user = new User(name, date, gen, firstPhone, middlePhone, lastPhone);

            dao.join(user);

            Alert info = new Alert(AlertType.INFORMATION);
            info.setHeaderText("회원등록 완료");
            info.showAndWait();
            
            Stage stage = (Stage)btnNext.getScene().getWindow();
            
            stage.close(); // DB에 유저 넣고 나면, 창 종료

        }); // 회원 추가 버튼 클릭


    }
    
}
