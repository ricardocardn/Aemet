package model;

public class TempEvent {
    private double temperature;
    private String station;
    private String ubi;
    private String instant;

    public TempEvent(double temperature, String station, String ubi, String date, String instant) {
        this.temperature = temperature;
        this.station = station;
        this.ubi = ubi;
        this.instant = instant;
    }

    public TempEvent() {}

    public double getTemperature() {
        return temperature;
    }

    public String getStation() {
        return station;
    }

    public String getUbi() {
        return ubi;
    }

    public String getInstant() {
        return instant;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public void setUbi(String ubi) {
        this.ubi = ubi;
    }

    public void setInstant(String instant) {
        this.instant = instant;
    }

    @Override
    public String toString() {
        return "TempEvent{" +
                "temperature=" + temperature +
                ", station='" + station + '\'' +
                ", ubi='" + ubi + '\'' +
                ", instant=" + instant +
                '}';
    }
}
