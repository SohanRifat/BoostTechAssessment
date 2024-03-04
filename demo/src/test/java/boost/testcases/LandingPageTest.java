package boost.testcases;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import boost.base.TestBase;
import boost.pages.LandingPage;
import io.github.bonigarcia.wdm.WebDriverManager;

public class LandingPageTest extends TestBase {

    // @BeforeMethod
    // public void setUp() {
    //     // initialization();
    // }

    // @Test
    // public void saveHeaderTabs() throws IOException {
    //     LandingPage landingPage = new LandingPage();
    //     landingPage.saveHeadersToCSV();
    // }

    // @AfterMethod
    // public void tearDown() {
    //     driver.quit();
    // }

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.boostb2b.com/");

        List<WebElement> tabsElementsRetrieved = driver.findElements(By.cssSelector("#menu-1-6e4221c"));

        WebElement listName = tabsElementsRetrieved.stream()
                .filter(tabElementInd -> tabElementInd.findElement(By.cssSelector("li")).getText().equals("Boost 100"))
                .findFirst().orElse(null);
        listName.findElement(By.cssSelector(null));
    }
}
