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
public class SessionRessourceTest extends TestCase{
	
	@Test
	public void validLobbyTest() {
		
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
		
		given().formParam("username", "username_test").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void lobby_WrongToken_Test() {
		
		given().formParam("username", "username_test").formParam("token", "ABCD")
			.when().post("/session/join")
			.then()
				.statusCode(400);
	}
	
	
	@Test
	public void lobby_EmptyUsername_Test() {
		
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
        
		given().formParam("username", "").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_BlankUsername_Test() {
		
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
        
		given().formParam("username", " ").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_WrongUsername_Test() {
		
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
        
		given().formParam("username", "username_test%").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_SessionFull_Test() {
		
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
        
        
        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");
        User joiner5 = new User("joiner5");

        // ----------- Ajout des Joiners à la Session ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        
        
        // ----------- Teste le fait que la Session est bien pleine ----------- // 
        
		given().formParam("username", "joiner5").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(401);
	}

}
