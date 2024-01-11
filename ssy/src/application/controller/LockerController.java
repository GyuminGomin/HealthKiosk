package application.controller;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.dao.LockerDAO;
import application.dao.LockerDAOImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LockerController implements Initializable {

    @FXML
    private VBox locker1, locker2, locker3, locker4, locker5, locker6, locker7,
            locker8, locker9, locker10, locker11, locker12, locker13, locker14, locker15;

    @FXML
    private Button btn1;

    private VBox selectedLocker;
    private boolean isRed = false;

    LockerDAO dao = new LockerDAOImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 락커 클릭 이벤트
        for (int i = 1; i <= 15; i++) {
            VBox locker = getLocker(i);

            locker.setOnMouseClicked(e -> {
                if (selectedLocker != null) {
                    // 이전에 선택한 사물함의 색상을 재설정합니다.
                    if (isRed) {
                        selectedLocker.setStyle("-fx-background-color: orange;");
                    } else {
                        selectedLocker.setStyle("-fx-background-color: pink;");
                    }
                }
                // 클릭한 락커의 색상
                selectedLocker = locker;
                locker.setStyle("-fx-background-color: orange;");
            });
        }

        // 버튼 클릭 이벤트
        btn1.setOnAction(e -> {
            if (selectedLocker != null) {
                // 회원가입버튼을 눌렸을 때 선택된 락커의 색상 변경
                selectedLocker.setStyle("-fx-background-color: skyblue;");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("락커를 등록하시겠습니까 ?");
                ButtonType yesButton = new ButtonType("예", ButtonBar.ButtonData.YES);
                ButtonType noButton = new ButtonType("아니오", ButtonBar.ButtonData.NO);
                alert.getButtonTypes().setAll(yesButton, noButton);
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == yesButton) {
                    // LockerPage를 닫기
                    Stage stage = (Stage) btn1.getScene().getWindow();
                    stage.close();
                    return;
                }
           }
        });
    }

    // locker1 ~ locker15
    private VBox getLocker(int number) {
        switch (number) {
            case 1:
                return locker1;
            case 2:
            	return locker2;
            case 3:
                return locker3;
            case 4:
                return locker4;
            case 5:
                return locker5;
            case 6:
                return locker6;
            case 7:
                return locker7;
            case 8:
                return locker8;
            case 9:
                return locker9;
            case 10:
                return locker10;
            case 11:
                return locker11;
            case 12:
                return locker12;
            case 13:
                return locker13;
            case 14:
                return locker14;
            case 15:
                return locker15;
            default:
                throw new IllegalArgumentException("Invalid locker number: " + number);
        }
    }
    
}
