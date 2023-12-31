package application.dao;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.dto.User;
import application.utils.DBUtil;

public class UserDAOImpl implements UserDAO{

    Connection conn;
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    @Override
    public boolean selectMember(String userId) {
        boolean isChecked = true; // 존재하지 않으면

        String sql = "SELECT userId, userPasswd FROM user WHERE userId = ?";
        conn = DBUtil.getConnection();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) { // 존재하면
                isChecked = false;
            }
        } catch (SQLException e) { // 쿼리 에러
            isChecked = false;
        } finally {
            DBUtil.close(rs, pstmt);
        }
        return isChecked;
    }

    @Override
    public User join(User user) {
        User u = null;

        String sql = "INSERT INTO user (userId, userPasswd, userName, userGender, userBirth, userEmail, userPhone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        conn = DBUtil.getConnection();

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserPasswd());
            pstmt.setString(3, user.getUserName());
            pstmt.setString(4, user.getUserGender());
            Date date = null;
            if (user.getUserBirth() != null) {
                date = Date.valueOf(user.getUserBirth());
            }
            pstmt.setDate(5, date);
            pstmt.setString(6, user.getUserEmail());
            pstmt.setString(7, user.getPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
    
}
