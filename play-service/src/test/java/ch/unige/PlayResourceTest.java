package ch.unige;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import ch.unige.dao.GameDB;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PlayResourceTest {

    @Test
    public void PlayTest() {
        GameDB gameDB = GameDB.getInstance();

        //* TOKEN = ABCDEF, add scores
        given().when().get("/play/send/ABCDEF/0/48").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/0/25").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/3/50").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/3/55").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/3/66").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/3/82").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/5/72").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/0/64").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/ABCDEF/19/38").then().statusCode(200).body(is("Success"));

        //* Token = FEDCBA, add scores
        given().when().get("/play/send/FEDCBA/1/66").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/2/25").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/3/34").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/4/96").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/5/26").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/6/82").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/7/38").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/8/61").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/9/39").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/10/75").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/11/32").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/12/100").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/13/74").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/14/52").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/15/14").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/16/92").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/17/27").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/18/65").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/FEDCBA/19/34").then().statusCode(200).body(is("Success"));

        //* Token = QWERTZ, add scores
        given().when().get("/play/send/QWERTZ/15/48").then().statusCode(200).body(is("Success"));
        given().when().get("/play/send/QWERTZ/9/25").then().statusCode(200).body(is("Success"));

        //* Getresult TOKEN = ABCDEF, le film indexé 5 gagne
        given().when().get("/play/getScore/ABCDEF").then().statusCode(200).body(is("5"));

        //* Getresult TOKEN = FEDCBA, le film indexé 12 gagne
        given().when().get("/play/getScore/FEDCBA").then().statusCode(200).body(is("12"));

        //* Getresult TOKEN = QWERTZ, le film indexé 15 gagne
        given().when().get("/play/getScore/QWERTZ").then().statusCode(200).body(is("15"));
    }
}