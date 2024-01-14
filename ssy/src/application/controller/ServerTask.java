package application.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import application.dao.UserDAO;
import application.dao.UserDAOImpl;

public class ServerTask implements Runnable{

    // 현재 task에 연결된 client socket 정보
	Socket client;
	UserAttendanceController sc;
	// 연결된 Clinet에 출력할 스트림
	PrintWriter printer;
	// client에서 출력된 데이터를 입력받을 스트림
	BufferedReader reader;
	// 연결된 client의 ip 정보를 저장
	String ip;
	// 연결된 client task 종료 flag
	boolean isRun = true;

	UserDAO dao = new UserDAOImpl();
	
	// 연결 정보가 바뀌면서 Socket 정보가 바뀌는 것을 방지하기 위해
	public ServerTask(Socket client, UserAttendanceController uc) {
		this.client = client;
		this.sc = uc;
        try {
			OutputStream os = client.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os); // 2byte
			BufferedWriter writer = new BufferedWriter(osw); // 속도 향상
			printer = new PrintWriter(writer, true); // auto flush
			
			reader = new BufferedReader( // String 기반으로 한줄씩 읽어올 수 있게 해주는 것
					new InputStreamReader(client.getInputStream())
			);
			// 입 출력 스트림 초기화 
			
		} catch (IOException e) {
			// (만약 여기서 문제가 발생했다면)
			System.out.println("Client 연결 오류 : " +e.getMessage());
		}
	}

    @Override
    public void run() {
		System.out.println(client.getRemoteSocketAddress()+ " receive 시작");
		while (isRun) {
			// client 에서 발신 된 메시지 수신
			try {
				String receiveData = reader.readLine();
				if (receiveData == null) {
					break;
				}
				// 클라이언트에서 받아오는 데이터는 번호랑, 현재 시간과, 현재 날짜 3개
				// 번호를 받아오는데, 뒷 자리가 같은 번호라면, Server에서 회원의 이름을 받아와서 어떤 회원인지 입력하는 새로운 창을 띄어줘야함.
				String[] data = receiveData.split("\\|");
				// 번호 0 : 첫번째 확인 수신
				if (data[0].equals("0")) { // code == 0
					String phoneTail = data[1];

					// 전송을 해줘야 함.
					// DB 활용 user 중 phoneTail이 같은 것 을 찾아야 하므로
					List<String> sD = dao.sendData1(phoneTail);
					if (sD != null) {
						for (String s : sD) {
							printer.println("0|"+s);
							// data 전송 성공
						}
					} else { // data 전송 실패
						printer.println("1|x|x");
					}

				} else if (data[0].equals("1")) { // code == 1
					String phoneTail = data[1];
					String name = data[2];
					String t = data[3];
					LocalTime time = LocalTime.parse(t);
					String d = data[4];
					LocalDate date = LocalDate.parse(d);
					// 이제 이 데이터들을 가지고 출석 DB에 연결
					int userCode = dao.getUserByCode(name, phoneTail);
					dao.attendance(name, phoneTail, userCode, time, date); // DB에 데이터 넣고 refresh 해주면 됨

				} else if (data[0].equals("2")) { // code = 2
					this.ip = data[1];
					sc.clients.put(ip, printer);
					System.out.println(ip+"에서 접속했습니다.");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				isRun = false;
			}
		} // end while
		// 연결된 client와 수신 작업 종료
		if (client != null && !client.isClosed()) {
			// serverTask 종료 시 - 연결된 socket된 자원 해제
			try {
				client.close();
			} catch (IOException e) {}
		}

		// 종료한 client 정보 삭제 및 사용자 목록 갱신
		sc.clients.remove(this.ip);
	}
}
