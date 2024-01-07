package application.dao;

import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.dto.User;
import application.dto.UserAtten;
import application.dto.UserChild;
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

    // 수정해야함
    @Override
    public List<UserChild> userManage() {
        List<UserChild> userList = new ArrayList<>();

        String sql = "SELECT * FROM user";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int userCode = rs.getInt(1);
                String userName = rs.getString(2);
                Date userStartDate = rs.getDate(3);
                String userGender = rs.getString(4);
                String phoneHeader = rs.getString(5);
                String phoneMiddle = rs.getString(6);
                String phoneTail = rs.getString(7);
                Boolean userStatus = rs.getBoolean(8);

                LocalDate userReg = userStartDate.toLocalDate();
                LocalDate startDate = null;
                LocalDate endDate = null;

                String phone = phoneHeader+"-"+phoneMiddle+"-"+phoneTail;

                String status = (userStatus == true) ? "활성화" : "비활성화";

                UserChild u = new UserChild(false, userCode, status, userName, userGender, phone, userReg, startDate, endDate);
                userList.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return userList;
    }

    @Override
    public List<UserAtten> userAtten() {
        List<UserAtten> userList = new ArrayList<>();

        String sql = "SELECT a.userCode, a.userName, a.userGender, concat(a.phoneHeader,'-',a.phoneMiddle,'-',a.phoneTail), b.doHealthTime, b.doHealthDate FROM user a inner join attendance b on a.userCode = b.userCode";

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int userCode = rs.getInt(1);
                String userName = rs.getString(2);
                String userGender = rs.getString(3);
                String phone = rs.getString(4);
                Time time = rs.getTime(5);
                Date date = rs.getDate(6);

                LocalDate doHealthDate = date.toLocalDate();
                LocalTime doHealthTime = time.toLocalTime();

                UserAtten u = new UserAtten(false, userCode, userName, userGender, phone, doHealthTime, doHealthDate);
                userList.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, stmt);
        }
        return userList;
    }

    @Override
    public List<String> sendData1(String phoneTail) {
        List<String> sD = new ArrayList<>();
        String sql = "SELECT userName, phoneTail FROM user WHERE phoneTail=?";

        try {
            // 2줄이 뜰 수 도 있으니까 생각
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, phoneTail);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String name = rs.getString(1);
                String tail = rs.getString(2);

                sD.add(name+"|"+tail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
        }
        return sD;
    }

    @Override
    public int getUserByCode(String name, String tail) {
        int id = 0;
        // 이름이랑 뒷 번호가 같은 사람이 존재할 수 도 있겠넹???
        // 하지만 생각하지 않기! 너무 빡세다.
        String sql = "SELECT userCode FROM user WHERE phoneTail=? AND userName=?";

        try {
            // 2줄이 뜰 수 도 있지만 생각하지 않기로 확률상 적다.
            // 이런 경우 예외를 만들어 두기 위해서 회원가입을 할 때 조건을 만들어 주면 좋을 것 같다.
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tail);
            pstmt.setString(2, name);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
        }
        return id;

    }
    
    @Override
    public void attendance(String name, String tail, int code, LocalTime time, LocalDate date) {

        String sql = "INSERT INTO attendance (userTail, userName, doHealthDate, doHealthTime, userCode) VALUES (?, ?, ?, ?, ?)";

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tail);
            pstmt.setString(2, name);
            pstmt.setDate(3, Date.valueOf(date));
            pstmt.setTime(4, Time.valueOf(time));
            pstmt.setInt(5, code);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(pstmt);
        }
    }


}

