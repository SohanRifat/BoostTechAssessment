package boost.pages;

import java.io.FileWriter;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import boost.base.TestBase;

public class LandingPage extends TestBase{

    @FindBy(css ="#menu-1-6e4221c")
    WebElement tabElements[];

    public LandingPage(){
        PageFactory.initElements(driver, this);
    }


    public void saveHeadersToCSV() throws IOException {
        String csvContent = "Tab Name\n";

        for (WebElement tabElement : tabElements) {
            String tabName = tabElement.getText().trim(); // Extract and trim text
            csvContent += tabName + "\n";
        }

        try (FileWriter fileWriter = new FileWriter("BoostB2B_HeadersListing.csv")) {
            fileWriter.write(csvContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
