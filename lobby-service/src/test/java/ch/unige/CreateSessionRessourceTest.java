package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.Session;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class CreateSessionRessourceTest extends TestCase{
	
	@Test
	public void createSessionTest() {
		given().formParam("username", "username_test")
			.when().post("/create-session/new-session")
			.then()
				.statusCode(200);
	}

}
