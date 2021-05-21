package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.Lobby;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class JoinLobbyRessourceTest extends TestCase{

	private LobbyDB lobbyDB = LobbyDB.getInstance();
	private UserDB userDB = UserDB.getInstance();
	private UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	
	@Test
	public void validLobby_Test() {
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId());

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();

        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Lobby ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
		
        
		given().formParam("username", joiner4.getUsername()).formParam("token", token)
			.when().post("/join-lobby/join")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void lobby_WrongToken_Test() {
		
		given().formParam("username", "username_test").formParam("token", "ABCD")
			.when().post("/join-lobby/join")
			.then()
				.statusCode(404);
	}
	
	
	@Test
	public void lobby_EmptyUsername_Test() {
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", "").formParam("token", token)
			.when().post("/join-lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_BlankUsername_Test() {
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", " ").formParam("token", token)
			.when().post("/join-lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_WrongUsername_Test() {
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", "username_test%").formParam("token", token)
			.when().post("/join-lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_LobbyFull_Test() {
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
        
        // ----------- Init des joiners ----------- // 
        
        User joiner1 = new User("joiner1");
        User joiner2 = new User("joiner2");
        User joiner3 = new User("joiner3");
        User joiner4 = new User("joiner4");

        // ----------- Ajout des Joiners à la Lobby ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);
        
        
        // ----------- Teste le fait que la Lobby est bien pleine ----------- // 
        
		given().formParam("username", "joiner5").formParam("token", token)
			.when().post("/join-lobby/join")
			.then()
				.statusCode(401);
	}
}
