package controller.databasecontroller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnector {
    private final String dbPath;
    private Connection conn;

    public DataBaseConnector(String dbPath) {
        this.dbPath = dbPath;
        connect();
    }

    public void connect() {
        String dbPath = "jdbc:sqlite:" + this.dbPath;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbPath);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());

        } finally {
            try {
                if (conn != null) {
                    this.conn = conn;
                }

            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    public Connection getConn() {
        return conn;
    }

    public String getDbPath() {
        return dbPath;
    }

    public boolean disconnect() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("DataBase connection closed");
                return true;

            } catch (Exception ex) {
                System.out.print(ex.getMessage());
            }
        }

        return false;
    }
}