package application.dao;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.dto.User;
import application.utils.DBUtil;

public class UserDAOImpl implements UserDAO {
    
    Connection conn = DBUtil.getConnection();
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    // User 등록 구현 중
    @Override
    public void join(User user) {

        String sql = "INSERT INTO user (userName, userstartDate, userGender, phoneHeader, phoneMiddle, phoneTail, userStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUserName());
            Date date = null;
            if (user.getUserStartDate() != null) {
                date = Date.valueOf(user.getUserStartDate());
            }
            pstmt.setDate(2, date);
            pstmt.setString(3, user.getUserGender());
            pstmt.setString(4, user.getPhoneHeader());
            pstmt.setString(5, user.getPhoneMiddle());
            pstmt.setString(6, user.getPhoneTail());
            pstmt.setBoolean(7, user.getUserStatus());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
        }
    }

    @Override
    public int countUser() {
        int count = 0;

        String sql = "SELECT count(*) FROM user";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                count = rs.getInt(1); // 숫자만 가져와서
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }

        return count;

    }

    @Override
    public int statusUserNum(Boolean activated) {
        int userStatusInt = 0;
        int actTrue = 0;
        int actFalse = 0;
        String sql = "SELECT sum(CASE WHEN userStatus=1 THEN 1 ELSE 0 END), sum(CASE WHEN userStatus=0 THEN 1 ELSE 0 END) FROM user";


        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                actTrue = rs.getInt(1);
                actFalse = rs.getInt(2);
            }
            if (activated == true) {
                userStatusInt = actTrue;
                return userStatusInt;
            } else {
                userStatusInt = actFalse;
                return userStatusInt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return userStatusInt;
    }

    @Override
    public int UserGenderNum(String gen) {
        int userStatusInt = 0;
        int actTrue = 0;
        int actFalse = 0;
        String sql = "SELECT sum(CASE WHEN userGender='남자' THEN 1 ELSE 0 END), sum(CASE WHEN userGender='여자' THEN 1 ELSE 0 END) from user";
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                actTrue = rs.getInt(1);
                actFalse = rs.getInt(2);
            }
            if (gen.equals("남자")) {
                userStatusInt = actTrue;
                return userStatusInt;
            } else if (gen.equals("여자")) {
                userStatusInt = actFalse;
                return userStatusInt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return userStatusInt;
    }
}
