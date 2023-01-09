package controller.databasecontroller;

import model.TempEvent;

import java.sql.SQLException;

public interface DDLdb {
    public void createTables();
    public void insertIntoTable(String name, TempEvent tempEvent) throws SQLException;
}
