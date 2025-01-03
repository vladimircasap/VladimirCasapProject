package com.project.step_definitions;

import com.mysql.cj.protocol.Resultset;
import com.project.pages.BookPage;
import com.project.pages.SignInPage;
import com.project.pages.TopNavigationBar;
import com.project.utilities.BrowserUtils;
import com.project.utilities.DB_Util;
import com.project.utilities.LibraryUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class LibraryStepDef{

    RequestSpecification givenPart = RestAssured.given().log().all();
    Response response;
    JsonPath jp;
    ValidatableResponse thenPart;
    Map<String, Object> API_book;


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

        String value = jp.getString(path);
        Assert.assertNotNull(value);
    }

    @Given("Request Content Type header is {string}")
    public void request_content_type_header_is(String contentTypeHeader) {
        givenPart.header("Content-Type", contentTypeHeader);

    }
    Map<String, Object> randomDataMap;
    @Given("I create a random {string} as request body")
    public void i_create_a_random_as_request_body(String dataType) {

        switch (dataType) {
            case "book":
                // Generates random data for the request body
                randomDataMap = LibraryUtil.createRandomBook();
                API_book=randomDataMap;
                System.out.println("Request Data: " + randomDataMap);
                break;
            case "user":
                randomDataMap = LibraryUtil.createRandomUser();
                System.out.println("Request Data: " + randomDataMap);
                break;

            default:
                throw new RuntimeException("Wrong data type is provide");

        }


        givenPart.formParams(randomDataMap); // Adds the generated data to the request
    }



    @When("I send POST request to {string} endpoint")
    public void i_send_post_request_to_endpoint(String endpoint) {

        response = givenPart.when().post(endpoint);
        jp = response.jsonPath();
        thenPart = response.then();

        response.prettyPrint();

    }
    @Then("the field value for {string} path should be equal to {string}")
    public void the_field_value_for_path_should_be_equal_to(String path, String expectedValue) {
        // Get value from response
        String actualValue = jp.getString(path);

        Assert.assertEquals(expectedValue, actualValue);
    }

    String expectedPath;
    @Given("Path param is {string}")
    public void path_param_is(String path) {
        expectedPath = path;
        givenPart.pathParam("id" , path);
    }
    @Then("{string} field should be same with path param")
    public void field_should_be_same_with_path_param(String string) {
        String actualPath = response.body().path("id");
        Assert.assertEquals(expectedPath, actualPath);
    }
    @Then("following fields should not be null")
    public void following_fields_should_not_be_null(List<String> fields) {
        for (String field : fields) {
            Object  value = jp.getString(field);
            Assert.assertNotNull(value);

        }
    }





    @Given("I logged in Library UI as {string}")
    public void i_logged_in_library_ui_as(String role) {
        Map<String, String> credentials = LibraryUtil.returnCredentials(role);
        System.out.println("credentials = " + credentials);

        SignInPage signInPage = new SignInPage();
        signInPage.login(credentials.get("email"), credentials.get("password"));



    }
    @Given("I navigate to {string} page")
    public void i_navigate_to_page(String string) {
        TopNavigationBar topNavigationBar = new TopNavigationBar();
        topNavigationBar.books.click();
    }
    @Then("UI, Database and API created book information must match")
    public void ui_database_and_api_created_book_information_must_match() throws InterruptedException {
        //------GETTING BOOK FROM DB------
        //run query
        DB_Util.runQuery("select * from books where id="+ jp.getString("book_id"));
        Map<String, String> DB_Book = DB_Util.getRowMap(1);
        System.out.println("DB_Book = " + DB_Book);


        // ---- GETTING BOOK FROM UI-----
        BookPage bookPage = new BookPage();

        Select dropdown  = new Select(bookPage.categoryDropDown);
        dropdown.selectByValue("3");
        BrowserUtils.waitFor(1);
        bookPage.searchField.sendKeys(randomDataMap.get("name").toString());
        BrowserUtils.waitFor(1);
        bookPage.searchField.sendKeys(Keys.ENTER);
        BrowserUtils.waitFor(1);

        Map<String, String> UI_Book = new HashMap<>();
        UI_Book.put("isbn" , bookPage.bookISBN.getText());
        UI_Book.put("name", bookPage.bookName.getText());
        UI_Book.put("author", bookPage.bookAuthor.getText());
        UI_Book.put("category", bookPage.bookCategory.getText());
        UI_Book.put("year", bookPage.bookYear.getText());

        //Assertion Book field UI vs DB vs API
        Assert.assertEquals(UI_Book.get("isbn"), DB_Book.get("isbn"), randomDataMap.get("isbn"));
        Assert.assertEquals(UI_Book.get("name"), DB_Book.get("name"), randomDataMap.get("name"));
        Assert.assertEquals(UI_Book.get("author"), DB_Book.get("author"), randomDataMap.get("author"));
        Assert.assertEquals(UI_Book.get("category"), DB_Book.get("category"), randomDataMap.get("category"));
        Assert.assertEquals(UI_Book.get("year"), DB_Book.get("year"), randomDataMap.get("year").toString());

    }

    @Then("created user information should match with Database")
    public void created_user_information_should_match_with_database() {
        //------GETTING USER FROM DB------
        DB_Util.runQuery("select * from users where id="+ jp.getString("user_id"));
        Map<String, String> DB_User = DB_Util.getRowMap(1);
        System.out.println("DB_User = " + DB_User);

        //Assertion field from DB vs API
        Assert.assertEquals(DB_User.get("full_name"),  randomDataMap.get("full_name"));
        Assert.assertEquals(DB_User.get("email"), randomDataMap.get("email"));
        Assert.assertEquals(DB_User.get("status"), randomDataMap.get("status"));
        Assert.assertEquals(DB_User.get("start_date"), randomDataMap.get("start_date"));
        Assert.assertEquals(DB_User.get("end_date"), randomDataMap.get("end_date"));
        Assert.assertEquals(DB_User.get("address"), randomDataMap.get("address"));
    }




    @Then("created user should be able to login Library UI")
    public void created_user_should_be_able_to_login_library_ui() {
        SignInPage signInPage = new SignInPage();
        signInPage.login(randomDataMap.get("email").toString(), randomDataMap.get("password").toString());
        BrowserUtils.waitFor(3);
    }
    @Then("created user name should appear in Dashboard Page")
    public void created_user_name_should_appear_in_dashboard_page() {

        TopNavigationBar topNavigationBar = new TopNavigationBar();
        String actualUserName = randomDataMap.get("full_name").toString();
        String expectedUserName = topNavigationBar.userName.getText();
        Assert.assertEquals(actualUserName, expectedUserName);
    }
    String token;
    @Given("I logged Library api with credentials {string} and {string}")
    public void i_logged_library_api_with_credentials_and(String email, String password) {
         token = LibraryUtil.getToken(email, password);  //Generate token for given user
                 //Add Authorization header with token
    }
    @Given("I send token information as request body")
    public void i_send_token_information_as_request_body() {

        givenPart.formParam("token", token);

    }






}

