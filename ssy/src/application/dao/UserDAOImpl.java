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

        List<Integer> userStatusNum = new ArrayList<>();
        int userStatusInt = 0;

        String sql = "SELECT count(userStatus) FROM user GROUP BY userStatus";


        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                userStatusNum.add(rs.getInt(1));
            }
            if (activated == true) {
                userStatusInt = userStatusNum.get(0);
            } else userStatusInt = userStatusNum.get(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return userStatusInt;
    }
}
