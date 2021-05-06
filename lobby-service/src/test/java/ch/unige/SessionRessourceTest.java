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
	public void validLobby_Test() {
		
		// ----------- Init des DB ----------- // 
		
		SessionsDB sessionsDB = SessionsDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Session ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();

        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Session ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
		
        
		given().formParam("username", joiner4.getUsername()).formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void lobby_WrongToken_Test() {
		
		given().formParam("username", "username_test").formParam("token", "ABCD")
			.when().post("/session/join")
			.then()
				.statusCode(404);
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

    	Session newSession = new Session(creator_user.getUser_id()); 
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

    	Session newSession = new Session(creator_user.getUser_id()); 
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

    	Session newSession = new Session(creator_user.getUser_id()); 
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

    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();
        
        
        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Session ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);
        
        
        // ----------- Teste le fait que la Session est bien pleine ----------- // 
        
		given().formParam("username", "joiner5").formParam("token", token)
			.when().post("/session/join")
			.then()
				.statusCode(401);
	}
	
	
	@Test
	public void startGame_Test() {
		// ----------- Init des DB ----------- // 
		
		SessionsDB sessionsDB = SessionsDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Session ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();
		
        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Session ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        
        // ----------- Setting them to Ready ----------- // 
        
        userInLobbyJoiner1.setReadyOrNot(1);
        userInLobbyJoiner2.setReadyOrNot(1);
        userInLobbyJoiner3.setReadyOrNot(1);
        userInLobbyJoiner4.setReadyOrNot(1);
        
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);

        
        
        // ----------- Test que toutes les conditions sont réunis pour pouvoir lancer la game ----------- //
        given().pathParam("token", token)
			.when().get("/session/{token}/start")
			.then()
				.statusCode(200);
	}
	
	
	@Test
	public void startGame_NotEveryoneReady_Test() {
		// ----------- Init des DB ----------- // 
		
		SessionsDB sessionsDB = SessionsDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Session ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);

        UserInLobby userInLobby = new UserInLobby(creator_user, newSession.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newSession.getToken();
		
        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Session ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        
        // ----------- Setting them to Ready ----------- // 
        
        userInLobbyJoiner1.setReadyOrNot(1);
        userInLobbyJoiner2.setReadyOrNot(1);
        userInLobbyJoiner3.setReadyOrNot(1);
        userInLobbyJoiner4.setReadyOrNot(0);
        
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);

        
        
        // ----------- Test que toutes les conditions sont réunis pour pouvoir lancer la game ----------- //
        given().pathParam("token", token)
			.when().get("/session/{token}/start")
			.then()
				.statusCode(409);
	}

}
