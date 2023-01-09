package controller.datalake;

import model.WeatherEvent;

import java.time.LocalDate;
import java.util.List;

public interface DataLake {
    void store(List<WeatherEvent> weatherEvents, LocalDate date);
}
