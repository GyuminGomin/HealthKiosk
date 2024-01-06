package application.dto;

public class UserDTO {
    
    private String userName;
    private String phoneTail;
    
    public UserDTO(String userName, String phoneTail) {
        this.userName = userName;
        this.phoneTail = phoneTail;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneTail() {
        return phoneTail;
    }
    public void setPhoneTail(String phoneTail) {
        this.phoneTail = phoneTail;
    }
    @Override
    public String toString() {
        return userName + "|" + phoneTail;
    }
    
}
