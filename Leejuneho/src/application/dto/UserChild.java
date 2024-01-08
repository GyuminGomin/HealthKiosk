package application.dto;

import java.time.LocalDate;

// Table View에 저장하기 위해서 순서대로 가져와야 해 상속은 포기
// 이 dto는 UserManagement를 다루기 위한 객체
public class UserChild{
    
    private Boolean checked; // checkBox
    private int userCode; // No
    private String userStatus; // 상태 (활성화 비활성화)
    private String userName; // 이름
    private String gender; // 성별
    private String userPhone; // 연락처
    private LocalDate userReg; // 회원 등록 일
    private LocalDate userStartDate; // 시작일
    private LocalDate userEndDate; // 종료일

    public UserChild(Boolean checked, int userCode, String userStatus, String userName, String gender, String userPhone, LocalDate userReg, 
            LocalDate userStartDate, LocalDate userEndDate) {
        this.checked = checked;
        this.userCode = userCode;
        this.userStatus = userStatus;
        this.userName = userName;
        this.gender = gender;
        this.userPhone = userPhone;
        this.userReg = userReg;
        this.userStartDate = userStartDate;
        this.userEndDate = userEndDate;
    }

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
    public String getUserStatus() {
        return userStatus;
    }
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public LocalDate getUserStartDate() {
        return userStartDate;
    }
    public void setUserStartDate(LocalDate userStartDate) {
        this.userStartDate = userStartDate;
    }
    public LocalDate getUserEndDate() {
        return userEndDate;
    }
    public void setUserEndDate(LocalDate userEndDate) {
        this.userEndDate = userEndDate;
    }

    public Boolean getChecked() {
        return checked;
    }

    public LocalDate getUserReg() {
        return userReg;
    }

    public void setUserReg(LocalDate userReg) {
        this.userReg = userReg;
    }

	@Override
	public String toString() {
		return "UserChild [checked=" + checked + ", userCode=" + userCode + ", userStatus=" + userStatus + ", userName="
				+ userName + ", gender=" + gender + ", userPhone=" + userPhone + ", userReg=" + userReg
				+ ", userStartDate=" + userStartDate + ", userEndDate=" + userEndDate + "]";
	}
    
    
    
    
}
