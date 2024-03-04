package com.boost;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bonigarcia.wdm.WebDriverManager;

public class StandAlone {

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        // WebDriverManager.chromedriver().setup();
        WebDriver driver = WebDriverManager.chromedriver().create();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.boostb2b.com/");
        driver.manage().window().maximize();

        // Scenario 1
        List<WebElement> tabsElementsRetrieved =
        driver.findElements(By.cssSelector("#menu-1-6e4221c li"));
        try {
        writeToCsvFile(tabsElementsRetrieved, "BoostB2B_HeadingsListing.csv");
        } catch (IOException e) {
        e.printStackTrace();
        }

        try {
        takeScreenshot(driver);
        } catch (IOException e) {
        e.printStackTrace();
        }

        // Scenario 2
        Actions actions = new Actions(driver);
        WebElement companyLink = driver.findElement(By.linkText("Company"));
        actions.doubleClick(companyLink).perform();

        List<String> actualCountries =
        driver.findElements(By.cssSelector("[data-id=\"6c595425\"] section h2"))
        .stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());

        System.out.println(actualCountries);

        validateCountries(actualCountries);

        // Scenario 3
        WebElement getStartedButton = driver.findElement(By.linkText("Get Started"));
        getStartedButton.click();

        driver.switchTo().frame(
                driver.findElement(By.xpath("//iframe[@src='https://go.boostb2b.com/l/492571/2020-02-25/nwm8d3']")));

        fillForm(
                driver,
                "sohan",
                "rifat",
                "souzrat94@gmail.com",
                "QA Engineer",
                "BoostB2B",
                "United States",
                "NY",
                formatDate());

        try {
            checkCheckboxByLabel(driver, "Supplier Enablement services");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // // pause for user to validate reCAPTCHA.
        // driver.manage().timeouts().wait(2000);

        submitFormByButton(driver);

    }

    public static void writeToCsvFile(List<WebElement> elements, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("HeaderListings\n"); // Write header row for CSV
            elements.stream()
                    .forEach(element -> {
                        String text = element.findElement(By.cssSelector("a")).getText().trim();
                        try {
                            writer.write(text + "\n"); // Write each element's text to a new line
                        } catch (IOException e) {
                            e.printStackTrace(); // Handle potential writing errors
                        }
                    });
        }
    }

    public static void takeScreenshot(WebDriver driver) throws IOException {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("BoostB2B_Headers.png"));
    }

    public static void validateCountries(List<String> actualCountries) {
        Set<String> expectedCountrySet = new HashSet<>(expectedCountries.stream()
                .map(String::toUpperCase) // Consistent uppercase
                .map(String::trim) // Trim whitespace
                .collect(Collectors.toList()));
        Set<String> actualCountrySet = new HashSet<>(actualCountries.stream()
                .map(String::toUpperCase) // Consistent uppercase
                .map(String::trim) // Trim whitespace
                .collect(Collectors.toList()));

        // Find missing expected countries (considering whitespace and case)
        Set<String> missingCountries = new HashSet<>(expectedCountrySet);
        missingCountries.removeIf(country -> actualCountrySet.contains(country));

        // Find unexpected actual countries (considering whitespace and case)
        Set<String> unexpectedCountries = new HashSet<>(actualCountrySet);
        unexpectedCountries.removeIf(country -> expectedCountrySet.contains(country));

        // Filter out duplicates (if applicable)
        missingCountries.removeAll(unexpectedCountries);

        // Generate failures
        if (!missingCountries.isEmpty()) {
            System.out.println("Missing expected countries: " + String.join(", ", missingCountries));
        }

        if (!unexpectedCountries.isEmpty()) {
            System.out.println("Unexpected countries found: " + String.join(", ", unexpectedCountries));
        }
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

    public static void fillForm(WebDriver driver, String firstName, String lastName, String email, String title,
            String company, String country, String state, String comment) {

        // Fill each field based on its label:
        fillField(driver, "First Name", firstName, false);
        fillField(driver, "Last Name", lastName, false);
        fillField(driver, "Email", email, false);
        fillField(driver, "Title", title, false);
        fillField(driver, "Company", company, false);
        fillField(driver, "Country", country, true); // Indicate dropdown for Country
        fillField(driver, "State (U.S. Only)", state, true);
        fillField(driver, "Comments", comment, false);
    }

    private static void fillField(WebDriver driver, String labelText, String value, boolean isDropdown) {
        WebElement label = driver.findElement(By.xpath("//label[text()='" + labelText + "']"));

        // Handle input fields and dropdowns accordingly:
        if (isDropdown) {
            WebElement dropdown = label.findElement(By.xpath("./following-sibling::select"));
            Select select = new Select(dropdown);
            select.selectByVisibleText(value);
        } else {
            WebElement input = label.findElement(By.xpath("./following-sibling::input"));
            input.sendKeys(value);
        }

        System.out.println(labelText + " field filled with: " + value);
    }

    public static String formatDate() throws IOException, ParseException, InterruptedException {
        // API endpoint URL
        String urlString = "https://timeapi.io/api/Time/current/zone?timeZone=Europe/Amsterdam";

        // Create HTTP request and client
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlString))
                .build();

        // Send request and get response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Check for successful response
        if (response.statusCode() != 200) {
            throw new IOException("Error: API request failed with status code: " + response.statusCode());
        }

        // Parse JSON response
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(response.body(), JsonNode.class);

        // Extract date and convert format
        String dateString = rootNode.at("/date").asText(); // Adjust path based on JSON structure
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
        Date date = inputFormat.parse(dateString);
        String formattedDate = outputFormat.format(date);

        return formattedDate;
    }

    public static void checkCheckboxByLabel(WebDriver driver, String option) throws Exception {
        try {
            // Find the element containing all checkboxes
            WebElement checkboxContainer = driver
                    .findElement(By.cssSelector(".form-field.Interest.pd-checkbox.required"));

            // Find all label elements within the container
            for (WebElement label : checkboxContainer.findElements(By.tagName("label"))) {
                String labelText = label.getText().trim(); // Get label text and trim whitespace
                if (labelText.equals(option)) {
                    // Find the corresponding checkbox using the "for" attribute of the label
                    String checkboxId = label.getAttribute("for");
                    WebElement checkbox = driver.findElement(By.id(checkboxId));
                    if (checkbox != null) {
                        checkbox.click(); // Click the checkbox
                        System.out.println("Checkbox '" + labelText + "' checked successfully.");
                    } else {
                        System.err.println("Checkbox with ID '" + checkboxId + "' not found.");
                    }
                    break; // Exit loop after finding the matching option
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking checkbox: " + e.getMessage());
            throw e; // Re-throw the exception for proper handling
        }
    }

    public static void submitFormByButton(WebDriver driver) {
        // Find the submit button within the form using the buttonSelector
        WebElement submitButton = driver.findElement(By.cssSelector(".submit input"));
        // Scroll the element into view within the frame
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", submitButton);
        // Click the submit button to submit the form
        submitButton.click();
    }
}
