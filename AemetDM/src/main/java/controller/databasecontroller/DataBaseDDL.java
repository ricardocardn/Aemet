package controller.databasecontroller;

import model.TempEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseDDL implements StandardDDL {
    private static DataBaseDDL dataBaseDDL;
    private final DataBaseConnector dataBaseConnection;
    private final int id = 0;

    public DataBaseDDL(DataBaseConnector dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
        dataBaseDDL = this;
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
                + "time TEXT NOT NULL,"
                + "station TEXT NOT NULL,"
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
        String date = getDateFromInstantString(tempEvent);
        if (tempEvent != null)
            dataBaseConnection.getConn().createStatement().execute(String.format(
                "DELETE FROM %s WHERE date = %s",
                name,
                date
            ));

        String sql = String.format("INSERT INTO %s(date,time,station,ubi,temp) VALUES (?,?,?,?,?)", name);

        try {
            PreparedStatement pstmt = dataBaseConnection.getConn().prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, tempEvent.getInstant().substring(11,19));
            pstmt.setString(3, tempEvent.getStation());
            pstmt.setString(4, tempEvent.getUbi());
            pstmt.setDouble(5, tempEvent.getTemperature());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getDateFromInstantString(TempEvent tempEvent) {
        return tempEvent.getInstant().substring(0, 4)
                + tempEvent.getInstant().substring(5, 7)
                + tempEvent.getInstant().substring(8, 10);
    }
}
