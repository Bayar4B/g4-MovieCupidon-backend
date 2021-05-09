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


import static io.restassured.RestAssured.given;

@QuarkusTest
public class LobbyRessourceTest extends TestCase{
	
	@Test
	public void validLobby_Test() {
		
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

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
			.when().post("/lobby/join")
			.then()
				.statusCode(200);
	}
	
	@Test
	public void lobby_WrongToken_Test() {
		
		given().formParam("username", "username_test").formParam("token", "ABCD")
			.when().post("/lobby/join")
			.then()
				.statusCode(404);
	}
	
	
	@Test
	public void lobby_EmptyUsername_Test() {
		
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", "").formParam("token", token)
			.when().post("/lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_BlankUsername_Test() {
		
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", " ").formParam("token", token)
			.when().post("/lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_WrongUsername_Test() {
		
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
		given().formParam("username", "username_test%").formParam("token", token)
			.when().post("/lobby/join")
			.then()
				.statusCode(400);
	}
	
	@Test
	public void lobby_LobbyFull_Test() {
		
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

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
			.when().post("/lobby/join")
			.then()
				.statusCode(401);
	}
	
	
	@Test
	public void startGame_Test() {
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

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
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        
        // ----------- Setting them to Ready ----------- // 
        
        userInLobbyJoiner1.setReadyStatut(true);
        userInLobbyJoiner2.setReadyStatut(true);
        userInLobbyJoiner3.setReadyStatut(true);
        userInLobbyJoiner4.setReadyStatut(true);
        
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);

        
        
        // ----------- Test que toutes les conditions sont réunis pour pouvoir lancer la game ----------- //
        given().pathParam("token", token)
			.when().get("/lobby/{token}/start")
			.then()
				.statusCode(200);
	}
	
	
	@Test
	public void startGame_NotEveryoneReady_Test() {
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
	    // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

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
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        
        // ----------- Setting them to Ready ----------- // 
        
        userInLobbyJoiner1.setReadyStatut(true);
        userInLobbyJoiner2.setReadyStatut(true);
        userInLobbyJoiner3.setReadyStatut(true);
        userInLobbyJoiner4.setReadyStatut(false);
        
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);

        
        
        // ----------- Test que toutes les conditions sont réunis pour pouvoir lancer la game ----------- //
        given().pathParam("token", token)
			.when().get("/lobby/{token}/start")
			.then()
				.statusCode(409);
	}

}
