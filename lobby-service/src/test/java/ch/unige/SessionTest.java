package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.Session;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

@QuarkusTest
public class SessionTest extends TestCase{
	
	@Test
	public void createSessionTest() {
		Session session = new Session(11);
		assertEquals(11, session.getCreator_user_id());
		assertEquals("Preparation - Lobby", session.getSession_status());
	}
	
	@Test
	public void setTokenIDTest() {
		Session session = new Session(11);
		session.setToken("TestToken");
		assertEquals("TestToken", session.getToken());
	}

	@Test
	public void setSessionStatutTest() {
		Session session = new Session(11);
		session.setSession_status("In Game");
		assertEquals("In Game", session.getSession_status());
	}
	
	@Test
	public void setChatIDTest() {
		Session session = new Session(11);
		session.setChat_id(11);
		assertEquals(11, session.getChat_id());
	}
	
	@Test
	public void toStringTest() {
		Session session = new Session(11);
		session.setToken("ABCD");
		assertEquals("UC: 11 token: ABCD", session.toString() );
	}
	
}
