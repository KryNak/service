package zad1.services;

import com.sun.deploy.xml.XMLNode;
import com.sun.deploy.xml.XMLParser;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public class NbpRateService {

    private String countryName;
    private String currencySymbol;

    public NbpRateService(String countryName) {
        this.countryName = countryName;
        this.currencySymbol = initCurrencySymbol();
    }

    private String initCurrencySymbol() {
        return Stream.of(Locale.getAvailableLocales())
                .filter(loc -> loc.getDisplayCountry(Locale.ENGLISH).equalsIgnoreCase(countryName))
                .map(loc -> Currency.getInstance(loc).getCurrencyCode())
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public Double getNBPRate() {
        final Double DEFAULT_RATE = 1.0;
        if(currencySymbol.equalsIgnoreCase("pln")) return DEFAULT_RATE;

        return Double.parseDouble(getParsedFromXmlNbpRate());
    }
    private String getParsedFromXmlNbpRate(){
        try {
            String nbpCurrencyRate = searchNbpRateInTables().orElseThrow(NotFound::new);

            XMLParser xmlParser = new XMLParser(nbpCurrencyRate);
            XMLNode node = xmlParser.parse();

            return node.getNested().getNext().getNext().getNext().getNested().getNested().getNext().getNext().getNested().getName();

        }catch (SAXException | NotFound e){
            throw new RuntimeException("No currency in table");
        }
    }

    private Optional<String> searchNbpRateInTables() {
        String nbpCurrencyRate = null;

        for(char i = 'A'; i <= 'C' && (nbpCurrencyRate = searchNbpRateInSingleTable(i)) == null; i++)
            ;

        return Optional.ofNullable(nbpCurrencyRate);
    }

    private String searchNbpRateInSingleTable(char tableIndex) {

        try{

            String rawUrl = String.format("http://api.nbp.pl/api/exchangerates/rates/%s/%s?format=xml", tableIndex, currencySymbol);
            URL url = new URL(rawUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            return reader.lines().collect(joining());

        }catch (IOException e){
            return null;
        }

    }
}
