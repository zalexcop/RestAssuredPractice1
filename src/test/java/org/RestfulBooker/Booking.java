package org.RestfulBooker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Booking {

    private static final String BASE_URL = "https://restful-booker.herokuapp.com/booking";

    // Метод для генерации параметров для теста
    public static Object[][] getBookingFilters() {
        return new Object[][]{
                {"John", null, null, null}, // фильтр по firstname
                {null, "Doe", null, null}, // фильтр по lastname
                {null, null, "2025-01-01", null}, // фильтр по checkin
                {null, null, null, "2025-01-01"}, // фильтр по checkout
                {"John", "Doe", "2025-01-01", "2025-02-01"}, // комбинированный фильтр
                {null, null, null, null}
        };
    } // без фильтров


        @ParameterizedTest
        @MethodSource("getBookingFilters")
        public void testGetBookingIds(String firstName, String lastName, String checkin, String checkout) {
        Map<String,String> params = new HashMap<>();

            Map<String, String> filters = new HashMap<>();
            filters.put("firstname", firstName);
            filters.put("lastname", lastName);
            filters.put("checkin", checkin);
            filters.put("checkout", checkout);

            for(Map.Entry<String,String> filter : filters.entrySet()){
                if(filter.getValue() != null){
                    params.put(filter.getKey(), filter.getValue());
                }
            }

        Response response = RestAssured.given()
                        .params(params)
                                .when()
                                        .get(BASE_URL);
        assertEquals(200, response.statusCode(), "Ожидаемый код: 200");
        assertNotNull(response);
    }

    @Test
    public void testGetBookingId() {
        String id = "1";
        Response response = RestAssured.given()
                .when()
                .get(BASE_URL + "/" + id);
        assertEquals(200, response.statusCode());
        assertNotNull(response);
    }
    @Test
    public void testCreateBooking() {
        String requestBody = "{\n" +
                "  \"firstname\": \"Jim\",\n" +
                "  \"lastname\": \"Brown\",\n" +
                "  \"totalprice\": 111,\n" +
                "  \"depositpaid\": true,\n" +
                "  \"bookingdates\": {\n" +
                "    \"checkin\": \"2018-01-01\",\n" +
                "    \"checkout\": \"2019-01-01\"\n" +
                "  },\n" +
                "  \"additionalneeds\": \"Breakfast\"\n" +
                "}";
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(BASE_URL);
        assertEquals(200, response.statusCode());
        String responseBody = response.getBody().asString();
        assert responseBody.contains("Jim");
        assert responseBody.contains("Brown");
    }

}
