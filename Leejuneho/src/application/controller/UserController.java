package application.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.UserChild;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

// 고객관리>회원 페이지
public class UserController implements Initializable {

	@FXML
    private Label userName, active;
	@FXML
	private Label nowDate, restDate;
	@FXML
	private ProgressBar ticketBar, ticketBar2;
	@FXML
	private ProgressIndicator ticketIndicator;
	@FXML
	private ComboBox<String> ticketBox;
    @FXML
    private TextField userNo, userGender, userPhone, userDate,startDate, endDate;
    
    UserDAO dao = new UserDAOImpl();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		LocalDate now = LocalDate.now();
		String nowDate1 = now.toString();
		nowDate.setText(nowDate1);
		
		
	}

	public void setUserData(UserChild user) {
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
		
	}
	
	
	
	
}
		
		
    
	
	

