package zad1.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import zad1.Service;
import zad1.components.AutoCompleteComboBox;
import zad1.components.CustomDialog;
import zad1.model.WeatherDTO;
import zad1.utils.LocaleUtils;

import java.util.Objects;
import java.util.Optional;

public class MainStageController {

    @FXML WebView webView;
    @FXML TextArea weatherTextArea;
    @FXML Button popupButton;
    @FXML TextArea currencyRateTextArea;
    @FXML TextArea nbpTextArea;

    private final String LINK_PREFIX = "https://en.wikipedia.org/wiki/";

    private Service service;

    private Dialog<ButtonType> dialog;
    private ComboBox<String> countryComboBox;
    private ComboBox<String> currencyCodesComboBox;
    private TextField cityTextField;

    public MainStageController() {
        this.cityTextField = new TextField();

        this.countryComboBox = new ComboBox<>(LocaleUtils.getAvailableCountries());
        AutoCompleteComboBox.attachAutoCompleteFunctionalityTo(countryComboBox);

        this.currencyCodesComboBox = new ComboBox<>(LocaleUtils.getAvailableCurrencyCodes());
        AutoCompleteComboBox.attachAutoCompleteFunctionalityTo(currencyCodesComboBox);

        this.dialog = CustomDialog.createFromComponents(countryComboBox, currencyCodesComboBox, cityTextField);
    }

    @FXML void initialize() {
        popupButton.setOnAction(buttonType -> popupAction());
        setDefaultSettings();
    }

    private void popupAction(){
        dialog.showAndWait().filter(buttonType -> buttonType.equals(ButtonType.OK)).ifPresent(this::updateView);
    }

    private void updateView(ButtonType ignore){
        service = new Service(countryComboBox.getSelectionModel().getSelectedItem());

        String selectedCity = cityTextField.getText();
        String selectedCurrencyCode = currencyCodesComboBox.getSelectionModel().getSelectedItem();
        setServiceRepresentationFields(selectedCity, selectedCurrencyCode);

        if(weatherTextArea.getText().trim().isEmpty()) {
            final String ERROR = "https://en.wikipedia.org/wiki/HTTP_404";
            webView.getEngine().load(ERROR);
            return;
        }

        webView.getEngine().load(LINK_PREFIX + cityTextField.getText());
    }

    private void setDefaultSettings(){
        final String DEFAULT_COUNTRY = "Poland";
        final String DEFAULT_CITY = "Warsaw";
        final String DEFAULT_CURRENCY_CODE = "PLN";

        this.service = new Service(DEFAULT_COUNTRY);
        setServiceRepresentationFields(DEFAULT_CITY, DEFAULT_CURRENCY_CODE);

        currencyCodesComboBox.getSelectionModel().select(DEFAULT_CURRENCY_CODE);
        countryComboBox.getSelectionModel().select(DEFAULT_COUNTRY);
        cityTextField.setText(DEFAULT_CITY);

        webView.getEngine().load(LINK_PREFIX + DEFAULT_CITY);
    }

    private void setServiceRepresentationFields(String city, String currencyCode){
        String weatherJson = service.getWeather(city);
        Optional<WeatherDTO> weatherDTO = WeatherDTO.getInstanceFromJson(weatherJson);
        weatherTextArea.setText(weatherDTO.map(WeatherDTO::getFormattedWeatherInfo).orElse(""));

        String formattedNbpRate = String.format("%.2f", service.getNBPRate());
        final String NBP_RATE_PREFIX = "PLN Rate: ";
        nbpTextArea.setText(NBP_RATE_PREFIX + formattedNbpRate);

        String formattedCurrencyRate = String.format("%.2f", service.getRateFor(currencyCode));
        final String CURRENCY_RATE_PREFIX = "Currency Rate: ";
        currencyRateTextArea.setText(CURRENCY_RATE_PREFIX + formattedCurrencyRate);
    }

}
