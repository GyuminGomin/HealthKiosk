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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MembershipController implements Initializable {

	@FXML
	private ComboBox<String> comboBox;
	@FXML
	private Button btnAdd, lockerbtn;
	@FXML
	private DatePicker startDate, endDate;
	
	private UserDAO dao = new UserDAOImpl();
	
	// 회원 추가 스테이지
	private Stage ownerStage;
	// 회원권 기간
	private int betweenMonth;
	private boolean isOpened = false; // 회원권 먼저 선택하세요 창이 두번 뜨지 않도록 하기 위한 플래그
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		startDate.setEditable(false);
		endDate.setEditable(false);

		endDate.setOnAction((e) -> {
			if (startDate.getValue() == null) {
				endDate.setValue(null);
				return;
			}
			setEndDate();
		});

		// startDate 설정시 endDate 설정
		startDate.setOnAction((e)->{
			if (comboBox.getSelectionModel().getSelectedItem() == null) {
				if (isOpened) {
					isOpened = !isOpened;
					return;
				}
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("회원권을 먼저 선택해주세요.");
				info.showAndWait();
				isOpened = true;
				startDate.setValue(null);
				return;
			}
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
			// 회원권 선택하지 않는다면 오류
			if (comboBox.getSelectionModel().getSelectedItem() == null) {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("회원권을 선택해주세요.");
				info.showAndWait();
				return;
			}

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
				try {
					Alert info = new Alert(AlertType.INFORMATION);
					info.setHeaderText("회원등록 완료");
					FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/CreateUserPage.fxml"));
					Parent createScene = loader.load();
					CreateUserController con = loader.getController();

					String membership = comboBox.getValue();
					LocalDate startDate1 = startDate.getValue();
					LocalDate endDate1 = endDate.getValue();
					Boolean userStatus = false; // 헬스장 시작 일짜가 오늘이 아님
					if (startDate1.isEqual(LocalDate.now())) {  // 헬스장을 등록하자마자 바로 시작한다면, 활성화 상태로 시작
						userStatus = true;
					}
					User u = con.setUserDatas(membership, startDate1, endDate1, userStatus);
					dao.join(u);
					ownerStage.close();
					info.showAndWait();
				} catch (IOException e1) {
					e1.printStackTrace();
					return;
				}
			} else {
				Alert info = new Alert(AlertType.INFORMATION);
				info.setHeaderText("다시 선택해주세요.");
				info.showAndWait();
				return;
			}
			Stage stage = (Stage) btnAdd.getScene().getWindow();
			stage.close();
		});
		
		// 락커등록 버튼 눌렸을때 LockerPage로 이동
	      lockerbtn.setOnAction(e-> {
	         Stage s = (Stage)lockerbtn.getScene().getWindow();
	         
	         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	         alert.setHeaderText("락커를 등록하시겠습니까 ?");
	         
	         ButtonType yesButton = new ButtonType("예", ButtonBar.ButtonData.YES);
	         ButtonType noButton = new ButtonType("아니오", ButtonBar.ButtonData.NO);
	         
	         alert.getButtonTypes().setAll(yesButton, noButton);
	         
	         Optional<ButtonType> result = alert.showAndWait();
	               
	          if (result.isPresent() && result.get() == yesButton) {
	              try {
	                  // FXMLLoader로 Loader를 생성하고 LockerPage를 로드합니다.
	                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/LockerPage.fxml"));
	                  Parent lockerPage = loader.load();

	                  // Scene을 LockerPage로 설정합니다.
	                  Scene scene = new Scene(lockerPage);

	                  // 새로운 Stage를 생성하고 Scene을 설정합니다.
	                  Stage newStage = new Stage(StageStyle.DECORATED);
	                  newStage.setScene(scene);
	                  // 새로운 Stage를 표시합니다.
	                  newStage.show();
	              } catch (IOException ex) {
	                  ex.printStackTrace();
	              }
	          }
	      });		
	}
	
	public void setOwnerStage(Stage s) {
		this.ownerStage = s;
	}

	// 시작일 입력시 회원권 기간에 따라 종료일 지정
	public void setEndDate() {
		if(startDate.getValue() != null) {
		LocalDate end =  startDate.getValue().plusMonths(betweenMonth);
		endDate.setValue(end);
		}
	}
	
}
