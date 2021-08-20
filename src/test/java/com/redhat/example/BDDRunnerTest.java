package com.redhat.example;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:test-application-scenarios.feature",
        plugin = { "json:target/junit-result.json", "pretty","html:target/cucumber-reports.html" } )
public class BDDRunnerTest {

}