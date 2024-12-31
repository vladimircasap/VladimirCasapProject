package com.project.utilities;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class LibraryTestbase {
    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI="https://library2.cydeo.com/rest/v1";
    }


}
