package application.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.UserChild;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

// 고객관리>회원 페이지
public class UserController implements Initializable {

	@FXML
    private Label userName;
    
    @FXML
    private TextField userNo, userGender, userPhone, userDate;
    
    UserDAO dao = new UserDAOImpl();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// 계속 해봐도 안됨...뭐가 잘못 되었을까...
		List<UserChild> userNameList = dao.userManage();
		if(!userNameList.isEmpty()) {
			String userName1 = userNameList.get(2).getUserName() +"님";
			userName.setText(userName1);
		}
		
		
		
				
	}
	
}
		
		
    
	
	

