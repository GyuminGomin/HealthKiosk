package application.controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import application.dto.UserChild;
import application.utils.DBUtil;
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
    
    private Connection conn = DBUtil.getConnection();
    private PreparedStatement pstmt;
    private ResultSet rs;
    
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
		System.out.println("UserController : " + user);
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
		System.out.println(userCode);
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
		// localDate엔 초가 없으니 atStartOfDay로 하루 시작 시간 사용
		LocalDateTime startingDay = now.atStartOfDay();
		LocalDateTime endingDay = endDate1.atStartOfDay();
		// Duration.between()을 이용해 날짜의 차이를 초로 변경 후 toDays로 초를 일수로 변환
		int betweenDays = (int) Duration.between(startingDay, endingDay).toDays();
		String restDay1 = betweenDays + "일 남음";
		restDays.setText(restDay1);
		// ProgressBar에 남은 일수 표현
		LocalDateTime firstDay = startDate1.atStartOfDay();
		int allDays = (int) Duration.between(firstDay, endingDay).toDays();
		// 회원권 전체 일수로 나누기
		double maxValue = 0;
		if(allDays > 31) {
			maxValue = 91;
			}else {
		maxValue = 31;
			};
		// 남은 일수가 감소할 수록 progressBar는 증가
		double progress = 1- (betweenDays / maxValue);
		ticketBar.setProgress(progress);
		ticketIndicator.setProgress(progress);
		
		String checkMembership = getMemberData(userName1);
		System.out.println(checkMembership);
		membershipTic.setText(checkMembership);
	}
	// 회원 당 어떤 회원권을 등록했는지 확인
	public String getMemberData(String name) {
		String memberCard = null;
		String sql = "SELECT userName, membership FROM user WHERE userName=?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				String memberCard1  = rs.getString(2);
				
				memberCard = memberCard1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memberCard;
	}
	
}
		
		
    
	
	

