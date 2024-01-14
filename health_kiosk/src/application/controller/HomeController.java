package application.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.dao.LockerDAO;
import application.dao.LockerDAOImpl;
import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.Manager;
import application.utils.DBUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
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
            
            // DB 연결 종료
            curStage.setOnCloseRequest(e-> {
                DBUtil.close(DBUtil.getConnection());
            });

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

        // 회원 세모박스 클릭
        btnLayout1.setOnMouseClicked(e -> {
            if (custFlag) {
                // custFlag가 true일 때
                panel.getChildren().removeAll(custManage, attendance);
                // 세모박스를 클릭했을때 색(이미지) 변환
                btnLayout1.setImage(new Image("/application/img/아래화살표_빨강.png"));
            } else {
                // custFlag가 false일 때
                panel.getChildren().add(2, custManage);
                panel.getChildren().add(3, attendance);
                // 세모박스를 클릭했을때 원래이미지로 돌아옴
                btnLayout1.setImage(new Image("/application/img/아래화살표.png"));
            }
            custFlag = !custFlag;
        }); // 회원 세모박스 클릭

        // 회계 세모박스 클릭
        btnLayout2.setOnMouseClicked(e -> {
            if (salFlag) {
                // salFlag가 true일 때
                panel.getChildren().removeAll(salManage,salStatistic);
                // 세모박스를 클릭했을때 색(이미지) 변환
                btnLayout2.setImage(new Image("/application/img/아래화살표_빨강.png"));
            } else {
                // salFlag가 false일 때
                panel.getChildren().addAll(salManage, salStatistic);
                // 세모박스를 클릭했을때 원래이미지로 돌아옴
                btnLayout2.setImage(new Image("/application/img/아래화살표.png"));
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
            try {
                stage = new Stage(StageStyle.DECORATED);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/UserManagementPage.fxml"));
                Parent managementPage = loader.load();

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
            try {
                Stage stage = new Stage(StageStyle.DECORATED);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/UserAttendancePage.fxml"));
                Parent attendancePage = loader.load();

                stage.setScene(new Scene(attendancePage));
                stage.setTitle("출석부 페이지");
                stage.setResizable(false);
                stage.show();
                Stage homePage = (Stage)logout.getScene().getWindow();
                homePage.close();

                // 출석부에서 종료키 클릭시 homePage로 돌아감
                stage.setOnCloseRequest(e1->{
                    e1.consume(); // X를 비활성화
                    Button btnServer = (Button)stage.getScene().lookup("#startServer"); // server 버튼을 가져오고
                    System.out.println(btnServer.getText());
                    // 서버가 가동중일 때, X 클릭
                    if (btnServer.getText().equals("Stop S_erver")) {
                        Alert info1 = new Alert(AlertType.CONFIRMATION);
                        info1.setHeaderText("서버가 가동중입니다. 종료하시겠습니까?");
                        Optional<ButtonType> result = info1.showAndWait();
                        if (result.get() == ButtonType.OK) {
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
                                // 서버 종료
                                UserAttendanceController controller = loader.getController();
                                controller.stopServer();
                                stage.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            // 서버 종료안하겠다면, 아무런 변화없이 그대로
                            return;
                        }
                    } else { // 서버가 가동중이 아닐 때, X 클릭
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
                    }
                });
            } catch (IOException e1) {
                e1.printStackTrace();
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
        
        // 매출통계 버튼 클릭
        salStatistic.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent lockerPage = null;
            
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/SalesPage.fxml"));
                lockerPage = loader.load();

                stage.setScene(new Scene(lockerPage));
                stage.setTitle("매출 통계 페이지");
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        });


        // 마우스 이벤트 (들어왔을 때)
        custManage.setOnMouseEntered(e -> {
        	custManage.setStyle("-fx-background-color : yellowgreen; -fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)custManage.getChildren().get(0);
        	l.setUnderline(true);
        });
        attendance.setOnMouseEntered(e -> {
        	attendance.setStyle("-fx-background-color : yellowgreen; -fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)attendance.getChildren().get(0);
        	l.setUnderline(true);
        });
        locker.setOnMouseEntered(e ->{
        	locker.setStyle("-fx-background-color : orange; -fx-border-width : 0 0 1 0; -fx-border-color : black;");
        });
        salManage.setOnMouseEntered(e -> {
        	salManage.setStyle("-fx-background-color : yellowgreen; -fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)salManage.getChildren().get(0);
        	l.setUnderline(true);
        });
        salStatistic.setOnMouseEntered(e -> {
        	salStatistic.setStyle("-fx-background-color : yellowgreen; -fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)salStatistic.getChildren().get(0);
        	l.setUnderline(true);
        });

        // 마우스 이벤트 (나왔을 때)
        custManage.setOnMouseExited(e -> {
        	custManage.setStyle("-fx-background-color : null;");
        	custManage.setStyle("-fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)custManage.getChildren().get(0);
        	l.setUnderline(false);
        });
        attendance.setOnMouseExited(e -> {
        	attendance.setStyle("-fx-backgrond-color : null;");
        	attendance.setStyle("-fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)attendance.getChildren().get(0);
        	l.setUnderline(false);
        });
        locker.setOnMouseExited(e ->{
        	locker.setStyle("-fx-background-color : null;");
        	locker.setStyle("-fx-border-width : 0 0 1 0; -fx-border-color : black;");
        });
        salManage.setOnMouseExited(e ->{
        	salManage.setStyle("-fx-background-color : null;");
        	salManage.setStyle("-fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)salManage.getChildren().get(0);
        	l.setUnderline(false);
        });
        salStatistic.setOnMouseExited(e -> {
        	salStatistic.setStyle("-fx-background-color : null;");
        	salStatistic.setStyle("-fx-border-width : 0 0 1 0; -fx-border-color : black;");
        	Label l = (Label)salStatistic.getChildren().get(0);
        	l.setUnderline(false);
        });   
	}
}

