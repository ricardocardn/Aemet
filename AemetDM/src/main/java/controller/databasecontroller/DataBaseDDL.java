package controller.databasecontroller;

import com.google.gson.Gson;
import model.TempEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseDDL implements DDLdb {
    private static DataBaseDDL dataBaseDDL;
    private DataBaseConnection dataBaseConnection;
    private int id = 0;

    public DataBaseDDL(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
        this.dataBaseDDL = this;
    }

    public static DataBaseDDL getInstance() throws SQLException {
        if (dataBaseDDL != null) {
            return dataBaseDDL;
        } else {
            throw new SQLException();
        }
    }

    @Override
    public void createTables() {
        createTemperatureTables();
    }

    private void createTemperatureTables() {
        String dbPath = "jdbc:sqlite:" + dataBaseConnection.getDbPath();
        String sql = "CREATE TABLE IF NOT EXISTS %s (\n"
                + "date TEXT PRIMARY KEY,"
                + "station TEXT,"
                + "ubi TEXT NOT NULL,"
                + "temp NUMBER NOT NULL"
                + ");";


        if (dataBaseConnection.getConn() != null) {
            try {
                Statement stmt = dataBaseConnection.getConn().createStatement();
                stmt.execute(String.format(sql, "MaxTemp"));

                stmt = dataBaseConnection.getConn().createStatement();
                stmt.execute(String.format(sql, "MinTemp"));

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public void insertIntoTable(String name, TempEvent tempEvent) throws SQLException {
        String date = tempEvent.getInstant().substring(0,4) + tempEvent.getInstant().substring(5,7) + tempEvent.getInstant().substring(8,10);
        if (tempEvent != null)
            dataBaseConnection.getConn().createStatement().execute(String.format(
                "DELETE FROM %s WHERE date = %s",
                name,
                date
            ));

        String sql = String.format("INSERT INTO %s(station,ubi,date,temp) VALUES (?,?,?,?)", name);

        try {
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setString(1, tempEvent.getStation());
            pstmt.setString(2, tempEvent.getUbi());
            pstmt.setString(3, date);
            pstmt.setDouble(4, tempEvent.getTemperature());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
