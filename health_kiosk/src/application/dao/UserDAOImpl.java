package application.dao;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import application.dto.User;
import application.utils.DBUtil;

public class UserDAOImpl implements UserDAO{

    Connection conn = DBUtil.getConnection();
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    @Override
    public boolean selectMember(String userId) {
        boolean isChecked = true; // 존재하지 않으면

        String sql = "SELECT userId, userPasswd FROM user WHERE userId = ?";

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
        } finally {
            DBUtil.close(pstmt);
        }
        return u;
    }

    // TODO
    @Override
    public User selectMember(String userId, String userPasswd) {
        User u = null;

        String sql = "SELECT ? FROM user WHERE userId=? AND userPasswd=?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, userPasswd);
            rs = pstmt.executeQuery();
            u = getMember(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt);
        }

        return u;
    }

    public User getMember(ResultSet rs) throws SQLException {
        User u = null;
        LocalDate birth = null;
        if (rs.next()) {
            if (rs.getDate(6) != null) {
                birth = rs.getDate(6).toLocalDate();
            }
            u = new User(
                rs.getInt(1), // memeber_id
                rs.getString(2), // id
                rs.getString(3), // passwd
                rs.getString(4), // name
                rs.getString(5), // Gender
                birth,
                rs.getString(7), // email
                rs.getString(8) // phone
            );
        }
        return u;
    }
    
}
