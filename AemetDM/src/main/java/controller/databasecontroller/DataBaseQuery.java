package controller.databasecontroller;

import com.google.gson.Gson;
import model.TempEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataBaseQuery implements Query {
    private DataBaseConnection dataBaseConnection;

    public DataBaseQuery(DataBaseConnection dataBaseConnection) {
        this.dataBaseConnection = dataBaseConnection;
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

        event.setStation(rs.getString(2));
        event.setUbi(rs.getString(3));
        event.setTemperature(rs.getDouble(4));
        return event;
    }
}
