package application.dao;

public interface UserDAO {
    
    // 아이디가 기존에 존재하는지 확인
	boolean selectMember(String userId);
}
