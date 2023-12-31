package application.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class LogonController implements Initializable{

    @FXML
    private TextField id, passwd, chkPasswd, name, email, phoneNum;
    @FXML
    private DatePicker birth;
    @FXML
    private ToggleGroup group;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 회원가입이 성공되어야 DB에 연동해서 유저 탑재
    }
    
}
