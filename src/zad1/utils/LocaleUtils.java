package zad1.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Currency;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocaleUtils {

    public static ObservableList<String> getAvailableCountries(){
        Predicate<Locale> shouldHaveIso3Code = loc -> {
            try{
                loc.getISO3Country();
            }
            catch (MissingResourceException e){
                return false;
            }
            return true;
        };

        return Stream.of(Locale.getAvailableLocales())
                .filter(shouldHaveIso3Code)
                .filter(loc -> !loc.getDisplayCountry(Locale.ENGLISH).isEmpty())
                .map(loc -> loc.getDisplayCountry(Locale.ENGLISH))
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    public static ObservableList<String> getAvailableCurrencyCodes(){

        Predicate<Locale> shouldHaveCurrencySymbol = loc -> {
            try{
                Currency.getInstance(loc);
            }catch (IllegalArgumentException e){
                return false;
            }

            return true;
        };

        return Stream.of(Locale.getAvailableLocales())
                .filter(shouldHaveCurrencySymbol)
                .map(loc -> Currency.getInstance(loc).getCurrencyCode())
                .collect(Collectors.toSet())
                .stream()
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

    }

}
