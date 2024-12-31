package com.project.step_definitions;

import com.project.utilities.LibraryUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static org.hamcrest.core.Every.everyItem;
import static org.hamcrest.core.IsNull.notNullValue;

public class retrieveAllUsers{

    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;

    @Given("I logged Library api as a {string}")
    public void i_logged_library_api_as_a(String role) {
        String token = LibraryUtil.generateTokenByRole(role);  //Generate token for given role
        givenPart.header("x-library-token", token);           //Add Authorization header with token

    }


    @Given("Accept header is {string}")
    public void accept_header_is(String accept_header) {
    givenPart.accept(accept_header); //Set the Accept header

    }


    @When("I send GET request to {string} endpoint")
    public void i_send_get_request_to_endpoint(String endpoint) {
    response = givenPart.when().get(endpoint); //Send GET request to the endpoint
    jp = response.jsonPath();    //Parse the JSON response
    thenPart = response.then();  //Prepare to validate the response
    response.prettyPrint();      //Print the response for debugging
    }


    @Then("status code should be {int}")
    public void status_code_should_be(int expectedStatusCode) {
    thenPart.statusCode(expectedStatusCode);  //Validate the status code
    }


    @Then("Response Content type is {string}")
    public void response_content_type_is(String expectedContentType) {
    thenPart.contentType(expectedContentType);  //Validate the content type
    }


    @Then("{string} field should not be null")
    public void field_should_not_be_null(String path) {
    thenPart.body(path, everyItem(notNullValue()));  //Assert that the field is not null
    }
}
