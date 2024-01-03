package application.dto;

import java.time.LocalDate;

public class User {
    
    private int userCode;
    private String userName, userPhoneNum, userGender, phoneHeader, phoneMiddle, phoneTail;
    private LocalDate userStartDate;
    private Boolean userStatus; // user 활성화 비활성화 상태

    
    public User(String userName, String userPhoneNum, String userGender, String phoneHeader,
            String phoneMiddle, String phoneTail, LocalDate userStartDate) {
        this.userName = userName;
        this.userPhoneNum = userPhoneNum;
        this.userGender = userGender;
        this.phoneHeader = phoneHeader;
        this.phoneMiddle = phoneMiddle;
        this.phoneTail = phoneTail;
        this.userStartDate = userStartDate;
        this.userStatus = true;
    } // 회원 생성할 때 필요

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
    public String getUserPhoneNum() {
        return userPhoneNum;
    }
    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
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
    public LocalDate getUserStartDate() {
        return userStartDate;
    }
    public void setUserStartDate(LocalDate userStartDate) {
        this.userStartDate = userStartDate;
    }

    
}
