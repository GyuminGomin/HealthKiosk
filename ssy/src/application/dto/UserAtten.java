package application.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserAtten {
    
    private Boolean checked; // checkBox
    private int userCode; // No
    private String userName; // 이름
    private String userGender; // 성별
    private String userPhone; // 연락처
    private LocalDateTime doHealthTime; // 출석 시간
    private LocalDate doHealthDate; // 출석한 날짜


    public UserAtten(Boolean checked, int userCode, String userName, String userGender, String userPhone, LocalDateTime doHealthTime,
            LocalDate doHealthDate) {
        this.checked = checked;
        this.userCode = userCode;
        this.userName = userName;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.doHealthTime = doHealthTime;
        this.doHealthDate = doHealthDate;
    } // 출석부 등록 필수

    public Boolean isChecked() {
        return checked;
    }
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
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
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public LocalDateTime getDoHealthTime() {
        return doHealthTime;
    }
    public void setDoHealthTime(LocalDateTime doHealthTime) {
        this.doHealthTime = doHealthTime;
    }
    public LocalDate getDoHealthDate() {
        return doHealthDate;
    }
    public void setDoHealthDate(LocalDate doHealthDate) {
        this.doHealthDate = doHealthDate;
    }

    public Boolean getChecked() {
        return checked;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }
}
