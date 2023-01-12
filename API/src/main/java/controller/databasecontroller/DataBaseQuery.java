package controller.databasecontroller;

import model.TempEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DataBaseQuery implements StandardQuery {
    private final DataBaseConnector dataBaseConnection;

    public DataBaseQuery(DataBaseConnector dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
    }

    @Override
    public LocalDate getFirstId(String table) throws SQLException {
        String sql = String.format("SELECT date FROM %s", table);

        Statement statement = dataBaseConnection.getConn().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(rs.getString(1), formatter);
    }

    @Override
    public List<TempEvent> getTempEvents(String name) throws SQLException {
        String sql = String.format("SELECT * FROM %s", name);

        Statement statement = dataBaseConnection.getConn().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        List<TempEvent> events = new ArrayList<>();

        while (rs.next()) {
            events.add(getTempEvent(rs));
        }

        return events;
    }

    @Override
    public TempEvent getTempEvent(String table, String id) throws SQLException {
        String sql = String.format("SELECT * FROM %s WHERE date = %s", table, id);
        Statement statement = dataBaseConnection.getConn().createStatement();
        ResultSet rs = statement.executeQuery(sql);

        while (rs.next()) {
           return getTempEvent(rs);
        }

        return null;
    }

    private TempEvent getTempEvent(ResultSet rs) throws SQLException {
        TempEvent event = new TempEvent();

        event.setInstant(
                getDateFromInstantString(rs.getString(1))
                + "T" + rs.getString(2) + "Z");

        event.setStation(rs.getString(3));
        event.setUbi(rs.getString(4));
        event.setTemperature(rs.getDouble(5));
        return event;
    }

    private String getDateFromInstantString(String d) {
        return d.substring(0, 4) + "-"
                + d.substring(4, 6) + "-"
                + d.substring(6, 8);
    }
}
