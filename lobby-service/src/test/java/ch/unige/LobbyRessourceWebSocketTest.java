package ch.unige;

import static io.restassured.RestAssured.given;

import java.net.URI;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.Lobby;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.TestCase;

@QuarkusTest
public class LobbyRessourceWebSocketTest extends TestCase{

	private static UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	
	private static final LinkedBlockingDeque<String> MESSAGES = new LinkedBlockingDeque<>();


    
    @BeforeAll
    public static void setup() {
    	// ----------- Création du Owner ----------- // 
    	User creator_user1 = new User("ownerUsername");
    	User creator_user2 = new User("ownerUsername");

        // ----------- Init des joiners ----------- // 
    	User joiner1 = new User("joiner1");
    	User joiner2 = new User("joiner2");

    	// Setting des bonnes valeurs pour le path
    	creator_user1.setUserID(1000);
    	joiner1.setUserID(1001);    
    	joiner2.setUserID(2000);
    	
        // ----------- Création du lobby ----------- // 
    	Lobby newLobby1 = new Lobby(creator_user1.getUserId()); 
    	Lobby newLobby2 = new Lobby(creator_user2.getUserId()); 

    	// Setting des bonnes valeurs pour le path
    	newLobby1.setToken("TOKEN1");
    	newLobby2.setToken("TOKEN2");

    	// Création des instance UserInLobby
    	UserInLobby userInLobby1 = new UserInLobby(creator_user1, newLobby1.getToken());
    	UserInLobby userInLobbyJoiner1 = new UserInLobby(joiner1, newLobby1.getToken());
    	
    	UserInLobby userInLobby2 = new UserInLobby(creator_user2, newLobby2.getToken());
    	UserInLobby userInLobbyJoiner2 = new UserInLobby(joiner2, newLobby2.getToken());

    	// Ajout à la DB userInLobby
        userLobbyDB.addUserInLobby(userInLobby1);
    	userLobbyDB.addUserInLobby(userInLobbyJoiner1);
    	
    	userLobbyDB.addUserInLobby(userInLobby2);
    	userLobbyDB.addUserInLobby(userInLobbyJoiner2);
    	
    }
    
    @TestHTTPResource("/lobbyWS/TOKEN1/1000/")
    URI correctUri;
    
    @TestHTTPResource("/lobbyWS/TOKEN1/1001/")
    URI correctJoiner1Uri;
    
    @TestHTTPResource("/lobbyWS/TOKEN3/1000/")
    URI wrongTokenUri;
    
    @TestHTTPResource("/lobbyWS/TOKEN1/3000/")
    URI wrongUserIDUri;
    
    @TestHTTPResource("/lobbyWS/TOKEN1/2000/")
    URI userInADifferentLobbyUri;
    
    @Test
    public void testWebsocket() throws Exception {
    	
        try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, correctUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("helloworld");
            Assertions.assertEquals("This is lobby service in lobbyRessource", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("connectToLobby");
            Assertions.assertEquals("You are in the lobby : TOKEN1", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    }
    
    @Test
    public void ToggleReadyTest() throws Exception {
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, correctUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("toggleReady");
            Assertions.assertEquals("{\"isOwner\":1000, \"Status\": true}", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    
    	// Correct Toggle Ready !
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, wrongTokenUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("toggleReady");
            Assertions.assertEquals("This token doesn't belongs to any lobby. Token : TOKEN3", MESSAGES.poll(10, TimeUnit.SECONDS));
        }

    	// Toggle Ready with a wrong Token
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, wrongTokenUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("toggleReady");
            Assertions.assertEquals("This token doesn't belongs to any lobby. Token : TOKEN3", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    
    	// Toggle Ready with a wrong User ID
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, wrongUserIDUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("toggleReady");
            Assertions.assertEquals("There isn't any user with this ID, userID : 3000", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    	
    	// Toggle Ready with a user that isn't in this lobby
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, userInADifferentLobbyUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("toggleReady");
            Assertions.assertEquals("There isn't any user with this ID in the lobby : Token : TOKEN1 ; userID : 2000", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    }

    
    @Test
    public void isOwnerTest() throws Exception {
    	// Correct isOwner = true 
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, correctUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("isOwner");
            Assertions.assertEquals("{\"isOwner\":true}", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    	
    	// Correct isOwner = false 
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, correctJoiner1Uri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("isOwner");
            Assertions.assertEquals("{\"isOwner\":false}", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    
    	// isOwner wrong token
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, wrongTokenUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("isOwner");
            Assertions.assertEquals("This token doesn't belongs to any lobby. Token : TOKEN3", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    	
    	// isOwner with a user that isn't in this lobby
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, userInADifferentLobbyUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("isOwner");
            Assertions.assertEquals("There isn't any user with this ID in the lobby : Token : TOKEN1 ; userID : 2000", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    }
    
    
    @Test
    public void whoIsTheOwnerTest() throws Exception {
    	// Correct isOwner = true 
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, correctUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("whoIsTheOwner");
            Assertions.assertEquals("{\"ownerID\":1000}", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    
    	// isOwner wrong token
    	try (Session session = ContainerProvider.getWebSocketContainer().connectToServer(Client.class, wrongTokenUri)) {
            Assertions.assertEquals("CONNECT", MESSAGES.poll(10, TimeUnit.SECONDS));
            session.getAsyncRemote().sendText("whoIsTheOwner");
            Assertions.assertEquals("This token doesn't belongs to any lobby. Token : TOKEN3", MESSAGES.poll(10, TimeUnit.SECONDS));
        }
    }
    
    
    @ClientEndpoint
    public static class Client {

    	@OnOpen
        public void open(Session session) {
            MESSAGES.add("CONNECT");
            // Send a message to indicate that we are ready,
            // as the message handler may not be registered immediately after this callback.
//            session.getAsyncRemote().sendText("_ready_");
        }

    	@OnMessage
        void message(String msg) {
            MESSAGES.add(msg);
        }

    }

}
