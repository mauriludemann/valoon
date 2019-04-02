package valoon.utils;

import io.restassured.response.Response;
import valoon.dto.CreateOrderDto;
import valoon.dto.TakeOrderDto;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class RequestHandler {

    public static Response post(String url, Integer expectedStatus) {
       return  given().contentType("application/json").
                        when().post(url)
                        .then().
                        contentType(JSON).
                        statusCode(expectedStatus).log().ifValidationFails().extract().response();

    }

    public static Response postWithCreateOrderBody(String url, CreateOrderDto orderDTO) {
        return given().contentType("application/json").
                body(orderDTO).log().body().
                when().post(url)
                .then().
                        contentType(JSON).
                        statusCode(201).log().ifValidationFails().extract().response();
    }

    public static Response postWithTakeOrderBody(String url, Integer expectedStatus, TakeOrderDto orderDTO) {
        return given().contentType("application/json").
                body(orderDTO).log().body().
                when().post(url)
                .then().
                        contentType(JSON).
                        statusCode(expectedStatus).log().ifValidationFails().extract().response();
    }

}
