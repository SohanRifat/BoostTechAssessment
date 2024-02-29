package boost.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import boost.util.TestUtil;

public class TestBase {
    
    // public static WebDriver driver;

    // public static void initialization(){
    //     // String browserName = prop.getProperty("brower");
	// 	String browserName = "chrome";
    //     if(browserName.equals("chrome")){
    //         System.setProperty("webdriver.chrome.driver", "/Users/sohan/Downloads/chromedriver-mac-x64/chromedriver");
    //         driver = new ChromeDriver(); 
    //     }

    //     driver.manage().window().maximize();
    //     driver.manage().deleteAllCookies();
    //     driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestUtil.PAGE_LOAD_TIMEOUT));
    //     driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestUtil.IMPLICIT_WAIT));
    //     driver.get("https://www.boostb2b.com/");
    // }

	protected WebDriver driver;  // Use protected for visibility in subclasses

    public TestBase() {
        String browserName = "chrome";
        if (browserName.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "/Users/sohan/Downloads/chromedriver-mac-x64/chromedriver");
            driver = new ChromeDriver();
        }

        // Browser setup code
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestUtil.PAGE_LOAD_TIMEOUT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestUtil.IMPLICIT_WAIT));

        // Navigate to base URL
        driver.get("https://www.boostb2b.com/");
    }
}