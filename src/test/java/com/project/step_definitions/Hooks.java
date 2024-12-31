package com.project.step_definitions;

import com.project.utilities.BrowserUtils;
import com.project.utilities.ConfigurationReader;
import com.project.utilities.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;


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

    //@Before
    public void setupMethod(){
        System.out.println("--------- Before Method is Executed ---------");
    }


    //@After
    public void teardownMethod(Scenario scenario){

        if (scenario.isFailed()) {

            byte[] screenshot = ((TakesScreenshot) Driver.getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());

        }


        BrowserUtils.sleep(2);
        Driver.closeDriver();
        System.out.println("--------- After Method is Executed ---------");

    }

}
