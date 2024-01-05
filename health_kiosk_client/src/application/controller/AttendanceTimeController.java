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
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class AttendanceTimeController implements Initializable{

    @FXML
    private Label currentTime, currentDate;
    @FXML
    private TextField txtUserCode;
    @FXML
    private TextArea txtArea;
    @FXML
    private Button btnCheck;

    public static boolean isRun = true;
    
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

        // 먼저 포트를 통해 server와 연결
        try {
            InetAddress local = InetAddress.getLocalHost();
            String ip = local.getHostAddress();
            server = new Socket(ip, 5000);
            displayText("[연결 완료 : ]" + server.getRemoteSocketAddress()+"]");

            // 출력 스트림
            printer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(server.getOutputStream())), true);
            // 입력 스트림
            br = new BufferedReader(new InputStreamReader(server.getInputStream()));

        } catch (UnknownHostException e1) {
            displayText("[IP 주소 형태가 다릅니다.]");
        } catch (IOException e1) {
            displayText("[서버 연결 안됨. 서버를 먼저 켜주시길 바랍니다.]");
            stopClient();
            return;
        }
        

        
        
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

    // 서버한테 데이터 수신
    public void receive() {
        Thread t = new Thread(()-> {
            while (true) {
                try {
                    String receiveData = br.readLine(); // 서버에서 발신된 데이터 한 라인씩 읽기
                    if (receiveData == null) {
                        break;
                    }

                    // 여기서 부터 수신 받은 데이터에 따라 기준 나누기
                    // 1|ㄻㄹㄴ|
                    // 2|ㄴㄹㅇㅁㄴㄹㄴㄹ| 이런 느낌으로

                    
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
