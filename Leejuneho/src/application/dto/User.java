package application.dto;

import java.time.LocalDate;

// DB에서 받아온 데이터를 저장하는 객체 (VO)
public class User {

	private int memberId; // 회원번호
	private String userId; // 아이디
	private String userPasswd; // 패스워드
	private String userName; // 이름
	private String userGender; // 성별 (남자, 여자)
	private LocalDate userBirth; // 생년월일
	private String userEmail; // 이메일
	private String phone; // 폰번호

	public User() {
	}

	public User(String userId, String userPasswd, String userName, String userGender) {
		this.userId = userId;
		this.userPasswd = userPasswd;
		this.userName = userName;
		this.userGender = userGender;
	} // 회원가입 할 때, NN 필드만

	public User(int memberId, String userId, String userPasswd, String userName, String userGender) {
		this.memberId = memberId;
		this.userId = userId;
		this.userPasswd = userPasswd;
		this.userName = userName;
		this.userGender = userGender;
	}

	public User(String userId, String userPasswd, String userName, String userGender, LocalDate userBirth,
			String userEmail, String phone) {
		this.userId = userId;
		this.userPasswd = userPasswd;
		this.userName = userName;
		this.userGender = userGender;
		this.userBirth = userBirth;
		this.userEmail = userEmail;
		this.phone = phone;
	} // 회원가입시 DB에 저장

	public User(int memberId, String userId, String userPasswd, String userName, String userGender, LocalDate userBirth,
			String userEmail, String phone) {
		this.memberId = memberId;
		this.userId = userId;
		this.userPasswd = userPasswd;
		this.userName = userName;
		this.userGender = userGender;
		this.userBirth = userBirth;
		this.userEmail = userEmail;
		this.phone = phone;
	} // DB에 있는 데이터 찾아오기

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

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public LocalDate getUserBirth() {
		return userBirth;
	}

	public void setUserBirth(LocalDate userBirth) {
		this.userBirth = userBirth;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "회원번호=" + memberId + "\n회원아이디=" + userId + "\n회원이름=" + userName;
	}

	// 회원 정보의 Id와 passwd가 일치하면 동일한 객체로 인식
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User u = (User) obj;
			if (this.userId.equals(u.getUserId()) && this.userPasswd.equals(u.getUserPasswd())) {
				return true;
			}
		}
		return false;
	}

}
