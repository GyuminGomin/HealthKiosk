package application.dao;

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

    // // 모든 사용자 받아오기
    // List<User> userManage();

    // userManagement 페이지의 테이블 뷰에 넣기 위해 조회 후 저장
    List<UserChild> userManage();

    // userAttendance 페이지의 테이블 뷰에 넣기 위해 조회 후 저장
    // 클라이언트에서 데이터를 받아와서 DB에 올리는 부분
    List<UserAtten> userAtten();

    
}
