package controller.command;

import controller.databasecontroller.StandardQuery;
import model.TempEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommandSQLite implements Command {
    private final StandardQuery dataBaseQuery;
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public CommandSQLite(StandardQuery dataBaseQuery) {
        this.dataBaseQuery = dataBaseQuery;
    }

    @Override
    public List<TempEvent> minTemperatures(LocalDate from, LocalDate to) throws SQLException {
        return getTemperatures("MinTemp", from, to);
    }

    @Override
    public List<TempEvent> maxTemperatures(LocalDate from, LocalDate to) throws SQLException {
        return getTemperatures("MaxTemp", from, to);
    }

    private List<TempEvent> getTemperatures(String table, LocalDate from, LocalDate to) throws SQLException {
        List<TempEvent> tempEvents = new ArrayList<>();
        if (from == null && to == null) {
            tempEvents.add(getMinTemp(table, LocalDate.now()));

        } else {
            LocalDate current = (from != null) ?
                    LocalDate.of(from.getYear(), from.getMonth(), from.getDayOfMonth()) :
                    (LocalDate) dataBaseQuery.getFirstId(table);

            LocalDate date = (to != null) ? to : LocalDate.now();
            while (minorOrEqualDate(current, date)) {
                TempEvent tempEvent = getMinTemp(table, current);
                if (tempEvent != null) tempEvents.add(getMinTemp(table, current));
                current = current.plusDays(1);
            }
        }
        return tempEvents;
    }

    private TempEvent getMinTemp(String Table, LocalDate now) throws SQLException {
        return dataBaseQuery.getTempEvent(Table, now.format(FORMAT));
    }

    private boolean equalDates(LocalDate d1, LocalDate d2) {
        return d1.getDayOfYear() == d2.getDayOfYear() && d1.getYear() == d2.getYear();
    }

    private boolean minorOrEqualDate(LocalDate d1, LocalDate d2) {
        return equalDates(d1, d2) || d1.getYear() < d2.getYear() || minorDateThan(d1, d2);
    }

    private boolean minorDateThan(LocalDate d1, LocalDate d2) {
        return d1.getYear() == d2.getYear() && d1.getDayOfYear() < d2.getDayOfYear();
    }
}
