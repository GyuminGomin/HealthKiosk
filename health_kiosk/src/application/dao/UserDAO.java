package application.dao;

import application.dto.User;

public interface UserDAO {
    
    // 회원가입
    User join(User user);

    // 아이디가 기존에 존재하는지 확인
	boolean selectMember(String userId);
}
