package ch.unige.model;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class SampleSelectionRessourceTest {

    @Test
    public void getValidSample(){
        String json = "{\n" +
                "\t\"genreList\" : [\"action\", \"horror\", \"animation\"],\n" +
                "\t\"rangeYear\" : [1900,2021]\n" +
                "}";

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().post("/sample-selection/get-sample")
                .then()
                .statusCode(200);

        // Si on veut print le contenu de la réponse
//        Response res = given().contentType(MediaType.APPLICATION_JSON).body(json)
//                .when().post("/sample-selection/get-sample");
//        res.getBody().print();
    }

    @Test
    public void getNonValidSampleWrongGender(){
        String json = "{\n" +
                "\t\"genreList\" : [\"ThisGenreDoesntExist\", \"horror\"],\n" +
                "\t\"rangeYear\" : [1900,2021]\n" +
                "}";

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().post("/sample-selection/get-sample")
                .then()
                .statusCode(400);
    }

    @Test
    void getNonValidSampleTooManyGendersGiven() {
        String json = "{\n" +
                "\t\"genreList\" : [\"action\", \"horror\", \"animation\",\"drama\"],\n" +
                "\t\"rangeYear\" : [1900,2021]\n" +
                "}";

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().post("/sample-selection/get-sample")
                .then()
                .statusCode(400);
    }

    @Test
    void getNonValidSampleNotAnyGenderGiven() {
        String json = "{\n" +
                "\t\"genreList\" : [],\n" +
                "\t\"rangeYear\" : [1900,2021]\n" +
                "}";

        given().contentType(MediaType.APPLICATION_JSON).body(json)
                .when().post("/sample-selection/get-sample")
                .then()
                .statusCode(400);
    }

    @Test
    void helloWorld() {
        given().when().get("/sample-selection/helloworld")
                .then()
                .statusCode(200);
    }
}
