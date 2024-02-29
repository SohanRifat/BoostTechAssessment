package boost.testcases;

import java.io.IOException;

import org.junit.Test;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import boost.base.TestBase;
import boost.pages.LandingPage;

public class LandingPageTest extends TestBase{
    
    @BeforeMethod
    public void setUp(){
        // initialization();
    }


    @Test
    public void saveHeaderTabs() throws IOException{
        LandingPage landingPage = new LandingPage();
        landingPage.saveHeadersToCSV();
    }

    @AfterMethod
    public void tearDown(){
        driver.quit();
    }
}
