package controller.dataextractor;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import model.TempEvent;

import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataLakeDataExtractor implements DataExtractor {
    public List<TempEvent> getEvents(LocalDate date) throws Exception {
        File file = new File(getFileName(date));
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        List<TempEvent> events = new ArrayList<>();
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            events = getEventsFromJson(line, date);
        }

        bufferedReader.close();
        fileReader.close();
        return events;
    }

    private List<TempEvent> getEventsFromJson(String line, LocalDate date) {
        JsonArray eventsArray = new Gson().fromJson(line, JsonArray.class);
        List<TempEvent> events = eventsArray.asList().stream()
                .map(jsonElement -> getEvent(jsonElement))
                .collect(Collectors.toList());
        return events;
    }

    private TempEvent getEvent(JsonElement event) {
        TempEvent tempEvent = new TempEvent();
        tempEvent.setStation(event.getAsJsonObject().get("station").getAsString());
        tempEvent.setTemperature(event.getAsJsonObject().get("temperature").getAsDouble());
        tempEvent.setUbi(event.getAsJsonObject().get("ubi").getAsString());
        tempEvent.setInstant(event.getAsJsonObject().get("instant").getAsString());
        return tempEvent;
    }

    private String getFileName(LocalDate date) {
        DateTimeFormatter fr = DateTimeFormatter.ofPattern("yyyyMMdd");
        return "datalake/" + date.format(fr) + ".events";
    }
}
