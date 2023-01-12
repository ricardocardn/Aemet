package controller;

import controller.databasecontroller.DataBaseDDL;
import controller.databasecontroller.DataBaseQuery;
import controller.dataextractor.DataExtractor;
import controller.dataextractor.DataLakeDataExtractor;
import model.TempEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private final DataExtractor dataExtractor;
    private final DataBaseDDL dataBaseDDL;
    private final DataBaseQuery dataBaseQuery;
    private final static DateTimeFormatter fr = DateTimeFormatter.ofPattern("yyyyMMdd");

    public Controller(DataExtractor dataExtractor, DataBaseDDL dataBaseDDL, DataBaseQuery dataBaseQuery) {
        this.dataExtractor = dataExtractor;
        this.dataBaseDDL = dataBaseDDL;
        this.dataBaseQuery = dataBaseQuery;
    }

    public void run(LocalDate date) {
        DataExtractor dataExtractor = new DataLakeDataExtractor();
        dataBaseDDL.createTables();

        Controller controller = new Controller(dataExtractor, dataBaseDDL, dataBaseQuery);

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updateMaxTemp(date);
                updateMinTemp(date);
            }
        };

        timer.schedule(timerTask, 60*1000, 60*60*1000);
    }

    private void updateMaxTemp(LocalDate date) {
        //dataBaseDDL.createTables();
        try {
            List<TempEvent> tempEvents = dataExtractor.getEvents(date);
            TempEvent maxTempEvent = dataBaseQuery.getTempEvent("MaxTemp", date.format(fr));
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

    private void updateMinTemp(LocalDate date) {
        try {
            List<TempEvent> tempEvents = dataExtractor.getEvents(date);
            TempEvent minTempEvent = dataBaseQuery.getTempEvent("MinTemp", date.format(fr));
            TempEvent lastMinTempEvent = getLastMinTempEvent(tempEvents);

            if (minTempEvent == null) {
                dataBaseDDL.insertIntoTable("MinTemp", lastMinTempEvent);
                printTemp("Min", lastMinTempEvent);

            } else if (minTempEvent.getTemperature() > lastMinTempEvent.getTemperature()) {
                dataBaseDDL.insertIntoTable("MinTemp", lastMinTempEvent);
                printTemp("Min", lastMinTempEvent);

            } else {
                printTemp("Min", minTempEvent);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
        System.out.printf(
                "%s. temperature on the island this day (%s) until now: %s (%s)%n",
                name,
                LocalDate.now(),
                tempEvent.getTemperature(),
                tempEvent.getUbi());
    }
}
