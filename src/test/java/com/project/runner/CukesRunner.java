package com.project.runner;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {
                "html:target/cucumber-reports.html" ,
                "rerun:target/rerun.txt",
                "me.jvt.cucumber.report.PrettyReports:target/cucumber"
        } ,
        features = "src/test/resources/features",
        glue = "com/project/step_definitions",
        dryRun = false,
        tags = "@regression"
)
public class CukesRunner {
}
