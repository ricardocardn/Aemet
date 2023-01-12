package controller.databasecontroller;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataBaseConnector {
    private final String dbPath;
    private Connection conn;

    /*
     * Class Constructor
     * @param dbPath: String with the local url of the database
     */
    public DataBaseConnector(String dbPath) {
        this.dbPath = dbPath;
        connect();
    }

    /*
     * Method that establishes a connection with the database
     * given as class params
     */
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

        return;

    }

    public Connection getConn() {
        return conn;
    }

    public String getDbPath() {
        return dbPath;
    }

    /*
     * Method that closed the opened connection with the database
     * given as class params
     */
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