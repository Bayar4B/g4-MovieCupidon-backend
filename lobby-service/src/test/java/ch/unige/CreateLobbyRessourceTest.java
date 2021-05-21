package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.Lobby;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CreateLobbyRessourceTest extends TestCase{
	
	@Test
	public void createValidLobbyTest() {
		given().formParam("username", "username_test")
			.when().post("/create-lobby/new-lobby")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void createNonValidLobby_EmptyUsername_Test() {
		given().formParam("username", "")
			.when().post("/create-lobby/new-lobby")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void createNonValidLobby_BlankUsername_Test() {
		given().formParam("username", " ")
			.when().post("/create-lobby/new-lobby")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void createNonValidLobby_WrongUsername_Test() {
		given().formParam("username", "username_test%")
			.when().post("/create-lobby/new-lobby")
			.then()
				.statusCode(400);
	}
	

}
