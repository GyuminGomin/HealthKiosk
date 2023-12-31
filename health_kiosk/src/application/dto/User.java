package application.dto;

// DB에서 받아온 데이터를 저장하는 객체 (VO)
public class User {
    
    private int memberId;
    private String userId;
    private String userPasswd;
    private String userName;
    
    public User() {}

    public User(int memberId, String userId, String userPasswd, String userName) {
        this.memberId = memberId;
        this.userId = userId;
        this.userPasswd = userPasswd;
        this.userName = userName;
    }
    
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserPasswd() {
        return userPasswd;
    }
    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "회원번호=" + memberId + "\n회원아이디=" + userId + "\n회원이름=" + userName;
    }

}
