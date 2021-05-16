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
public class StartGameTest extends TestCase{

	@Test
	public void startGame_Test() {
		// ----------- Init des DB ----------- // 
		
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	    
	    
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
