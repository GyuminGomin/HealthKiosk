package application.dto;

import java.time.LocalDate;

public class User {

	private int userCode;
    private String userName, userGender, phoneHeader, phoneMiddle, phoneTail;
	private LocalDate userRegDate, startDate, endDate; // 등록일, 시작일, 종료일
    private Boolean userStatus = true; // user 활성화 비활성화 상태
    private String membership;

	public User() {}

    public User(int userCode, String userName, String userGender, String phoneHeader, String phoneMiddle,
            String phoneTail, LocalDate userRegDate, Boolean userStatus) {
        this.userCode = userCode;
        this.userName = userName;
        this.userGender = userGender;
        this.phoneHeader = phoneHeader;
        this.phoneMiddle = phoneMiddle;
        this.phoneTail = phoneTail;
        this.userRegDate = userRegDate;
        this.userStatus = userStatus;
    } // 회원 조회할 때 필요

    public User(String userName, LocalDate userRegDate, String userGender, String phoneHeader,
            String phoneMiddle, String phoneTail) {
        this.userName = userName;
        this.userGender = userGender;
        this.phoneHeader = phoneHeader;
        this.phoneMiddle = phoneMiddle;
        this.phoneTail = phoneTail;
        this.userRegDate = userRegDate;
    } // 회원 생성할 때 필요
    
    public User(String userName, LocalDate userRegDate, String userGender, String phoneHeader,
    		String phoneMiddle, String phoneTail, String membership, LocalDate startDate, LocalDate endDate, Boolean userStatus) {
    	this.userName = userName;
    	this.userGender = userGender;
    	this.phoneHeader = phoneHeader;
    	this.phoneMiddle = phoneMiddle;
    	this.phoneTail = phoneTail;
    	this.userRegDate = userRegDate;
    	this.membership = membership;
    	this.startDate = startDate;
    	this.endDate = endDate;
        this.userStatus = userStatus;
    } // 회원권 생성시 필요


	public int getUserCode() {
        return userCode;
    }
    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public LocalDate getUserRegDate() {
    	return userRegDate;
    }
    public void setUserRegDate(LocalDate userRegDate) {
    	this.userRegDate = userRegDate;
    }
    public String getUserGender() {
        return userGender;
    }
    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
    public String getPhoneHeader() {
        return phoneHeader;
    }
    public void setPhoneHeader(String phoneHeader) {
        this.phoneHeader = phoneHeader;
    }
    public String getPhoneMiddle() {
        return phoneMiddle;
    }
    public void setPhoneMiddle(String phoneMiddle) {
        this.phoneMiddle = phoneMiddle;
    }
    public String getPhoneTail() {
        return phoneTail;
    }
    public void setPhoneTail(String phoneTail) {
        this.phoneTail = phoneTail;
    }
    public Boolean getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(Boolean userStatus) {
        this.userStatus = userStatus;
    }
    public String getMembership() {
		return membership;
	}
	public void setMembership(String membership) {
		this.membership = membership;
	}
	public LocalDate getStartDate() {
		return startDate;
	}
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
	public LocalDate getEndDate() {
		return endDate;
	}
	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}
	

	@Override
	public String toString() {
		return "User [userCode=" + userCode + ", userName=" + userName + ", userGender=" + userGender + ", phoneHeader="
				+ phoneHeader + ", phoneMiddle=" + phoneMiddle + ", phoneTail=" + phoneTail + ", userRegDate="
				+ userRegDate + ", userStatus=" + userStatus + ", membership=" + membership + "]";
	}
    
}
