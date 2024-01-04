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
        List<Integer> actStatusNum = new ArrayList<>();
        int actStatusInt = 0;

        String sql = "SELECT count(lockerActivated) FROM locker GROUP BY lockerActivated";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                actStatusNum.add(rs.getInt(1));
            }
            if (activated == true) {
                actStatusInt = actStatusNum.get(0);
                if (actStatusNum.size() == 1) return actStatusInt;
            } else {
                if (actStatusNum.size() == 1) {
                    return 0;  
                }
                actStatusInt = actStatusNum.get(1);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return actStatusInt;
    }
    
}
