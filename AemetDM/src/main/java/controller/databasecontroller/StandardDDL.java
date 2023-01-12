package controller.databasecontroller;

import model.TempEvent;

import java.sql.SQLException;

public interface StandardDDL {
    void createTables();
    void insertIntoTable(String name, TempEvent tempEvent) throws SQLException;
}
