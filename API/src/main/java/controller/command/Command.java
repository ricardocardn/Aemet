package controller.command;

import model.TempEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface Command {
    List<TempEvent> minTemperatures(LocalDate from, LocalDate to) throws SQLException;
    List<TempEvent> maxTemperatures(LocalDate from, LocalDate to) throws SQLException;
}
