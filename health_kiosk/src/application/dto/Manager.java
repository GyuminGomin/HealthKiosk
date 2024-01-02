package application.dto;

import java.time.LocalDate;

// DB에서 받아온 데이터를 저장하는 객체 (VO)
public class Manager {
    
    private int managerCode; // 회원번호
    private String managerId; // 아이디
    private String managerPasswd; // 패스워드
    private String managerName; // 이름
    private String managerGender; // 성별 (남자, 여자)
    private LocalDate managerBirth; // 생년월일
    private String managerEmail; // 이메일
    private String managerPhone; // 폰번호

    public Manager() {}

    public Manager(int managerCode, String managerId, String managerPasswd, String managerName, String managerGender,
            LocalDate managerBirth, String managerEmail, String managerPhone) {
        this.managerCode = managerCode;
        this.managerId = managerId;
        this.managerPasswd = managerPasswd;
        this.managerName = managerName;
        this.managerGender = managerGender;
        this.managerBirth = managerBirth;
        this.managerEmail = managerEmail;
        this.managerPhone = managerPhone;
    } // DB에 있는 데이터 찾아오기

    public Manager(String managerId, String managerPasswd, String managerName, String managerGender,
            LocalDate managerBirth, String managerEmail, String managerPhone) {
        this.managerId = managerId;
        this.managerPasswd = managerPasswd;
        this.managerName = managerName;
        this.managerGender = managerGender;
        this.managerBirth = managerBirth;
        this.managerEmail = managerEmail;
        this.managerPhone = managerPhone;
    } // 회원가입시 DB에 저장

    public Manager(int managerCode, String managerId, String managerPasswd, String managerName, String managerGender) {
        this.managerCode = managerCode;
        this.managerId = managerId;
        this.managerPasswd = managerPasswd;
        this.managerName = managerName;
        this.managerGender = managerGender;
    }

    public Manager(String managerId, String managerPasswd, String managerName, String managerGender) {
        this.managerId = managerId;
        this.managerPasswd = managerPasswd;
        this.managerName = managerName;
        this.managerGender = managerGender;
    }

    
    @Override
    public String toString() {
        return "관리자번호=" + managerCode + "\n관리자아이디=" + managerId + "\n관리자이름=" + managerName;
    }


    // 회원 정보의 Id와 passwd가 일치하면 동일한 객체로 인식
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Manager) {
            Manager u = (Manager)obj;
            if (this.managerId.equals(u.getManagerId()) && this.managerPasswd.equals(u.getManagerPasswd())) {
                return true;
            }
        }
        return false;
    }

    public int getManagerCode() {
        return managerCode;
    }

    public void setManagerCode(int managerCode) {
        this.managerCode = managerCode;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerPasswd() {
        return managerPasswd;
    }

    public void setManagerPasswd(String managerPasswd) {
        this.managerPasswd = managerPasswd;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerGender() {
        return managerGender;
    }

    public void setManagerGender(String managerGender) {
        this.managerGender = managerGender;
    }

    public LocalDate getManagerBirth() {
        return managerBirth;
    }

    public void setManagerBirth(LocalDate managerBirth) {
        this.managerBirth = managerBirth;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

}
