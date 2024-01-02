package application.utils;

import java.sql.Connection;

public class TestDriver {
    public static void main(String[] args) {
            Connection conn = DBUtil.getConnection();
            System.out.println(conn);
    }
}
