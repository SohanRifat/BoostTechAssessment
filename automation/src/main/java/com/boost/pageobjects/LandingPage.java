package com.boost.pageobjects;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.boost.base.BaseTest;

public class LandingPage extends BaseTest{
    WebDriver driver;

    public LandingPage(WebDriver driver){
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "#menu-1-6e4221c li")
    List<WebElement> tabsElementsRetrieved; 

    @FindBy(linkText = "Company")
    WebElement companyLink;

    @FindBy(linkText = "Get Started")
    WebElement getStartedButton; 


    public void goToUrl(){
        driver.get("https://www.boostb2b.com/");
    }

    public List<WebElement> getTabList(){
        return tabsElementsRetrieved;
    }

    public void clickCompanyTab(){
        Actions actions = new Actions(driver);
        actions.doubleClick(companyLink).perform();
    }

    public void clickGetStartedButton(){
        getStartedButton.click();
    }
}
