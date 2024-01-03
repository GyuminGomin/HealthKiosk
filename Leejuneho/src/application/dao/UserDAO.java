package application.dao;

import application.dto.User;

public interface UserDAO {

    // 현재 회원 추가
    User createUser(User user);

    // 현재 회원 수 조회 (탈퇴한 회원은 조회 안함)
    int countUser();

    // 활성화 된 회원 비 활성 회원 설정
    
    // 활성화, 비활성화 회원 가져오기
    Boolean statusUser(User user);
}
