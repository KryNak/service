package zad1.services;

import com.google.gson.Gson;
import zad1.model.CurrencyRateDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class CurrencyRateService {

    private String countryName;
    private String countryCurrencyCode;

    public CurrencyRateService(String countryName) {
        this.countryName = countryName;
        this.countryCurrencyCode = initCountryCurrencyCode();
    }

    private String initCountryCurrencyCode(){
        return Stream.of(Locale.getAvailableLocales())
                .filter(loc -> loc.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName))
                .map(loc -> Currency.getInstance(loc).getCurrencyCode())
                .findFirst()
                .orElse("");
    }

    public Double getRateFor(String currencyCode) {
        try {
            final String OLD_API_ADDRESS = "https://api.exchangeratesapi.io/latest?base=%s&symbols=%s";
            final String NEW_API_ADDRESS = "https://api.exchangerate.host/latest?base=%s&symbols=%s";

            String rawUrl = String.format(NEW_API_ADDRESS, currencyCode, countryCurrencyCode);
            URL url = new URL(rawUrl);
            BufferedReader currencyRateReader = new BufferedReader(new InputStreamReader(url.openStream()));

            String requestResult = currencyRateReader.lines().collect(joining("\n"));

            Gson parser = new Gson();
            CurrencyRateDTO currencyRateDTO = parser.fromJson(requestResult, CurrencyRateDTO.class);

            return currencyRateDTO.rates.get(countryCurrencyCode).doubleValue();
        } catch (IOException e) {
            return 1.0;
        }
    }


}
