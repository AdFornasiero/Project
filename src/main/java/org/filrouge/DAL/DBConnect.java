package org.filrouge.DAL;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static String url = "jdbc:mysql://localhost:3306/ecom?serverTimezone=UTC";
    private static String user = "root";
    private static String pwd = "";


    public static Connection connect() {
        try {
            Connection db = DriverManager.getConnection(url, user, pwd);
            return db;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    public static Connection connect(String url, String user, String pwd) {
        try {
            Connection db = DriverManager.getConnection(url, user, pwd);
            return db;
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
