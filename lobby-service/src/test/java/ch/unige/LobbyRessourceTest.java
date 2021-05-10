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

import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class LobbyRessourceTest extends TestCase{
	
	// ----------- Init des DB ----------- // 
	
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
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

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
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

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
		
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

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
			.when().post("/lobby/join")
			.then()
				.statusCode(401);
	}
	
	@Test
	public void isOwnerTest() {
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
	    int ownerID = creator_user.getUserId();

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
        
		given().pathParam("token", token).pathParam("userID", ownerID)
			.when().get("/lobby/{token}/{userID}/isOwner")
			.then()
				.statusCode(200)
				.body("isOwner", is(true));
	}
	
	@Test
	public void isOwner_wrongUserID_Test() {
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
        
        int joiner4ID = joiner4.getUserId();

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
        
		given().pathParam("token", token).pathParam("userID", joiner4ID)
			.when().get("/lobby/{token}/{userID}/isOwner")
			.then()
				.statusCode(200)
				.body("isOwner", is(false));
	}
	
	@Test
	public void isOwner_wrongToken_Test() {
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
	    int ownerID = creator_user.getUserId();

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
        // ----------- Teste le fait que la Lobby est bien pleine ----------- // 
        
		given().pathParam("token", "ABCD").pathParam("userID", ownerID)
			.when().get("/lobby/{token}/{userID}/isOwner")
			.then()
				.statusCode(404);
	}
	
	
	@Test
	public void isOwner_userIsNotInThisLobby_Test() {
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
        
        User otherUser = new User("otherUser");
        int otherUserID = otherUser.getUserId();

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
        
		given().pathParam("token", token).pathParam("userID", otherUserID)
			.when().get("/lobby/{token}/{userID}/isOwner")
			.then()
				.statusCode(409);
	}
	
	@Test
	public void whoIsTheOwnerTest() {
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");
	    int ownerID = creator_user.getUserId();

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
        
		given().pathParam("token", token)
			.when().get("/lobby/{token}/whoIsTheOwner")
			.then()
				.statusCode(200)
				.body("ownerID", is(ownerID));
	}
	
	@Test
	public void whoIsTheOwner_wrongToken_Test() {
		// ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername");

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
        
        // ----------- Teste le fait que la Lobby est bien pleine ----------- // 
        
		given().pathParam("token", "ABCD")
			.when().get("/lobby/{token}/whoIsTheOwner")
			.then()
				.statusCode(404);
	}

	@Test
	public void removeUserFromLobby_Test() {
		
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

		// ----------- Get l'id du joueur 1 ----------- //

		int joiner1_id = joiner2.getUserId();

        // ----------- Ajout des Joiners à la Lobby ----------- // 
        
        UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner1);
        UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner2);
        UserInLobby userInLobbyJoiner3 = new UserInLobby(joiner3, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner3);
        UserInLobby userInLobbyJoiner4 = new UserInLobby(joiner4, token);
        userLobbyDB.addUserInLobby(userInLobbyJoiner4);
        
        
        // ----------- Teste de remove le user à l'id = 2 ----------- // 
        
		given().pathParam("TOKEN", token).pathParam("USERID", joiner1_id)
			.when().get("/lobby/quit/{TOKEN}/{USERID}")
			.then()
				.statusCode(200);

		// ----------- Teste de remove à nouveau le même user ----------- // 
        
		given().pathParam("TOKEN", token).pathParam("USERID", joiner1_id)
			.when().get("/lobby/quit/{TOKEN}/{USERID}")
			.then()
				.statusCode(404);

		// ----------- Teste de user = 2 avec mauvais token ----------- // 
        
		given().pathParam("TOKEN", "ABCDEF").pathParam("USERID", joiner1_id + 1)
			.when().get("/lobby/quit/{TOKEN}/{USERID}")
			.then()
				.statusCode(404);

		// ----------- Teste de remove avec id HS ----------- // 
        
		given().pathParam("TOKEN", token).pathParam("USERID", 6666)
			.when().get("/lobby/quit/{TOKEN}/{USERID}")
			.then()
				.statusCode(404);
	}
}
