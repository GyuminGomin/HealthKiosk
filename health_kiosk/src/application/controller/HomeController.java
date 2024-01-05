package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.dao.LockerDAO;
import application.dao.LockerDAOImpl;
import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.Manager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HomeController implements Initializable {

    @FXML
    private Button logout, btnAddUser;
    @FXML
    private ImageView btnLayout1, btnLayout2;
    @FXML
    private VBox panel;
    @FXML
    private Label loginManager, totalUser, activateUser, inactivateUser, allUser, actUser, inactUser, totalLocker, activateLocker, inactivateLocker;
    @FXML
    private ProgressBar allUserBar, actUserBar, inactUserBar, allLockerBar, actLockerBar, inactLockerBar;
    @FXML
    private PieChart pieChartGender, pieChartLocker;

    // DB에서 받아온 데이터를 저장하는 User객체(VO)
    UserDAO dao = new UserDAOImpl();

    // DB에서 받아온 데이터를 저장하는 Locker객체(VO)
    LockerDAO ldao = new LockerDAOImpl();

    private AnchorPane custManage, attendance, salManage, salStatistic, locker;
    private boolean custFlag = true, salFlag = true;
    Manager m = LoginController.loginManager; // 현재 로그인한 매니저
    

	@Override
	public void initialize(URL location, ResourceBundle resources) {

        // 현재 로그인 한 매니저의 이름과 id를 가져와서 저장
        String loginM = m.getManagerId() + " : " + m.getManagerName();
        loginManager.setText(loginM);

        // 현재 회원 수 출력 해주기
        String countUser = "전체 회원 "+dao.countUser()+"명";
        totalUser.setText(countUser);
        // ProgressBar 전체 회원 수
        double maxValue = 12;
        double progress = dao.countUser() / maxValue;
        allUserBar.setProgress(progress);
		String count = "만료를 포함한 등록된 회원은 총 " +dao.countUser()+ "명 입니다";
		allUser.setText(count);
        
        // 현재 활성화된 회원, 비활성화 된 회원
        String activateU = "활성 회원 "+dao.statusUserNum(true)+"명";
        activateUser.setText(activateU);
        // ProgressBar 활성 회원 수
        maxValue = dao.countUser();
        progress = dao.statusUserNum(true) / maxValue;
        actUserBar.setProgress(progress);
		count = "전체 회원 중 활성 회원은 " +dao.statusUserNum(true)+ "명 입니다";
		actUser.setText(count);

        String inactivateU = "비활성 회원 "+dao.statusUserNum(false)+"명";
        inactivateUser.setText(inactivateU);
        // ProgressBar 비활성 회원 수
        progress = dao.statusUserNum(false) / maxValue;
        inactUserBar.setProgress(progress);
		count = "전체 회원 중 비활성화 회원은 " +dao.statusUserNum(false)+ "명 입니다";
		inactUser.setText(count);


        // 락커 수 출력 해주기
        String countLocker = "전체 락커 : "+ldao.countLocker()+"개";
        totalLocker.setText(countLocker);
        // ProgressBar 전체 락커 수
        maxValue = 15;
        progress = ldao.countLocker() / maxValue;
        allLockerBar.setProgress(progress);

        // 현재 활성화된 회원, 비활성화 된 회원
        String activateL = "활성 락커 : "+ldao.statusActivatedNum(true)+"개";
        activateLocker.setText(activateL);
        // ProgressBar 활성 락커 수
        maxValue = ldao.countLocker();
        progress = ldao.statusActivatedNum(true) / maxValue;
        actLockerBar.setProgress(progress);

        String inactivateL = "비활성화 락커 : "+ldao.statusActivatedNum(false)+"개";
        inactivateLocker.setText(inactivateL);
        // ProgressBar 비활성 락커 수
        progress = ldao.statusActivatedNum(false) / maxValue;
        inactLockerBar.setProgress(progress);

        // pieChartGender View
        pieChartGender.setTitle("활성 회원 성별 비");
        ObservableList<PieChart.Data> glist = FXCollections.observableArrayList();
        glist.add(new PieChart.Data("남자", dao.UserGenderNum("남자")));
        glist.add(new PieChart.Data("여자", dao.UserGenderNum("여자")));
        pieChartGender.setData(glist);

        
        // PieChartGender 마우스 이동시
        Platform.runLater(() -> {
            Label caption = new Label("");
            caption.setFont(new Font(20));
            caption.setStyle("-fx-background-color: rgba(0,0,0,0);");
            caption.setTextAlignment(TextAlignment.CENTER);

            Stage s = new Stage(StageStyle.TRANSPARENT);
            AnchorPane a = new AnchorPane();
            a.setStyle("-fx-background-color: rgba(0,0,0,0);");
            Scene scene = new Scene(a);
            scene.setFill(Color.TRANSPARENT);
            a.getChildren().add(caption);
            s.setScene(scene);
            
            for (PieChart.Data d : pieChartGender.getData()) {
                d.getNode().setOnMouseMoved((e) -> {
                    caption.setText(String.format("%.1f%%",d.getPieValue()/dao.countUser()*100));

                    s.setX(e.getScreenX()+20);
                    s.setY(e.getScreenY());
                    
                    if (!s.isShowing()) {
                        s.show();
                    }
                });
                d.getNode().setOnMouseExited(e -> {
                    s.close();
                });
            }
        }); // pieChartGender 마우스 이동시

        // pieChartLocker View
        pieChartLocker.setTitle("락커 현황");
        ObservableList<PieChart.Data> llist = FXCollections.observableArrayList();
        llist.add(new PieChart.Data("활성", ldao.statusActivatedNum(true)));
        llist.add(new PieChart.Data("비활성", ldao.statusActivatedNum(false)));
        pieChartLocker.setData(llist);

        
        // PieChartLocker 마우스 이동시
        Platform.runLater(() -> {
            Label caption = new Label("");
            caption.setFont(new Font(20));
            caption.setStyle("-fx-background-color: rgba(0,0,0,0);");
            caption.setTextAlignment(TextAlignment.CENTER);

            Stage s = new Stage(StageStyle.TRANSPARENT);
            AnchorPane a = new AnchorPane();
            a.setStyle("-fx-background-color: rgba(0,0,0,0);");
            Scene scene = new Scene(a);
            scene.setFill(Color.TRANSPARENT);
            a.getChildren().add(caption);
            s.setScene(scene);
            
            for (PieChart.Data d : pieChartLocker.getData()) {
                d.getNode().setOnMouseMoved((e) -> {
                    caption.setText(String.format("%.1f%%",d.getPieValue()/ldao.countLocker()*100));

                    s.setX(e.getScreenX()+20);
                    s.setY(e.getScreenY());
                    
                    if (!s.isShowing()) {
                        s.show();
                    }
                });
                d.getNode().setOnMouseExited(e -> {
                    s.close();
                });
            }
        });

        // f5를 눌러서 Stage 리다이렉트
        Platform.runLater(()-> {
            Stage curStage = (Stage)panel.getScene().getWindow();

            curStage.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F5) {
                    curStage.close();
                    try {
                        // 새로운 Stage 열기
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                        Parent root = loader.load();
                        Stage newStage = new Stage();
                        newStage.setScene(new Scene(root));
                        newStage.setTitle("홈 페이지");
                        newStage.setResizable(false);
                        newStage.show();        
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        });
        
        custManage = (AnchorPane)panel.getChildren().get(2);
        attendance = (AnchorPane)panel.getChildren().get(3);
        salManage = (AnchorPane)panel.getChildren().get(6);
        salStatistic = (AnchorPane)panel.getChildren().get(7);
        locker = (AnchorPane)panel.getChildren().get(4);

        // 로그아웃 버튼 클릭
        logout.setOnAction(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent loginPage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/LoginPage.fxml"));
                loginPage = loader.load();

                stage.setScene(new Scene(loginPage));
                stage.setTitle("홈 페이지");
                Stage homePage = (Stage)logout.getScene().getWindow();
                stage.setResizable(false);
                stage.show();
                homePage.close();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 로그아웃 버튼 클릭
//------------------------------------------------------------------
//        locker.setOnMouseMoved(new EventHandler<Event>() {
//            @Override
//            public void handle(MouseDragEvent event) {
//            	locker.setStyle("-fx-background-color: red;");
//            }
//        });
//------------------------------------------------------------------

        // 회원 세모박스 클릭
        btnLayout1.setOnMouseClicked(e -> {
            if (custFlag) {
                // custFlag가 true일 때
                panel.getChildren().removeAll(custManage, attendance);
            } else {
                // custFlag가 false일 때
                panel.getChildren().add(2, custManage);
                panel.getChildren().add(3, attendance);
            }
            custFlag = !custFlag;
        }); // 회원 세모박스 클릭

        // 회계 세모박스 클릭
        btnLayout2.setOnMouseClicked(e -> {
            if (salFlag) {
                // salFlag가 true일 때
                panel.getChildren().removeAll(salManage,salStatistic);
            } else {
                // salFlag가 false일 때
                panel.getChildren().addAll(salManage, salStatistic);
            }
            salFlag = !salFlag;
        }); // 회계 세모박스 클릭

        // 회원 등록 버튼 클릭
        btnAddUser.setOnAction(e -> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent createUserPage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/CreateUserPage.fxml"));
                createUserPage = loader.load();

                stage.setScene(new Scene(createUserPage));
                stage.setTitle("회원 등록 페이지");
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 회원 등록 버튼 클릭

        // 고객 관리 버튼 클릭
        custManage.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent managementPage = null;
            
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/UserManagementPage.fxml"));
                managementPage = loader.load();

                stage.setScene(new Scene(managementPage));
                stage.setTitle("고객 관리 페이지");
                stage.setResizable(false);
                stage.show();
                Stage homePage = (Stage)logout.getScene().getWindow();
                homePage.close();
                // 고객관리에서 종료키 클릭시 homePage로 돌아감
                stage.setOnCloseRequest(e1->{
                	Stage stage1 = null;
                	FXMLLoader loader1 = null;
        			Parent homePage1 = null;
        			try {
        				stage1 = new Stage(StageStyle.DECORATED);
        				loader1 = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
        				homePage1 = loader1.load();
        				
        				stage1.setScene(new Scene(homePage1));
        				stage1.setTitle("홈 페이지");
        				stage1.setResizable(false);
        				stage1.show();
        			} catch (IOException e2) {
        				e2.printStackTrace();
        			}
                });
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 고객 관리 버튼 클릭

        // 출석부 버튼 클릭
        attendance.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent attendancePage = null;
            
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/UserAttendancePage.fxml"));
                attendancePage = loader.load();

                stage.setScene(new Scene(attendancePage));
                stage.setTitle("출석부 페이지");
                stage.setResizable(false);
                stage.show();
                Stage homePage = (Stage)logout.getScene().getWindow();
                homePage.close();
                // 출석부에서 종료키 클릭시 homePage로 돌아감
                stage.setOnCloseRequest(e1->{
                	Stage stage1 = null;
                	FXMLLoader loader1 = null;
        			Parent homePage1 = null;
        			try {
        				stage1 = new Stage(StageStyle.DECORATED);
        				loader1 = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
        				homePage1 = loader1.load();
        				
        				stage1.setScene(new Scene(homePage1));
        				stage1.setTitle("홈 페이지");
        				stage1.setResizable(false);
        				stage1.show();
        			} catch (IOException e2) {
        				e2.printStackTrace();
        			}
                });
            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 출석부 버튼 클릭

        // 락커 버튼 클릭
        locker.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent lockerPage = null;
            
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/LockerPage.fxml"));
                lockerPage = loader.load();

                stage.setScene(new Scene(lockerPage));
                stage.setTitle("락커 페이지");
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        }); // 락커 버튼 클릭
        
	}

}

