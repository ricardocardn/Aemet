package model;

public class WeatherEvent {
    private String instant;
    private String station;
    private String ubi;
    private Double temperature;
    private Double humidity;
    private Double latitude;
    private Double longitude;

    public WeatherEvent(String instant, String station, String ubi, Double temperature, Double humidity, Double latitude, Double longitude) {
        this.instant = instant;
        this.station = station;
        this.ubi = ubi;
        this.temperature = temperature;
        this.humidity = humidity;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public WeatherEvent() {}

    public String getInstant() {
        return instant;
    }

    public String getStation() {
        return station;
    }

    public Double getTemperature() {
        return temperature;
    }

    public Double getHumidity() {
        return humidity;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
