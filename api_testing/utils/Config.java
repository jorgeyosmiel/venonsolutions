package petstore.utils;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Config {
    public static final String BASE_URL = "http://localhost:8080/api/v3";
    public static final String BASE_URL_V2 = "http://localhost:8080/api/v2";

    // Método para obtener la configuración de la solicitud
    public static RequestSpecification getRequestSpecification() {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
    }

}
