package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.Session;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class CreateSessionRessourceTest extends TestCase{
	
	@Test
	public void createValidSessionTest() {
		given().formParam("username", "username_test")
			.when().post("/create-session/new-session")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void createNonValidSession_EmptyUsername_Test() {
		given().formParam("username", "")
			.when().post("/create-session/new-session")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void createNonValidSession_BlankUsername_Test() {
		given().formParam("username", " ")
			.when().post("/create-session/new-session")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void createNonValidSession_WrongUsername_Test() {
		given().formParam("username", "username_test%")
			.when().post("/create-session/new-session")
			.then()
				.statusCode(400);
	}
	

}
