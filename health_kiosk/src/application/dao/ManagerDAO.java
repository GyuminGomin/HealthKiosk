package application.dao;

import application.dto.Manager;

public interface ManagerDAO {
    
    // 회원가입
    Manager join(Manager manager);

    // 아이디가 기존에 존재하는지 확인
	boolean selectMember(String managerId);

    // Id와 Passwd가 일치하는 사용자 검색
    Manager selectMember(String managerId, String managerPasswd);

}
