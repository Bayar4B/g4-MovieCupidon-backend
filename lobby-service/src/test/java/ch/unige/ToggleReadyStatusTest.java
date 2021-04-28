package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.dao.SessionsDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.Session;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ToggleReadyStatusTest extends TestCase{
	
	@Test
	public void failTogglingReadyStatus() {
		// ----------- Init des DB ----------- // 
		SessionsDB sessionsDB = SessionsDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	 // ----------- Création d'une Session ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Session newSession = new Session(creator_user.getUserId()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();
        String url = "/session/"+ token +"/USERID/toggleready";
        
		given().when().post(url)
			.then().statusCode(401);
	}
	
	@Test
	public void togglingReadyStatus() {
		// ----------- Init des DB ----------- // 
		SessionsDB sessionsDB = SessionsDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	 // ----------- Création d'une Session ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Session newSession = new Session(creator_user.getUserId()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();
		

        String url = "/session/" + token + "/ownerUsername/toggleready" ;
		given().when().post(url)
			.then().statusCode(200);
	}
	

}
