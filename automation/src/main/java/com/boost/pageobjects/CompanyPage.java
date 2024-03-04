package com.boost.pageobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.boost.base.BaseTest;

public class CompanyPage extends BaseTest {

    WebDriver driver;

    public CompanyPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "[data-id=\"6c595425\"] section h2")
    List<WebElement> countriesInGlobalFootPrint;

    public List<String> getCountriesInGlobalFootprint() {
        List<String> actualCountries = countriesInGlobalFootPrint
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
        return actualCountries;
    }

    static List<String> expectedCountries = List.of(
            "Australia", "Austria", "Belgium", "Brazil", "Bulgaria", "Canada",
            "Colombia", "Croatia", "Cyprus", "Czech Republic", "Denmark",
            "Ecuador", "Estonia", "Finland", "France", "Germany", "Greece",
            "Hong Kong", "Hungary", "Iceland", "India", "Ireland", "Italy",
            "Latvia", "Liechtenstein", "Lithuania", "Luxembourg", "Malaysia",
            "Malta", "Mexico", "Netherlands", "New Zealand", "Norway", "Poland",
            "Portugal", "Puerto Rico", "Romania", "Singapore", "Slovakia",
            "Slovenia", "Spain", "Sweden", "Switzerland", "Turkiye", "U.S.A",
            "United Arab Emirates", "United Kingdom");

    public void validateCountries(List<String> actualCountries) {
        Set<String> expectedCountrySet = new HashSet<>(expectedCountries.stream()
                .map(String::toUpperCase) 
                .map(String::trim) 
                .collect(Collectors.toList()));
        Set<String> actualCountrySet = new HashSet<>(actualCountries.stream()
                .map(String::toUpperCase) 
                .map(String::trim) 
                .collect(Collectors.toList()));

        Set<String> missingCountries = new HashSet<>(expectedCountrySet);
        missingCountries.removeIf(country -> actualCountrySet.contains(country));

        Set<String> unexpectedCountries = new HashSet<>(actualCountrySet);
        unexpectedCountries.removeIf(country -> expectedCountrySet.contains(country));

        missingCountries.removeAll(unexpectedCountries);

        if (!missingCountries.isEmpty()) {
            System.out.println("Missing expected countries: " + String.join(", ", missingCountries));
        }

        if (!unexpectedCountries.isEmpty()) {
            System.out.println("Unexpected countries found: " + String.join(", ", unexpectedCountries));
        }
    }

}
