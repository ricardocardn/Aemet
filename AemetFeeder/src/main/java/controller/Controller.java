package controller;

import controller.collector.DataCollector;
import controller.collector.EventCollector;
import controller.datalake.DataLake;
import model.WeatherEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class Controller {

    public Controller() {}

    public void run(DataLake dataLake, String apiKey) {
        DateTimeFormatter fr = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileName = "controller/datalake/" + LocalDate.now().format(fr) + ".events";
        DataCollector dataCollector = new EventCollector();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                String fileName = LocalDate.now().format(fr) + ".events";

                try {
                    List<WeatherEvent> weatherEvents = dataCollector.getCurrentObjects(apiKey).stream()
                            .map(object -> (WeatherEvent) object)
                            .collect(Collectors.toList());

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
