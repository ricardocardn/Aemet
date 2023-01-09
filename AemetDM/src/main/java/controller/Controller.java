package controller;

import controller.databasecontroller.DataBaseConnection;
import controller.databasecontroller.DataBaseDDL;
import controller.databasecontroller.DataBaseQuery;
import controller.dataextractor.DataExtractor;
import controller.dataextractor.DataLakeDataExtractor;
import model.TempEvent;

import java.time.LocalDate;
import java.util.List;

public class Controller {
    private DataExtractor dataExtractor;
    private DataBaseDDL dataBaseDDL;
    private DataBaseQuery dataBaseQuery;

    public Controller(DataExtractor dataExtractor, DataBaseDDL dataBaseDDL, DataBaseQuery dataBaseQuery) {
        this.dataExtractor = dataExtractor;
        this.dataBaseDDL = dataBaseDDL;
        this.dataBaseQuery = dataBaseQuery;
    }

    public void run(String date) {
        updateMaxTemp(date);
        updateMinTemp(date);
    }

    private void updateMaxTemp(String date) {
        dataBaseDDL.createTables();
        try {
            List<TempEvent> tempEvents = dataExtractor.getEvents(LocalDate.now());
            TempEvent maxTempEvent = dataBaseQuery.getTempEvent("MaxTemp", date);
            TempEvent lastMaxTempEvent = getLastMaxTempEvent(tempEvents);

            if (maxTempEvent == null) {
                dataBaseDDL.insertIntoTable("MaxTemp", lastMaxTempEvent);
                printTemp("Max", lastMaxTempEvent);

            } else if (maxTempEvent.getTemperature() < lastMaxTempEvent.getTemperature()) {
                dataBaseDDL.insertIntoTable("MaxTemp", lastMaxTempEvent);
                printTemp("Max", lastMaxTempEvent);

            } else {
                printTemp("Max", maxTempEvent);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private void updateMinTemp(String date) {
        try {
            List<TempEvent> tempEvents = dataExtractor.getEvents(LocalDate.now());
            TempEvent minTempEvent = dataBaseQuery.getTempEvent("MinTemp", date);
            TempEvent lastMinTempEvent = getLastMinTempEvent(tempEvents);

            if (minTempEvent == null) {
                dataBaseDDL.insertIntoTable("MinTemp", lastMinTempEvent);
                //printTemp("Min", lastMinTempEvent);

            } else if (minTempEvent.getTemperature() > lastMinTempEvent.getTemperature()) {
                dataBaseDDL.insertIntoTable("MinTemp", lastMinTempEvent);
                printTemp("Min", lastMinTempEvent);

            } else {
                printTemp("Min", minTempEvent);
            }
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    private TempEvent getLastMaxTempEvent(List<TempEvent> tempEvents) {
        return tempEvents.stream()
                .reduce((t1, t2) -> {
                    if (t1.getTemperature() > t2.getTemperature())
                        return t1;
                    else
                        return t2;
                }).orElse(null);
    }

    private TempEvent getLastMinTempEvent(List<TempEvent> tempEvents) {
        return tempEvents.stream()
                .reduce((t1, t2) -> {
                    if (t1.getTemperature() > t2.getTemperature())
                        return t2;
                    else
                        return t1;
                }).orElse(null);
    }

    private void printTemp(String name, TempEvent tempEvent) {
        System.out.println(
                String.format(
                        "%s. temperature on the island this day (%s) until now: %s (%s)",
                        name,
                        LocalDate.now(),
                        tempEvent.getTemperature(),
                        tempEvent.getUbi())
        );
    }
}
