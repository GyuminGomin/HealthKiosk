package application.dao;

public interface LockerDAO {

    // 전체 락커 수 조회
    int countLocker();
    
    // 활성화, 비활성화 락커 수 조회
    int statusActivatedNum(Boolean activated);
}
