package controller;

import datalake.DataLake;
import model.WeatherEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Controller {
    private EventCollector eventCollector;

    public Controller() {
        this.eventCollector = new EventCollector();;
    }

    public void run(DataLake dataLake, String apiKey) {
        DateTimeFormatter fr = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileName = "datalake/" + LocalDate.now().format(fr) + ".events";

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String fileName = LocalDate.now().format(fr) + ".events";

                try {
                    List<WeatherEvent> weatherEvents = EventCollector.getCurrentEvents(apiKey);
                    dataLake.store(weatherEvents, LocalDate.now());

                } catch (IOException e) {
                    printError(fileName, e);
                }
            }
        };
        timer.schedule(timerTask, 0, 60*60*1000);
    }

    private void printError(String fileName, IOException e) {
        System.out.println("Trouble found at collecting data for file: " + fileName);
        System.out.println("\tException message: " + e.getMessage());
    }
}
