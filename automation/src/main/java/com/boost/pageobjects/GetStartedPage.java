package com.boost.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import com.boost.base.BaseTest;

public class GetStartedPage extends BaseTest {

    WebDriver driver;

    public GetStartedPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".form-field.Interest.pd-checkbox.required")
    WebElement checkboxContainer;

    @FindBy(css = ".submit input")
    WebElement submitButton;

    public void fillForm(WebDriver driver, String firstName, String lastName, String email, String title,
            String company, String country, String state, String comment) {
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

    public void checkCheckboxByLabel(WebDriver driver, String option) throws Exception {
        try {
            for (WebElement label : checkboxContainer.findElements(By.tagName("label"))) {
                String labelText = label.getText().trim(); 
                if (labelText.equals(option)) {
                    String checkboxId = label.getAttribute("for");
                    WebElement checkbox = driver.findElement(By.id(checkboxId));
                    if (checkbox != null) {
                        checkbox.click(); 
                        System.out.println("Checkbox '" + labelText + "' checked successfully.");
                    } else {
                        System.err.println("Checkbox with ID '" + checkboxId + "' not found.");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking checkbox: " + e.getMessage());
            throw e;
        }
    }

    public void submitFormByButton(WebDriver driver) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({ block: 'center' });", submitButton);
        submitButton.click();
    }
}
