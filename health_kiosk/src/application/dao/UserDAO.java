package application.dao;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import application.dto.User;
import application.dto.UserAtten;
import application.dto.UserChild;

public interface UserDAO {


    // 회원 등록
    void join(User user);

    // 현재 회원 수 조회 (탈퇴한 회원은 조회 안함)
    int countUser();
    
    // 활성화, 비활성화 회원 수 조회
    int statusUserNum(Boolean activated);

    // 활성화 된 회원 중 성별비 조회
    int UserGenderNum(String gen);

    // userManagement 페이지의 테이블 뷰에 넣기 위해 조회 후 저장
    List<UserChild> userManage();

    // user의 이름과, userCode가 같은 회원이 존재하는지 확인
    boolean selectUser(String userName, String phoneTail);

    // 서버에서 클라이언트로 확인 후 첫번째 전송해주기 위한 부분
    List<String> sendData1(String phoneTail);

    // 사용자 기반으로 userCode 조회
    int getUserByCode(String name, String tail);

    // userCode, name, tail로 Attendance DB에 데이터 저장
    void attendance(String name, String tail, int Code, LocalTime time, LocalDate date);

    // userAttendance 페이지의 테이블 뷰에 넣기 위해 조회 후 저장
    // 클라이언트에서 데이터를 받아와서 DB에 올리는 부분
    List<UserAtten> userAtten();

    // 회원 당 어떤 회원권을 등록했는지 확인
    String getMemberData(String name, String tail);
}
