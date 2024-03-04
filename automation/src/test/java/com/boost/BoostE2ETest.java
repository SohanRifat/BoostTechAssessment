package com.boost;
import java.util.List;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.boost.pageobjects.CompanyPage;
import com.boost.pageobjects.GetStartedPage;
import com.boost.pageobjects.LandingPage;
import com.boost.util.TestUtil;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BoostE2ETest {

    public static void main(String[] args) throws Exception {

        //Background steps for test
        WebDriver driver = WebDriverManager.chromedriver().create();
        LandingPage landingpage = new LandingPage(driver);
        landingpage.setup();
        landingpage.goToUrl();

        //Scenario 1
        List <WebElement> tabsElementsRetrieved = landingpage.getTabList();
        TestUtil.writeToCsvFile(tabsElementsRetrieved, "BoostB2B_HeadingsListing.csv");
        TestUtil.takeScreenshot(driver);

        // Scenario 2
        landingpage.clickCompanyTab();
        CompanyPage companyPage = new CompanyPage(driver);
        List<String> actualCountries = companyPage.getCountriesInGlobalFootprint();
        companyPage.validateCountries(actualCountries);

        // Scenario 3
        landingpage.clickGetStartedButton();
        driver.switchTo().frame(
                driver.findElement(By.xpath("//iframe[@src='https://go.boostb2b.com/l/492571/2020-02-25/nwm8d3']")));
        GetStartedPage getStartedPage = new GetStartedPage(driver);
        getStartedPage.fillForm(
                driver,
                "Sohan",
                "Rifat",
                "souzrat94@gmail.com",
                "QA Engineer",
                "BoostB2B",
                "United States",
                "NY",
                TestUtil.formatDate());

            getStartedPage.checkCheckboxByLabel(driver, "Supplier Enablement services");

        // pause for user to validate reCAPTCHA.
        driver.manage().timeouts().wait(2000);

        getStartedPage.submitFormByButton(driver);

    }
}
