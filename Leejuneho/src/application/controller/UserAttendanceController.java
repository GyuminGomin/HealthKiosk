package application.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;
import application.dto.UserAtten;
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
import javafx.scene.control.DatePicker;
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
    private Button startServer, searching;
    @FXML
    private DatePicker searchingDate;

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

        // tableView Data 설정
        Platform.runLater(() -> {
            tableReloading();
        }); // tableView Data 설정

        // server 설정
        startServer.setOnAction(e-> {
            String text = startServer.getText();
            if (text.equals("Start _Server")) {
				startServer();
				startServer.setText("Stop S_erver");
			} else {
				stopServer();
				startServer.setText("Start _Server");
			}
        }); // server 설정

        // f5 눌렀을 때
        Platform.runLater(()-> {
            Stage curStage = (Stage)tableView.getScene().getWindow();

            curStage.getScene().setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F5) {
                    tableReloading();
                    searchingDate.setValue(null);
                }
            });
        }); // f5 눌렀을 때   

        

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

    // 테이블 뷰 설정
    private void tableReloading() {
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
        for (int i=1; i<fields.length; i++) {
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

        // 검색 버튼 클릭 테이블 뷰에는 현재 date가 없긴 한데, 여기서의 user 정보를 가져와서
        searching.setOnAction(e-> {
            ObservableList<UserAtten> usL = FXCollections.observableArrayList();
            LocalDate date = searchingDate.getValue();
            searchingDate.setEditable(false);

            if (date == null) {
                tableView.setItems(userList);
            } else {
                for (UserAtten u : userList) {
                    if (u.getDoHealthDate().isEqual(date)) {
                        usL.add(u);
                    }
                }
                tableView.setItems(usL);
            }
            // 전체 목록 조회
            int tableNum = tableView.getItems().size();
            String s = "전체 목록 : "+tableNum;
            totalAtten.setText(s);
            // 전체 목록 조회
            
        });

        // txt 이름으로, 전화번호 뒷자리로 검색
        // 테이블 필터링
        FilteredList<UserAtten> filteredList = new FilteredList<>(userList, p -> true);

        searchField.textProperty().addListener((obs, o, n) -> {
            filteredList.setPredicate(user -> {
                if (searchingDate.getValue() != null) {
                    // 이름 또는 전화번호에 대한 필터링
                    boolean nameMatch = user.getUserName().toLowerCase().contains(n.toLowerCase());
                    boolean phoneMatch = Pattern.matches("^[0-9]*$", n) && user.getUserPhone().contains(n);

                    // DatePicker의 값이 있는 경우 해당 날짜에 대한 필터링
                    LocalDate datePickerValue = searchingDate.getValue();
                    boolean dateMatch = datePickerValue != null && user.getDoHealthDate().isEqual(datePickerValue);
                    
                    // (이름, 날짜), (번호, 날짜) 같으면 true
                    return (nameMatch && dateMatch) || (phoneMatch && dateMatch);
                } else {
                    // 이름 또는 전화번호에 대한 필터링
                    boolean nameMatch = user.getUserName().toLowerCase().contains(n.toLowerCase());
                    boolean phoneMatch = Pattern.matches("^[0-9]*$", n) && user.getUserPhone().contains(n);

                    return nameMatch || phoneMatch;
                }
            });
            Platform.runLater(() -> {
                tableView.setItems(filteredList);
                // 전체 목록 조회
                int tableNum = tableView.getItems().size();
                String s = "전체 목록 : "+tableNum;
                totalAtten.setText(s);
                // 전체 목록 조회
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
        }); // TableCell에 대한 업데이트를 처리하는 셀 팩토리 설정

        // 전체 목록 조회
        int tableNum = tableView.getItems().size();
        String s = "전체 목록 : "+tableNum;
        totalAtten.setText(s);
        // 전체 목록 조회

        

    }
}
