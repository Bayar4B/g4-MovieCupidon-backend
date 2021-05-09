package ch.unige;

import org.junit.Before;
import org.junit.BeforeClass;
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
public class ToggleReadyStatusTest extends TestCase{
	
	/* La majorité du code en commentaire étaient des essais d'utiliser le @befor pour éviter d'avoir à chaque fois une initialisation
	 * Mais bon.. Je n'ai pas réussi à la faire marcher. */
//	static lobbyDB lobbyDB;
//	static UserDB userDB;
//	static UserInLobbyDB userLobbyDB;
//	static User creator_user;
//	static Lobby newLobby;
//	static UserInLobby userInLobby;
	
	
//	@Before
//	public static void settingUpValues() {
//		System.out.println("SETTING UP ALL THE VALUES FOR THE TOGGLE READY STATUS TESTS \n \n");
////		 ----------- Init des DB ----------- // 
//		lobbyDB = lobbyDB.getInstance();
//		userDB = UserDB.getInstance();
//		userLobbyDB = UserInLobbyDB.getInstance();
////	  ----------- Création d'une Lobby ----------- // 
//	    
//	    creator_user = new User("ownerUsername_toggle_ready");
//    	userDB.add_user(this.creator_user);
//
//    	newLobby = new Lobby(creator_user.getUserId()); 
//        lobbyDB.add_lobby(newLobby);
//
//        userInLobby = new UserInLobby(creator_user, newLobby.getToken());
//        userLobbyDB.addUserInLobby(userInLobby);
//		System.out.println("FINISHED SETTING UP ALL THE VALUES FOR THE TOGGLE READY STATUS TESTS \n \n");
//
//	}
	
	
	@Test
	public void togglingReadyStatusProg() {
//		System.out.println("STARTING  togglingReadyStatusProg TEST \n \n");

		// ----------- Init des DB ----------- // 
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	 // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername_test_toggle_1");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
		
        userInLobby.toggleReadyStatus();      
    	assertEquals(true, userInLobby.getReadyStatus());


	}
	
	@Test
	public void failTogglingReadyStatus() {
//		System.out.println("STARTING  failTogglingReadyStatus TEST \n \n");
		// ----------- Init des DB ----------- // 
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	 // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername_test_toggle_2");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();


		given().pathParam("TOKEN", token).pathParam("USERID",  3)
		.when().post("/lobby/{TOKEN}/{USERID}/toggleready").then().statusCode(401);
	}
	
	@Test
	public void togglingReadyStatusRest() {
//		System.out.println("STARTING  togglingReadyStatusRest TEST \n \n");
		// ----------- Init des DB ----------- // 
		LobbyDB lobbyDB = LobbyDB.getInstance();
	    UserDB userDB = UserDB.getInstance();
	    UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	 // ----------- Création d'une Lobby ----------- // 
	    
	    User creator_user = new User("ownerUsername_test_toggle_3");
    	userDB.add_user(creator_user);

    	Lobby newLobby = new Lobby(creator_user.getUserId()); 
        lobbyDB.add_lobby(newLobby);

        UserInLobby userInLobby = new UserInLobby(creator_user, newLobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);
        
        String token = newLobby.getToken();
//		LobbyRessource lobbyRessource = new LobbyRessource();
//		System.out.println(lobbyRessource.seeUserInLobbyDB());

		given().pathParam("TOKEN", token).pathParam("USERID",  creator_user.getUserId())
		.when().post("/lobby/{TOKEN}/{USERID}/toggleready").then().statusCode(200);

	}
	

}
