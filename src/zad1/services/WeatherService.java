package zad1.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public class WeatherService {

    private Map<String, String> countryIsoCodeByCountryName;
    private String countryName;

    public WeatherService(String countryName) {
        this.countryName = countryName;
        this.countryIsoCodeByCountryName = initCountryIsoCodeByCountryName();
    }

    private Map<String, String> initCountryIsoCodeByCountryName() {
        return Stream.of(Locale.getAvailableLocales())
                .filter(this::shouldHaveIso3CountryCode)
                .collect(toMap(loc -> loc.getDisplayCountry(Locale.US), Locale::getISO3Country, (loc1, loc2) -> loc1));
    }

    private boolean shouldHaveIso3CountryCode(Locale locale) {
        try {
            locale.getISO3Country();
        } catch (MissingResourceException e) {
            return false;
        }
        return true;
    }

    public String getWeather(String city) {
        return getJsonWeatherString(city).orElse("");
    }

    private Optional<String> getJsonWeatherString(String city) {
        try {
            return Optional.ofNullable(loadWeatherByCountryAndCity(city));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private String loadWeatherByCountryAndCity(String city) throws IOException {
        String countryIsoCode = countryIsoCodeByCountryName.get(countryName);

        String rawUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "," + 0 + "," + countryIsoCode + "&appid=a458b924214be5a205df44b71027e22d";
        URL url = new URL(rawUrl);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

        return bufferedReader.lines().collect(joining("\n"));
    }

}
