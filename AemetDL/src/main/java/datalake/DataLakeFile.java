package datalake;

import com.google.gson.Gson;
import model.WeatherEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DataLakeFile implements DataLake {

    public DataLakeFile() {}

    @Override
    public void store(List<WeatherEvent> weatherEvents, LocalDate date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
        String fileName = date.format(format);

        try {
            saveOnFile(weatherEvents, fileName);
            printSuccess(date, fileName);
        } catch (Exception e) {
            printError(fileName, e);
        }
    }

    private void saveOnFile(List<WeatherEvent> weatherEvents, String fileName) throws IOException {
        FileOutputStream file = new FileOutputStream(new File("datalake/" + fileName + ".events"));
        file.write((new Gson().toJson(weatherEvents) + '\n').getBytes(StandardCharsets.UTF_8));
        file.close();
    }

    private void printError(String fileName, Exception e) {
        System.out.println("Trouble found at collecting data for file: " + fileName);
        System.out.println("\tException message: " + e.getMessage());
    }

    private void printSuccess(LocalDate date, String fileName) throws IOException {
        String success = String.format(
                "%s: data properly updated to file %s and stored to datalake already. (exact moment: %s)\n",
                date,
                fileName + ".events",
                Instant.now()
        );

        System.out.print(success);
        saveLog(fileName, success);
    }

    private void saveLog(String fileName, String success) throws IOException {
        FileWriter logs = new FileWriter(new File("datalake/logs/" + fileName + ".log"), true);
        logs.write(success);
        logs.close();
    }
}
