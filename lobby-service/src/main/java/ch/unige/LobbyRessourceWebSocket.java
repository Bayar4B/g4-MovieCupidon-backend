package ch.unige;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.SessionsPerLobbyDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.websocket.server.PathParam;

@ServerEndpoint("/lobbyWS/{token}/{userID}")
@ApplicationScoped
public class LobbyRessourceWebSocket {

    private static LobbyDB lobbyDB = LobbyDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();


    private static SessionsPerLobbyDB  sessionsPerLobbyDB = SessionsPerLobbyDB.getInstance();
	
	private Map<Integer, Session> sessions;
	private Map<String ,Map<Integer, Session>> sessionsList = sessionsPerLobbyDB.getSessionsListDB();

    @OnOpen
    public void onOpen(Session session,@PathParam("token") String token, @PathParam("userID") int userID) {
    		sessions = sessionsList.get(token);
    	
    	if (sessions == null) {
    		sessions = new ConcurrentHashMap<Integer, Session>();
    		sessionsList.put(token, sessions);
		}
    	
        sessions.put(userID, session);
    }

    @OnClose
    public void onClose(Session session, @PathParam("token") String token, @PathParam("userID") int userID) {
    	sessions = sessionsList.get(token);
    	
        sessions.remove(userID);
    }



    @OnMessage
    public void onMessage(String action, @PathParam("token") String token, @PathParam("userID") int userID) {
    	// Selon l'action, on fait la fonction correspondante
    	if (action.equals("helloworld")) {
            System.out.println("helloworld Trigered");
    		helloWorld(token, userID);
    	}
    	if (action.equals("toggleReady")) {
    		toggleReady(token, userID);
    	}
    }
    
    
    private void helloWorld(String token, int userID) {
    	// On récupère la session websocket actuelle
        Session actualSession = sessionsList.get(token).get(userID);

        String message = "This is lobby service in lobbyRessource";
               
        System.out.println("Succeed sending the hello message.");
        actualSession.getAsyncRemote().sendObject(message, result -> {
        	if (result.getException() != null) {
                System.out.println("Unable to send the hello from lobby message : "+result.getException());
        	}
        });
    }
    
    public void toggleReady(String token, int userID){
    	// On récupère la session websocket actuelle
    	Session actualSession = sessionsList.get(token).get(userID);
    	
    	if (!lobbyDB.lobbyExist(token)){
            // Check if lobby exist
        	System.out.println("This token doesn't belongs to any lobby. Token : "+token);
        	
        	String message = "This token doesn't belongs to any lobby. Token : "+token;
        	
        	actualSession.getAsyncRemote().sendObject(message, result -> {
            	if (result.getException() != null) {
                    System.out.println("Unable to send the message : "+message+" : "+result.getException());
            	}
            });
        	return;
        }
    	
        int userIndexUserInDB = userLobbyDB.findUserInLobbyById(userID);
        if (userIndexUserInDB  == -1){
            // User doesn't exist in the userInLobbyDB...
        	System.out.println("Aucun User avec cet ID");
        	
        	String message = "There isn't any user with this ID, userID : " + userID;
        	
        	actualSession.getAsyncRemote().sendObject(message, result -> {
            	if (result.getException() != null) {
                    System.out.println("Unable to send the message : "+message+" : "+result.getException());
            	}
            });
        	return;
        }
        
        if(  !token.equalsIgnoreCase( userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getLobbyToken() )  ){
        		// User Not in the correct Lobby..
        	System.out.println("Cet utilisateur(" +userID +") n'est pas dans ce Lobby:"+token);
        	
        	String message = "There isn't any user with this ID in the lobby : Token : " + token + " ; userID : " + userID;

        	actualSession.getAsyncRemote().sendObject(message, result -> {
            	if (result.getException() != null) {
                    System.out.println("Unable to send the message : "+message+" : "+result.getException());
            	}
            });
        	return;
        }

        // Si toutes les conditions sont remplies
        userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).toggleReadyStatus();
        
        System.out.println("Succeed changing the user : " + userID + " to ready");
        String message = "{\"isOwner\":"+userID+", \"Status\": "+userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getReadyStatus()+"}";
        
        actualSession.getAsyncRemote().sendObject(message, result -> {
        	if (result.getException() != null) {
                System.out.println("Unable to send the message : "+message+" : "+result.getException());
        	}
        });
    	return;
    }
    
}
