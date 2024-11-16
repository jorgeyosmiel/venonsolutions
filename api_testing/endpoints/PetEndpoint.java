package petstore.endpoints;

import io.restassured.response.Response;
import petstore.utils.Config;
import petstore.models.Pet;
import io.restassured.http.ContentType;

import java.util.List;

import static io.restassured.RestAssured.given;

public class PetEndpoint {

    public static Response getPet(int petId) {
        return given()
                .baseUri(Config.BASE_URL)
                .when()
                .get("/pet/" + petId);
    }

    public static Response getPetsByStatus(String status) {
        return given()
                .baseUri(Config.BASE_URL)
                .queryParam("status", status) // Concatenar tags con comas
                .when()
                .get("/pet/findByStatus");
    }

    public static Response getPetsByTags(List<String> tags) {
        return given()
                .baseUri(Config.BASE_URL)
                .queryParam("tags", String.join(",", tags)) // Concatenar tags con comas
                .when()
                .get("/pet/findByTags");
    }

    public static Response createPet(Pet pet) {
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .post("/pet");
    }

    public static Response updatePet(Pet pet){
        return given()
                .baseUri(Config.BASE_URL)
                .contentType(ContentType.JSON)
                .body(pet)
                .when()
                .put("/pet");
    }

    public static void deletePet(int petId) {
        given()
                .baseUri(Config.BASE_URL)
                .when()
                .delete("/pet/" + petId)
                .then()
                .statusCode(200);
    }

    // Otros métodos (updatePet, etc.)
}

