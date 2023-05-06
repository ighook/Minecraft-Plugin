package org.rocheserver.NotUsed;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection conn = null;

    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // JDBC 드라이버 클래스 이름
            String url = "jdbc:mysql://ighook.cafe24.com:3306/ighook?useSSL=false&serverTimezone=UTC"; // 데이터베이스 URL
            String username = "ighook"; // 사용자 이름
            String password = "wlsqja4292!"; // 패스워드
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
