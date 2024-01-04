package application.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import application.utils.DBUtil;

public class LockerDAOImpl implements LockerDAO{

    Connection conn = DBUtil.getConnection();
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    @Override
    public int countLocker() {
        int count = 0;

        String sql = "SELECT count(*) FROM locker";

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
    public int statusActivatedNum(Boolean activated) {
        int actStatusInt = 0;
        int actTrue = 0;
        int actFalse = 0;

        String sql = "SELECT sum(CASE WHEN lockerActivated=1 THEN 1 ELSE 0 END), sum(CASE WHEN lockerActivated=0 THEN 1 ELSE 0 END) FROM locker";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                actTrue = rs.getInt(1);
                actFalse = rs.getInt(2);
            }
            if (activated == true) {
                actStatusInt = actTrue;
                return actStatusInt;
            } else {
                actStatusInt = actFalse;
                return actStatusInt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return actStatusInt;
    }
    
}
