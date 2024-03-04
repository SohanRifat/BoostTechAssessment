package com.boost.base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;

public class BaseTest {

    WebDriver driver;

    public BaseTest(WebDriver driver) {
        this.driver = driver;
    }

    public void setup(){
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
    }
}
