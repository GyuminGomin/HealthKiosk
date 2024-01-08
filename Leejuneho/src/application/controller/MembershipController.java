package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class MembershipController implements Initializable {

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Button btnAdd;
	
	private UserDAO dao = new UserDAOImpl();
	private User user;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		ObservableList<String> lists = FXCollections.observableArrayList(
				"프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원",
				"프리미엄 회원권 [1개월] + 운동복 + 개인락커                                 * 가격  :  200,000원"
				);
		comboBox.setItems(lists);
		
		
		
    
		
		btnAdd.setOnAction(e->{
			Alert info1 = new Alert(AlertType.CONFIRMATION);
			info1.setHeaderText("회원권을 등록하시겠습니까?");
			Optional<ButtonType> result = info1.showAndWait(); 
			if(result.get() == ButtonType.OK) {
				Alert info = new Alert(AlertType.INFORMATION);
	            info.setHeaderText("회원등록 완료");
	            FXMLLoader loader = null;
	            Parent CreateUserPage = null;
	            try {
	                loader = new FXMLLoader(getClass().getResource("/application/fxml/CreateUserPage.fxml"));
	                CreateUserPage = loader.load();
	                CreateUserController con= loader.getController();
	                String membership = comboBox.getValue();
	                User u = con.setUserDatas(membership);
	                System.out.println(u);
	                dao.join(u);
	                
	            } catch (IOException e1) {
	            	e1.printStackTrace();
	            	return;
            }
            info.showAndWait();
            
        }else {
        	Alert info = new Alert(AlertType.INFORMATION);
        	info.setHeaderText("회원권을 선택해주세요.");
        	info.showAndWait();
        	return;
        }
        
        
        
        Stage stage = (Stage)btnAdd.getScene().getWindow();
        
        stage.close();
		});
		
		}
	}
		


