package com.project.step_definitions;

import com.project.utilities.ConfigurationReader;
import com.project.utilities.DB_Util;
import com.project.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


public class Hooks {

    @Before()
    public void setBaseURI() {
        System.out.println("----- Setting BaseURI");
        RestAssured.baseURI= ConfigurationReader.getProperty("base_url");
    }
    @After()
    public void endScenario(Scenario scenario){
        System.out.println("Test Result for "+scenario.getName()+" "+scenario.getStatus());
    }

    @Before("@db")
    public void dbHook() {
        System.out.println("----- creating database connection");
        DB_Util.createConnection();
    }

    @After("@db")
    public void afterDbHook() {
        System.out.println("----- closing database connection");
        DB_Util.destroy();

    }

    @Before("@ui")
    public void setUp() {
        Driver.get().get(ConfigurationReader.getProperty("library_url"));
        Driver.get().manage().window().maximize();
        Driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @After("@ui")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) Driver.get()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png","screenshot");
        }
        Driver.closeDriver();
    }

}
