package application.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import application.dto.UserChild;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

// 고객관리>회원 페이지
public class UserController implements Initializable {

	@FXML
    private Label userName, active;
	@FXML
	private Label nowDate, restDays, membershipTic, lockerTic;
	@FXML
	private ProgressBar ticketBar, ticketBar2;
	@FXML
	private ProgressIndicator ticketIndicator;
    @FXML
    private TextField userNo, userGender, userPhone, userDate, startDate, endDate;
    @FXML
    private TextField lockerStartDate, lockerEndDate;
    
    UserDAO dao = new UserDAOImpl();
    User u = new User();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// ProgressBar 위 현재 날짜
		LocalDate now = LocalDate.now();
		String nowDate1 = now.toString();
		nowDate.setText(nowDate1);
	}

	public void setUserData(UserChild user) {
		// 인적사항란 작성
		String userName1 = user.getUserName();
		userName.setText(userName1);
		String userPhone1 = user.getUserPhone();
		userPhone.setText(userPhone1);
		String userGneder1 = user.getGender();
		userGender.setText(userGneder1);
		LocalDate userDate1 = user.getUserReg();
		String userDate2 = userDate1.toString();
		userDate.setText(userDate2);
		char[] charPhone = userPhone1.toCharArray();
		String userCode = new String(charPhone, 9, 4);
		userNo.setText(userCode);
		String active1 = user.getUserStatus();
		active.setText(active1);
		
		// 회원권란 작성
		LocalDate startDate1 = user.getUserStartDate();
		String startDate2 = startDate1.toString();
		LocalDate endDate1 = user.getUserEndDate();
		String endDate2 = endDate1.toString();
		startDate.setText(startDate2);;
		endDate.setText(endDate2);
		lockerStartDate.setText(startDate2);
		lockerEndDate.setText(endDate2);
		
		LocalDate now = LocalDate.now();
		int betweenDays = (int)ChronoUnit.DAYS.between(now, endDate1); // 뒤 - 앞
		String restDay1 = betweenDays + "일 남음";
		restDays.setText(restDay1);
		// ProgressBar에 남은 일수 표현
		int allDays = (int)ChronoUnit.DAYS.between(startDate1, endDate1); // 뒤 - 앞

		// 회원권 전체 일수로 나누기
		double progress = 1- (betweenDays/(double)allDays);
		ticketBar.setProgress(progress);
		ticketIndicator.setProgress(progress);
		
		String checkMembership = dao.getMemberData(userName1, userCode);
		membershipTic.setText(checkMembership);
	}
	
}
		
		
    
	
	

