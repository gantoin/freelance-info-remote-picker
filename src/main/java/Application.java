import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Application {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        String initUrl = "https://www.freelance-info.fr/missions?keywords=Java%2C+Spring%2C+remote";
        driver.get(initUrl);
        String h2PageText = driver.findElement(By.cssSelector("#mission1")).findElement(By.tagName("h2")).getText();
        String nbJobs = h2PageText.substring(h2PageText.lastIndexOf(" de ") + 4);
        for (int page = 2; page < Integer.parseInt(nbJobs) / 10; page++) {
            List<WebElement> jobs = driver.findElements(By.id("offre"));
            jobs.forEach(job -> {
                String linkToJob = job.findElement(By.cssSelector("#titre-mission > a")).getAttribute("href");
                WebDriver driverJob = new ChromeDriver();
                driverJob.manage().window().maximize();
                driverJob.get(linkToJob);
                String infos = driverJob.findElement(By.cssSelector("#left-col > div.row > div.col-8.left")).getText();
                String remote;
                try {
                    remote = infos.substring(infos.lastIndexOf("Télétravail : ") + 14, infos.lastIndexOf("%"));
                } catch (StringIndexOutOfBoundsException exception) {
                    remote = "0";
                }
                if (Integer.parseInt(remote.trim()) == 100) {
                    CSVService.writeToCsv(driverJob.getCurrentUrl());
                }
                driverJob.close();
            });
            driver.navigate().to(initUrl + "&page=" + page);
        }
    }

}
