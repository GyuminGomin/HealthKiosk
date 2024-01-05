package application.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerTask implements Runnable{

    // 현재 task에 연결된 client socket 정보
	Socket client;
	UserAttendanceController sc;
	// 연결된 Clinet에 출력할 스트림
	PrintWriter printer;
	// client에서 출력된 데이터를 입력받을 스트림
	BufferedReader reader;
	// 연결된 client의 nickName 정보를 저장
	String nickName;
	// 연결된 client task 종료 flag
	boolean isRun = true;
	
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
    public void run() {}
}
