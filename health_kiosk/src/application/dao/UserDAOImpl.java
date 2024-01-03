package application.dao;

import java.sql.Statement;
import java.sql.Connection;
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

    @Override
    public User createUser(User user) {
        // TODO
        return null;
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
    public Boolean statusUser(User user) {

        Boolean userStatus = false;

        String sql = "SELECT count(userStatus) FROM user GROUP BY userStatus";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                status
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }



        return userStatus;
    }
}
