package application.dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import application.dto.Manager;
import application.utils.DBUtil;

public class ManagerDAOImpl implements ManagerDAO{

    Connection conn = DBUtil.getConnection();
    Statement stmt;
    PreparedStatement pstmt;
    ResultSet rs;

    @Override
    public boolean selectMember(String managerId) {
        boolean isChecked = true; // 존재하지 않으면

        String sql = "SELECT managerId, managerPasswd FROM manager WHERE managerId = ?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, managerId);
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
    public Manager join(Manager manager) {
        Manager m = null;

        String sql = "INSERT INTO manager (managerId, managerPasswd, managerName, managerGender, managerBirth, managerEmail, managerPhone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, manager.getManagerId());
            pstmt.setString(2, manager.getManagerPasswd());
            pstmt.setString(3, manager.getManagerName());
            pstmt.setString(4, manager.getManagerGender());
            Date date = null;
            if (manager.getManagerBirth() != null) {
                date = Date.valueOf(manager.getManagerBirth());
            }
            pstmt.setDate(5, date);
            pstmt.setString(6, manager.getManagerEmail());
            pstmt.setString(7, manager.getManagerPhone());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
        }
        return m;
    }

    @Override
    public Manager selectMember(String managerId, String managerPasswd) {
        Manager m = null;

        String sql = "SELECT * FROM manager WHERE managerId=? AND managerPasswd=?";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, managerId);
            pstmt.setString(2, managerPasswd);
            rs = pstmt.executeQuery();
            m = getMember(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, pstmt);
        }
        return m;
    }

    public Manager getMember(ResultSet rs) throws SQLException {
        Manager m = null;
        LocalDate birth = null;
        if (rs.next()) {
            if (rs.getDate(6) != null) {
                birth = rs.getDate(6).toLocalDate();
            }
            m = new Manager(
                rs.getInt(1), // managerCode
                rs.getString(2), // id
                rs.getString(3), // passwd
                rs.getString(4), // name
                rs.getString(5), // Gender
                birth,
                rs.getString(7), // email
                rs.getString(8) // phone
            );
        }
        return m;
    }
}
