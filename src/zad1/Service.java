/**
 * @author Nakielski Krystian S20258
 */

package zad1;

import zad1.services.CurrencyRateService;
import zad1.services.NbpRateService;
import zad1.services.WeatherService;

public class Service {

    private WeatherService weatherService;
    private CurrencyRateService currencyRateService;
    private NbpRateService nbpRateService;

    public Service(String countryName) {
        this.weatherService = new WeatherService(countryName);
        this.currencyRateService = new CurrencyRateService(countryName);
        this.nbpRateService = new NbpRateService(countryName);
    }

    public String getWeather(String city) {
        return weatherService.getWeather(city);
    }

    public Double getRateFor(String currencyCode) {
        return currencyRateService.getRateFor(currencyCode);
    }

    public Double getNBPRate() {
        return nbpRateService.getNBPRate();
    }

}
