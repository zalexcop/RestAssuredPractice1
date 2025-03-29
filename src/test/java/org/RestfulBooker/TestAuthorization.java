package org.RestfulBooker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestAuthorization{
    @Test
    public void testAuthorization(){
        String username = "admin";
        String password = "password123";
        String authUrl = "https://restful-booker.herokuapp.com/auth";

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post(authUrl);

        assertEquals(200, response.statusCode(),"Ожидаемый статус код: 200");

        String token = response.jsonPath().getString("token");
        assertNotNull(token,"Токен не может быть null");
    }
}