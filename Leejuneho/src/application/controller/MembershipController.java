package application.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
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
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class MembershipController implements Initializable {

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Button btnAdd;
	@FXML
	private DatePicker startDate, endDate;
	
	private UserDAO dao = new UserDAOImpl();
	private User user;
	
	// 회원 추가 스테이지
	private Stage ownerStage;
	// 회원권 기간
	int betweenMonth;
	// 시작일 입력시 회원권 기간에 따라 종료일 지정
	public void setEndDate() {
		if(startDate.getValue() != null) {
		LocalDate end =  startDate.getValue().plusMonths(betweenMonth);
		endDate.setValue(end);
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// startDate 설정시 endDate 설정
		startDate.setOnAction((e)->{
			setEndDate();
		});
		ObservableList<String> lists = FXCollections.observableArrayList(
				"프리미엄 통합 회원권 [3개월] + 운동복 + 개인락커                             * 가격  :  500,000원",
				"프리미엄 회원권 [1개월] + 운동복 + 개인락커                                 * 가격  :  200,000원");
		comboBox.setItems(lists);
		// 콤보 박스 항목 선택 변경 감지 이벤트 리스터 추가
		comboBox.getSelectionModel().selectedIndexProperty().addListener((t, o, n)->{
			int index = n.intValue();
			switch(index) {
			case 0 :
				betweenMonth = 3;
				setEndDate();
				break;
			case 1 :
				betweenMonth = 1;
				setEndDate();
				break;
			}
		});

		btnAdd.setOnAction(e -> {
			
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
			
			Alert info1 = new Alert(AlertType.CONFIRMATION);
			info1.setHeaderText("회원권을 등록하시겠습니까?");
			Optional<ButtonType> result = info1.showAndWait();
			if (result.get() == ButtonType.OK) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("회원등록 완료");
				FXMLLoader loader = null;
				Parent CreateUserPage = null;
				try {
					loader = new FXMLLoader(getClass().getResource("/application/fxml/CreateUserPage.fxml"));
					CreateUserPage = loader.load();
					CreateUserController con = loader.getController();
					String membership = comboBox.getValue();
					LocalDate startDate1 = startDate.getValue();
					LocalDate endDate1 = endDate.getValue();
					User u = con.setUserDatas(membership, startDate1, endDate1);
					System.out.println(u);
					dao.join(u);
					// 정상 추가 완료 시 회원 추가 스테이지도 종료
					ownerStage.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
				info.showAndWait();
			} else {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("회원권을 선택해주세요.");
				info.showAndWait();
				return;
			}
			Stage stage = (Stage) btnAdd.getScene().getWindow();
			stage.close();
		});

	}

	public void setOwnerStage(Stage s) {
		this.ownerStage = s;
		
	}
}
