package controller.collector;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import controller.api.AemetAPIAccessor;
import model.WeatherEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventCollector implements DataCollector {
    private static final String URL = "https://opendata.aemet.es/opendata/api/observacion/convencional/todas";

    public EventCollector() {}

    @Override
    public List<Object> getCurrentObjects(String apiKey) throws IOException {
        JsonArray weatherList = getJsonElements(apiKey);
        return getWeatherEvents(weatherList).stream()
                .filter(event -> event.getInstant().substring(0,4).equals(LocalDate.now().toString().substring(0,4)))
                .filter(event -> event.getInstant().substring(5,7).equals(LocalDate.now().toString().substring(5,7)))
                .filter(event -> event.getInstant().substring(8,10).equals(LocalDate.now().toString().substring(8,10)))
                .collect(Collectors.toList());
    }

    public static String getEventsJson(String apiKey) throws IOException {
        JsonArray weatherList = getJsonElements(apiKey);
        return weatherList.toString();
    }

    private static JsonArray getJsonElements(String apiKey) throws IOException {
        Gson gson = new Gson();
        JsonObject response = gson.fromJson(
                AemetAPIAccessor.getInstance().get(URL, apiKey),
                JsonObject.class);

        return gson.fromJson(
                AemetAPIAccessor.getInstance().get(response.get("datos").getAsString(), apiKey),
                JsonArray.class);
    }

    private static List<WeatherEvent> getWeatherEvents(JsonArray weatherList) {
        List<WeatherEvent> weatherEvents = new ArrayList<>();
        for (JsonElement jsonElement : weatherList) {
            WeatherEvent weatherEvent = getWeatherEvent(jsonElement);
            if (weatherEvent.getLatitude() < 28.4 && weatherEvent.getLatitude() > 27.5 &&
                    weatherEvent.getLongitude() < -15 && weatherEvent.getLongitude() > -16)
                weatherEvents.add(weatherEvent);
        }
        return weatherEvents;
    }

    private static WeatherEvent getWeatherEvent(JsonElement jsonElement) {
        WeatherEvent weatherEvent = new WeatherEvent();
        weatherEvent.setInstant(jsonElement.getAsJsonObject().get("fint").getAsString());
        weatherEvent.setStation(jsonElement.getAsJsonObject().get("idema").getAsString());
        weatherEvent.setUbi(jsonElement.getAsJsonObject().get("ubi").getAsString());
        try {
            weatherEvent.setTemperature(jsonElement.getAsJsonObject().get("ta").getAsDouble());
        } catch (NullPointerException e) {
            weatherEvent.setTemperature(null);
        }

        weatherEvent.setLatitude(jsonElement.getAsJsonObject().get("lat").getAsDouble());
        weatherEvent.setLongitude(jsonElement.getAsJsonObject().get("lon").getAsDouble());
        return weatherEvent;
    }
}
