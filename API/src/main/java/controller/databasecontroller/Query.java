package controller.databasecontroller;

import model.TempEvent;

import java.sql.SQLException;
import java.util.List;

public interface Query {
    List<TempEvent> getTempEvents(String table) throws SQLException;
    TempEvent getTempEvent(String table, String id) throws SQLException;
}
