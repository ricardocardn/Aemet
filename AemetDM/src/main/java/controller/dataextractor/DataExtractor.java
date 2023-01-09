package controller.dataextractor;

import model.TempEvent;

import java.time.LocalDate;
import java.util.List;

public interface DataExtractor {
    public List<TempEvent> getEvents(LocalDate date) throws Exception;
}
