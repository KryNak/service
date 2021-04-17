package zad1.model;

import com.google.gson.Gson;

import java.util.Optional;

public class WeatherDTO {

    private WeatherDTO(){}

    public static class Coord {

        public Double lon;
        public Double lat;

    }

    public static class Main{

        public Double temp;
        public Double feelsLike;
        public Double tempMin;
        public Double tempMax;
        public Double pressure;
        public Double humidity;

    }

    public static class Clouds{

        public Double all;

    }

    public static class Wind{

        public Double speed;
        public Double deg;

    }

    public static class Sys{

        public Integer type;
        public Long id;
        public String country;
        public Double sunrise;
        public Double sunset;

    }

    public Coord coord;
    public Object[] weather;
    public String base;
    public Main main;
    public Double visibility;
    public Wind wind;
    public Clouds clouds;
    public Double dt;
    public Sys sys;
    public Double timezone;
    public Long id;
    public String name;
    public Double cod;

    public String getFormattedWeatherInfo(){
        return String
                .format(
                        "" +
                                "Localization: %s\n" +
                                "Temperature: %.0f \u00B0C \n" +
                                "Humidity: %.2f\n" +
                                "Pressure: %.2f\n" +
                                "Wind Speed: %.2f",
                        name, main.temp - 273.15, main.humidity, main.pressure, wind.speed
                );
    }

    public static Optional<WeatherDTO> getInstanceFromJson(String json){
        if(json.isEmpty()) return Optional.empty();
        return Optional.of(new Gson().fromJson(json, WeatherDTO.class));
    }

}
