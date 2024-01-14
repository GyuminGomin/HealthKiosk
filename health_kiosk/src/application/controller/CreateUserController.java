package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateUserController implements Initializable{

    @FXML
    private TextField txtName, firstNum, middleNum, lastNum;
    @FXML
    private ToggleGroup gender;
    @FXML
    private Button btnNext;
    @FXML
    private DatePicker regDate;

    // DB에서 받아온 데이터를 저장하는 객체(VO)
    private UserDAO dao = new UserDAOImpl();
    // User VO
    public static User user;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        LogonController.focusMove(txtName, firstNum, middleNum, lastNum);

        // 등록날짜는 무조건 오늘
        regDate.setValue(LocalDate.now());
        regDate.setEditable(false);

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
        	if(e1.getCode() == KeyCode.ENTER) regDate.requestFocus();
        });
        regDate.setOnKeyPressed(e1->{
        	if(e1.getCode() == KeyCode.ENTER) btnNext.fire();
        });

        // 회원 추가 버튼 클릭
        btnNext.setOnAction(e -> {

            Stage s = (Stage)txtName.getScene().getWindow();

            // 이름 설정 // 한글과 영어, 숫자로 입력가능하게 설정
            if (txtName.getText().trim().length() < 2 || txtName.getText().trim().length()>20) {
                LogonController.warnPage(s, "이름 입력 잘못", "이름은 2글자 이상 20글자 이하로 제한됩니다.", txtName);
                return;
            } else if (txtName.getText().trim() != null && !Pattern.matches("^[가-힣a-zA-Z0-9]*$", txtName.getText().trim())) {
            	LogonController.warnPage(s, "특수문자 입력 불가","이름을 영어, 한글, 숫자로만 작성해주세요.",txtName);
            	return;
            };

            // 휴대폰 설정 // 미션 -> 숫자만 입력가능하게 설정
            if (firstNum.getText().trim().length() != 3) {
                LogonController.warnPage(s, "휴대폰 앞자리 3자리", "대한민국 휴대폰 앞자리는 010, 011, 016, 017, 019 등이 존재합니다.", firstNum);
                return;
            } else if (firstNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", firstNum.getText().trim())) {
            	LogonController.warnPage(s, "번호 재입력", "전화번호는 숫자로만 작성해주세요.", firstNum, middleNum, lastNum);
            	return;
            }

            if (middleNum.getText().trim().length() != 4) {
                LogonController.warnPage(s, "휴대폰 지역별 할당번호는 4자리", "휴대폰 지역별 할당번호는 4자리입니다.", middleNum);
                return;
            } else if (middleNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", middleNum.getText().trim())) {
            	LogonController.warnPage(s, "번호 재입력", "전화번호는 숫자로만 작성해주세요.", middleNum, lastNum);
            	return;
            }

            if (lastNum.getText().trim().length() != 4) {
                LogonController.warnPage(s, "휴대폰 끝자리 4자리", "휴대폰 끝자리는 4자리입니다.", lastNum);
                return;
            } else if (lastNum.getText().trim() != null && !Pattern.matches("^[\\d]*$", lastNum.getText().trim())) {
            	LogonController.warnPage(s, "번호 재입력", "전화번호는 숫자로만 작성해주세요.", lastNum);
            	return;
            }

            if (!dao.selectUser(txtName.getText(), lastNum.getText())) { // 같은 이름 또는 전화번호 뒷자리가 존재한다면, 이미 존재하는 회원입니다.
                // 원래는 같은 전화번호 뒷자리에다 같은 이름이라면, 전화번호 분석을 통해 유저 코드를 새로 바꾸는 로직이 필요할 수도 있지만, 지금은 작은 프로세스이므로
                LogonController.warnPage(s, "회원 중복", "이미 존재하는 회원입니다.", txtName, middleNum, lastNum);
                return;
            }

            // 등록날짜는 무조건 오늘
            regDate.setValue(LocalDate.now());

            // User 객체 받아오기
            // 성별 선택
            RadioButton value = (RadioButton)gender.selectedToggleProperty().getValue();
            // 데이터 받아오기
            String name = txtName.getText().trim();
            LocalDate date = regDate.getValue();
            String gen = value.getText();
            String firstPhone = firstNum.getText().trim();
            String middlePhone = middleNum.getText().trim();
            String lastPhone = lastNum.getText().trim();

            user = new User(name, date, gen, firstPhone, middlePhone, lastPhone);
            
            // MembershipPage로 넘어가기
            Alert info1 = new Alert(AlertType.CONFIRMATION);
            info1.setHeaderText("회원권 등록 페이지로 넘어가시겠습니까?");
            Optional<ButtonType> result = info1.showAndWait(); 
            if(result.get() == ButtonType.OK) {
            	
            	Stage stage = null;
                FXMLLoader loader = null;
                Parent membershipPage = null;
                try {
                    stage = new Stage(StageStyle.DECORATED);
                    loader = new FXMLLoader(getClass().getResource("/application/fxml/MembershipPage.fxml"));
                    membershipPage = loader.load();
                    stage.setScene(new Scene(membershipPage));
                    stage.setTitle("회원권 등록 페이지");
                    stage.setResizable(false);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    // 회원 추가 스테이지 전달
                    MembershipController controller = loader.getController();
                    controller.setOwnerStage(s);
                    stage.show();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return;
                }
            }else {
            	Alert info = new Alert(AlertType.INFORMATION);
            	info.setHeaderText("재입력해주세요.");
            	info.showAndWait();
            	return;
            }
        }); // 회원 추가 버튼 클릭

    }
    
    public User setUserDatas(String membership, LocalDate regDate1, LocalDate endDate1, Boolean userStatus) {
        // User VO 생성
        User u = new User(user.getUserName(), user.getUserRegDate(), user.getUserGender(), user.getPhoneHeader(), user.getPhoneMiddle(), user.getPhoneTail(), membership
        		, regDate1, endDate1, userStatus);
        return u;
    }
}
