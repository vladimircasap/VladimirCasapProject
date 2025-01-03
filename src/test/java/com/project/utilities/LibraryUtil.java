package com.project.utilities;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LibraryUtil {


    public static String getToken(String email, String password) {

        JsonPath jp = RestAssured.given().log().uri()
            .accept(ContentType.JSON)
            .contentType(ContentType.URLENC) // Datatype that I am sending to API
            .formParam("email", email)
            .formParam("password", password)
            .when().post("/login")
            .then().statusCode(200)
            .extract().jsonPath();

        String accessToken = jp.getString("token");

        //return "Bearer " + accessToken;
        return  accessToken;

    }

    public static String generateTokenByRole(String role) {

        Map<String, String> roleCredentials = returnCredentials(role);
        String email = roleCredentials.get("email");
        String password = roleCredentials.get("password");

        return getToken(email, password);

    }


    public static Map<String, String> returnCredentials(String role) {
        String email = "";
        String password = "";

        switch (role) {
            case "librarian":
                email = ConfigurationReader.getProperty("librarian_username");
                password = ConfigurationReader.getProperty("librarian_password");
                break;

            case "user":
                email = ConfigurationReader.getProperty("user_email");
                password = ConfigurationReader.getProperty("user_password");
                break;

            default:

                throw new RuntimeException("Invalid Role Entry :\n>> " + role + " <<");
        }
        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return credentials;

    }

    public static Map<String, Object> createRandomBook() {

        Faker faker = new Faker();
        Map<String, Object> bookMap = new LinkedHashMap<>();
        String name = faker.book().title();
        String isbn = String.format("%d", faker.number().randomNumber(12, true));
        Integer year = faker.number().numberBetween(1900, 2024);
        String author = faker.book().author();
        Integer book_category_id = 3;
        String description = faker.lorem().paragraph(2);

        bookMap.put("name", name);
        bookMap.put("isbn", isbn);
        bookMap.put("year", year);
        bookMap.put("author", author);
        bookMap.put("book_category_id", book_category_id);
        bookMap.put("description", description);


        return bookMap;
    }

    public static Map<String, Object> createRandomUser() {

        Faker faker = new Faker();
        Map<String, Object> userMap = new LinkedHashMap<>();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String full_name = firstName + " " + lastName;
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
        String password = generatePassword(faker, 12);
        Integer user_group_id = 3;
        String status = "ACTIVE";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        Date randomStartDate = faker.date().past(500, TimeUnit.DAYS);
        String startDate = dateFormat.format(randomStartDate);
        Date randomEndDate = faker.date().future(500, TimeUnit.DAYS);
        String endDate = dateFormat.format(randomEndDate);
        String address = faker.address().fullAddress();

        userMap.put("full_name", full_name);
        userMap.put("email", email);
        userMap.put("password", password);
        userMap.put("user_group_id", user_group_id);
        userMap.put("status", status);
        userMap.put("start_date", startDate);
        userMap.put("end_date", endDate);
        userMap.put("address", address);

        return userMap;
    }

    public static String generatePassword(Faker faker, int length) {
        // You can use Faker to generate random letters, digits, and special characters
        StringBuilder password = new StringBuilder(length);

        Random random = new Random();

        // Define character sets for password generation
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String specialCharacters = "!@#$%^&*()_-+=<>?";

        // Combine all character sets into one
        String allCharacters = upperCaseLetters + lowerCaseLetters + digits + specialCharacters;

        // Add random characters to the password until the desired length is reached
        for (int i = 0; i < length; i++) {
            // Pick a random character from the combined set
            char randomChar = allCharacters.charAt(random.nextInt(allCharacters.length()));
            password.append(randomChar);
        }

        // Optionally, you can add logic here to ensure the password has at least one upper case, one digit, and one special char
        return password.toString();
    }



}







