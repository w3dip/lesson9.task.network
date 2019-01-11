package ru.sberbank.lesson9.task.network.presentation.model;

public class ForecastItem {
    private String weather;
    private Double temp;

    public ForecastItem(String weather, Double temp) {
        this.weather = weather;
        this.temp = temp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }
}
