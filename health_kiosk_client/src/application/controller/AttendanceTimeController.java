package application.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import application.dto.UserDTO;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AttendanceTimeController implements Initializable{

    @FXML
    private Label currentTime, currentDate;
    @FXML
    private TextField txtUserCode;
    @FXML
    private TextArea txtArea;
    @FXML
    private Button btnCheck, connServer;

    public static boolean isRun = true;

    public static List<UserDTO> userList = new ArrayList<>();

    // Client 정보
    // 연결된 Server의 소켓 정보를 저장할 Socket 멤버
	private Socket server;
	// 연결 요청을 보낼 server ip 주소
	private InetAddress ip;
	// 연결 요청을 보낼 server port 번호
	private int port;
	// 서버로 출력할 스트림
	private PrintWriter printer;
	// 서버에서 데이터를 입력받을 스트림
	private BufferedReader br;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        Platform.runLater(() -> {
            txtUserCode.requestFocus();
        });

        txtUserCode.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                btnCheck.fire();
            }
        });

        LocalDate date = LocalDate.now(); // 현재 날짜
        String dateData = "Today "+ date.format(DateTimeFormatter.ofPattern("MM/dd"));
        currentDate.setText(dateData);

        Thread t = new Thread(() -> {
            LocalTime time = LocalTime.now(); // 현재 시간
            while (isRun) {
                time = time.plusNanos(10000000);
                String timeData = "현재시간 " + time.format(DateTimeFormatter.ofPattern("hh:mm:ss"));

                Platform.runLater(() -> {
                    currentTime.setText(timeData);
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }); // thread end
        t.setDaemon(true);
        t.start();
        
        

        // 클라이언트 구축

        connServer.setOnAction(e -> {
            // 먼저 포트를 통해 server와 연결
            try {
                InetAddress local = InetAddress.getLocalHost();
                String ip = local.getHostAddress();
                this.ip = InetAddress.getByName(ip);
                this.port = 5000;
                server = new Socket(this.ip, this.port);
                displayText("[연결 완료 : " + server.getRemoteSocketAddress()+"]");

                // 출력 스트림
                printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
                // 입력 스트림
                br = new BufferedReader(new InputStreamReader(server.getInputStream()));

                send(2, ip); // 연결 완료

            } catch (IOException e1) {
                displayText("[서버 연결 안됨. 서버를 먼저 켜주시길 바랍니다.]");
                stopClient();
                return;
            }
            receive();
        });
        

        // textField 숫자만 조건 입력
        // 조건이 만족되면, 서버로 데이터 전송
        btnCheck.setOnAction(e-> {
            String code = txtUserCode.getText().trim();
            if (code.equals("") || !Pattern.matches("^[\\d]*$", code) || code.length() != 4) {
                displayText("[전화번호 뒷 4자리를 입력해주세요.]");
                return;
            } else {
                // 먼저 번호 보내고
                send(0, code);
                // 서버에서 유저 데이터를 찾아서 (name, phonetail) 을 보내면,
                // 받아서 uL에 저장

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

                // receive 받은 데이터를 기다려야 하는데... 비동기로 처리를 못해서 기다림
                if (AttendanceTimeController.userList.size() != 0) {
                    Stage stage = new Stage(StageStyle.UNDECORATED);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/ListViewPage.fxml"));
                    Parent root;
                    try {
                        root = loader.load();
                        stage.setScene(new Scene(root));
                        stage.setResizable(false);
                        stage.showAndWait(); // 창이 꺼질때가지 대기
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    if (ListViewController.userSelected != null) {
                        // listView 에서 데이터를 가져와서 전송
                        String[] s = currentTime.getText().split(" ");
                        String time = s[1];
                        String dat = LocalDate.now().toString();
                        System.out.println(dat);
                        send(1, ListViewController.userSelected.getPhoneTail(), ListViewController.userSelected.getUserName(), time, dat);
                        System.out.println(ListViewController.userSelected.getUserName());
                        // 전송 완료 후, 초기화
                        ListViewController.userSelected = null;
                        userList = new ArrayList<>();
                        displayText("출석완료 되었습니다. ㅗ");
                        // 전송 완료 끝
                        return;
                    }
                }
                displayText("출석 실패 (다른 유저코드를 입력해주세요.) ㅗ");
                
            }
        });
            
        
    } // end initialize
 
    // 자원 해제 후 client 종료
	public void stopClient() {
		displayText("[서버와 연결 종료]");
		if (server != null && !server.isClosed()) {
			try {
				server.close(); // server와 연결된 소켓 정보 닫기
			} catch (IOException e) {}
		}
	}
    
    // txtDisplay textArea에 UI thread를 이용하여 text 작성
	public void displayText(String text) {
		Platform.runLater(()-> {
			txtArea.appendText(text+"\n");
		});
	}

    // 서버한테 데이터 발송 0 = 첫번째 보내는 것, 1 = 두번째 보내는 것
    public void send(int code, String phoneTail) {
        printer.println(code+"|"+phoneTail);
    }
    // 1 = 두번째 보내는 것
    public void send(int code, String phoneTail, String name, String time, String date) {
        printer.println(code+"|"+phoneTail+"|"+name+"|"+time+"|"+date);
    }

    // 서버한테 데이터 수신
    public void receive() {
        Thread t = new Thread(()-> {
            while (true) {
                try {
                    String receiveData = br.readLine(); // 서버에서 발신된 데이터 한 라인씩 읽기
                    if (receiveData == null) {
                        break;
                    }
                    // 무조건 수신하는 데이터는 name 과 phoneTail 뿐
                    String[] data = receiveData.split("\\|");
                    String code = data[0];
                    String name = data[1];
                    String phoneTail = data[2];

                    switch (code) {
                        case "0" :
                            // 받아온 객체를 활용하기 위해 객체 저장
                            UserDTO u = new UserDTO(name, phoneTail);
                            AttendanceTimeController.userList.add(u); // 한줄씩 받아와서 저장
                            break;
                        default: // case 1 유저를 찾을 수 없음
                            displayText("존재하지 않는 회원번호입니다. 다시 입력해주세요.");
                            break;
                    }
                } catch (IOException e) {
                    break; // 더이상 수신할 수 없으면 종료
                }
            } // end while
            stopClient();
        });
        t.setDaemon(true);
        t.start();
    }
}
