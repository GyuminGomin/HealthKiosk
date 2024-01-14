package application.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.dto.UserDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

public class ListViewController implements Initializable{

    @FXML
    private ListView<String> listView;

    public static UserDTO userSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> usL = FXCollections.observableArrayList();

        ObservableList<UserDTO> uL = FXCollections.observableArrayList();
        uL.addAll(AttendanceTimeController.userList);

        for (UserDTO u : uL) {
            usL.add(u.toString());
        }
        listView.setItems(usL);
        
        
        
        // 여기서 리스트 뷰의 한 곳을 더블 클릭하면, uL을 초기화
        listView.setOnMouseClicked(e -> {
            MouseButton btn = e.getButton();
            if (btn == MouseButton.PRIMARY && e.getClickCount() == 2) {
                String u = listView.getSelectionModel().getSelectedItem();
                String[] usr= u.split("\\|");
                if (u != null) {
                    userSelected = informationPage(new UserDTO(usr[0], usr[1]));
                    Platform.runLater(() -> {
                        Stage stage = (Stage)listView.getScene().getWindow();
                        stage.close();
                    });
                }
            }
        });

    }

    public UserDTO informationPage(UserDTO u) {
        Alert inf = new Alert(AlertType.CONFIRMATION);
        inf.setTitle("출석 회원 확인");
        inf.setHeaderText("출석하시겠습니까?");
        Optional<ButtonType> res = inf.showAndWait();
        if (res.get() == ButtonType.OK) {
            Alert info = new Alert(AlertType.INFORMATION);
            info.setHeaderText("출석 완료");
            info.showAndWait();
            return u;
        } else {
            Alert info = new Alert(AlertType.INFORMATION);
            info.setHeaderText("다시 입력해 주세요.");
            info.showAndWait();
            AttendanceTimeController.userList = new ArrayList<>();
            return null;
        }
    }
    
}
