package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.User;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

@QuarkusTest
public class UserTest extends TestCase{
	
	// Comment tester les BadREquest pour les pseudo empty et blank ?
	// Solution Tester la fonction validUsername (voir plus bas)
	
	@Test
	public void createNormalUserTest() {
		User user_normal = new User("test");
		assertEquals("test", user_normal.getUsername());
	}
	
	@Test
	public void setUsernameTest() {
		User user = new User("test");
		user.setUsername("testing");
		assertEquals("testing", user.getUsername());
	}
	
	@Test
	public void setUserIDTest() {
		User user = new User("test");
		user.setUserID(11);
		assertEquals(11, user.getUserId());
	}

// Shouldn't be in this class but in UserInDB..
//	@Test
//	public void setReadyStatusTest() {
//		User user = new User("test");
//		user.setReady_statut(true);
//		assertEquals(true, user.getReady_status());
//	}
//	
	@Test  	
	public void validUsername_EmptyTest() {
		User user = new User("test");
		assertEquals(false, user.validUsername(""));
	}
	
	@Test  	
	public void validUsername_BlankTest() {
		User user = new User("test");
		assertEquals(false, user.validUsername(" "));
	}

}
