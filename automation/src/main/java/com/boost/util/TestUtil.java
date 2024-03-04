package com.boost.util;

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
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    public static void writeToCsvFile(List<WebElement> elements, String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("HeaderListings\n"); 
            elements.stream()
                    .forEach(element -> {
                        String text = element.findElement(By.cssSelector("a")).getText().trim();
                        try {
                            writer.write(text + "\n"); 
                        } catch (IOException e) {
                            e.printStackTrace(); 
                        }
                    });
        }
    }

    public static void takeScreenshot(WebDriver driver) throws IOException {
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshotFile, new File("BoostB2B_Headers.png"));
    }

    public static String formatDate() throws IOException, ParseException, InterruptedException {
        String urlString = "https://timeapi.io/api/Time/current/zone?timeZone=Europe/Amsterdam";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(urlString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Error: API request failed with status code: " + response.statusCode());
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readValue(response.body(), JsonNode.class);

        String dateString = rootNode.at("/date").asText(); // Adjust path based on JSON structure
        SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");
        Date date = inputFormat.parse(dateString);
        String formattedDate = outputFormat.format(date);

        return formattedDate;
    }
}
