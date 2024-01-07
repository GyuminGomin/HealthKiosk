package application.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.UserAtten;
import application.dto.UserChild;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserAttendanceController implements Initializable{

    @FXML
    private Label labelHome, totalAtten;
    @FXML
    private TableView<UserAtten> tableView; 
    @FXML
    private TextField searchField;
    @FXML
    private Button startServer;

    private UserDAO dao = new UserDAOImpl();

    // DB 서버 가동 부분
    // Client Thread를 관리할 스레드 풀
	ExecutorService serverPool;
	// 운영체제에서 요청한 포트로 프로세스를 할당받아 client socket 연결관리를 할 class
	ServerSocket server;
	// 연결된 client의 닉네임을 key값, 서버에서 발신할 정보를 value로 저장하는 Map 객체
	Hashtable<String, PrintWriter> clients; // <Client Id, socket 출력 스트림>

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        startServer.setOnAction(e-> {
            String text = startServer.getText();
            if (text.equals("Start _Server")) {
				startServer();
				startServer.setText("Stop S_erver");
			} else {
				stopServer();
				startServer.setText("Start _Server");
			}
        });

        Platform.runLater(()-> {
            Stage curStage = (Stage)tableView.getScene().getWindow();

            curStage.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F5) {
                    ObservableList<UserAtten> userList = FXCollections.observableArrayList();
                    for (UserAtten u : dao.userAtten()) {
                        userList.add(u);
                    }
                    // 첫 번째 TableColumn에 CheckBox 추가
                    TableColumn<UserAtten, Boolean> checkBoxColumn = (TableColumn<UserAtten, Boolean>)tableView.getColumns().get(0);

                    checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));

                    // 체크박스 셀의 내용을 표시하는 방법 지정 (옵션)
                    checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

                    checkBoxColumn.setEditable(false);

                    ObservableList<TableColumn<UserAtten, ?>> columnList = tableView.getColumns();
                    Field[] fields = UserAtten.class.getDeclaredFields();
                    for (int i=1; i<fields.length-1; i++) {
                        String fieldName = fields[i].getName();
                        TableColumn<UserAtten, ?> columnName = columnList.get(i);
                        columnName.setCellValueFactory(new PropertyValueFactory<>(fieldName));
                    }

                    tableView.setItems(userList);

                    // TableCell에 대한 업데이트를 처리하는 셀 팩토리 설정
                    checkBoxColumn.setCellFactory(col -> new CheckBoxTableCell<UserAtten, Boolean>(){
                        @Override
                        public void updateItem(Boolean item, boolean empty) {
                            super.updateItem(item, empty);
                            
                            if (empty || getIndex() < 0) {
                                setGraphic(null);
                            } else {
                                UserAtten user = getTableView().getItems().get(getIndex());
                                CheckBox checkBox = (CheckBox)getGraphic();
                                if (checkBox != null && user != null) {
                                    checkBox.setSelected(user.isChecked());
                                }
                            }
                        }
                    });
                }
            });
        });            

        
        // tableView Data 설정
        Platform.runLater(() -> {
            ObservableList<UserAtten> userList = FXCollections.observableArrayList();
            for (UserAtten u : dao.userAtten()) {
                userList.add(u);
            }
            // 첫 번째 TableColumn에 CheckBox 추가
            TableColumn<UserAtten, Boolean> checkBoxColumn = (TableColumn<UserAtten, Boolean>)tableView.getColumns().get(0);

            checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));

            // 체크박스 셀의 내용을 표시하는 방법 지정 (옵션)
            checkBoxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(checkBoxColumn));

            checkBoxColumn.setEditable(false);

            ObservableList<TableColumn<UserAtten, ?>> columnList = tableView.getColumns();
            Field[] fields = UserAtten.class.getDeclaredFields();
            for (int i=1; i<fields.length-1; i++) {
                String fieldName = fields[i].getName();
                TableColumn<UserAtten, ?> columnName = columnList.get(i);
                columnName.setCellValueFactory(new PropertyValueFactory<>(fieldName));
            }

            tableView.setItems(userList);

            // 테이블 뷰를 (한번) 클릭
            tableView.setOnMouseClicked(e -> {
                MouseButton btn = e.getButton();
                // 체크 표시
                if (btn == MouseButton.PRIMARY && e.getClickCount() == 1) {
                    UserAtten u = tableView.getSelectionModel().getSelectedItem();
                    if (u != null) {
                        u.setChecked(!u.isChecked());

                        tableView.refresh();
                    }
                }
            });

            // txt 이름으로, 전화번호 뒷자리로 검색
            // 테이블 필터링
            FilteredList<UserAtten> filteredList = new FilteredList<>(userList, p -> true);

            searchField.textProperty().addListener((obs, o, n) -> {
                filteredList.setPredicate(user -> {
                    if (n == null || n.isEmpty()) {
                        return true;
                    }
                    if (Pattern.matches("^[0-9]*$", n)) {
                        return user.getUserPhone().contains(n);
                    }
                    return user.getUserName().toLowerCase().contains(n.toLowerCase());
                });
                Platform.runLater(() -> {
                    tableView.setItems(filteredList);
                });
            }); // txt 이름으로, 전화번호 뒷자리로 검색


            // TableCell에 대한 업데이트를 처리하는 셀 팩토리 설정
            checkBoxColumn.setCellFactory(col -> new CheckBoxTableCell<UserAtten, Boolean>(){
                @Override
                public void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (empty || getIndex() < 0) {
                        setGraphic(null);
                    } else {
                        UserAtten user = getTableView().getItems().get(getIndex());
                        CheckBox checkBox = (CheckBox)getGraphic();
                        if (checkBox != null && user != null) {
                            checkBox.setSelected(user.isChecked());
                        }
                    }
                }
            });

            // 검색 날짜를 입력하면 그 날짜의 대상만 입력할 수 있게 수정 해야 함
            // 전체 목록에 현재 뜨고 있는 tableView의 인원 수 구하기
            int tableNum = tableView.getItems().size();
            String s = "전체 목록 : "+tableNum;
            totalAtten.setText(s);

        }); // tableView Data 설정

        // HealthKiosk 버튼 클릭
        labelHome.setOnMouseClicked(e-> {
            Stage stage = null;
            FXMLLoader loader = null;
            Parent homePage = null;
            try {
                stage = new Stage(StageStyle.DECORATED);

                loader = new FXMLLoader(getClass().getResource("/application/fxml/HomePage.fxml"));
                homePage = loader.load();

                stage.setScene(new Scene(homePage));
                stage.setTitle("홈 페이지");
                stage.setResizable(false);
                stage.show();
                Stage oldStage = (Stage)labelHome.getScene().getWindow();
                oldStage.close();

                // 서버 종료
                stopServer();

            } catch (IOException e1) {
                e1.printStackTrace();
                return;
            }
        });
    } // end initialize


    // 서버 실행 담당
    public void startServer() {
        serverPool = Executors.newFixedThreadPool(12); // 전체 회원 12명
        clients = new Hashtable<>();
        int port = 5000; // 실행 포트는 5000으로 설정
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("이미 사용중인 포트 번호입니다.");
            stopServer(); // 만약 현재 사용중 이라면
            return;
        }

        Runnable run = new Runnable() {
            @Override
            public void run() {
                System.out.println("[서버 시작]");
                while (true) {
                    try {
                        System.out.println("client 연결 대기 중..");
                        Socket client = server.accept();
                        String address = client.getRemoteSocketAddress().toString();
                        System.out.println("연결 수락 : "+address);
                        serverPool.submit(new ServerTask(client, UserAttendanceController.this));
                    } catch (IOException e) {
                        stopServer();
                        break;
                    }
                }
            }
        };
        serverPool.submit(run); // 작업 전달
    }

    // 서버 종료 자원해제 담당
    public void stopServer() {	
		try {
            if (clients != null && !clients.isEmpty()) {
            	for (PrintWriter p : clients.values()) {
            		if (p != null) {
            			p.close();
            		}
            	}
            	clients.clear();
            }
            	
            if(server != null && !server.isClosed()) {
            	try {
            		server.close();
            	} catch (IOException e) {}
            }
            
            if (serverPool != null && serverPool.isShutdown()) {
            	serverPool.shutdown();
            }
            System.out.println("[서버 중지]");
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    
}
