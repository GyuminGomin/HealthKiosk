package application.dto;

public class Locker {
    private int lockerCode;
    private String lockerOwner; // 락커 소유주
    private String lockerTailNum; // 휴대폰 번호 뒷자리 4개
    private Boolean lockerActivated; // 활성화 상태
    
    public Locker(int lockerCode, String lockerOwner, String lockerTailNum, Boolean lockerActivated) {
        this.lockerCode = lockerCode;
        this.lockerOwner = lockerOwner;
        this.lockerTailNum = lockerTailNum;
        this.lockerActivated = lockerActivated;
    } // 락커 등록 시 필요

    public int getLockerCode() {
        return lockerCode;
    }
    public void setLockerCode(int lockerCode) {
        this.lockerCode = lockerCode;
    }
    public String getLockerOwner() {
        return lockerOwner;
    }
    public void setLockerOwner(String lockerOwner) {
        this.lockerOwner = lockerOwner;
    }
    public String getLockerTailNum() {
        return lockerTailNum;
    }
    public void setLockerTailNum(String lockerTailNum) {
        this.lockerTailNum = lockerTailNum;
    }
    public Boolean getLockerActivated() {
        return lockerActivated;
    }
    public void setLockerActivated(Boolean lockerActivated) {
        this.lockerActivated = lockerActivated;
    }
}
