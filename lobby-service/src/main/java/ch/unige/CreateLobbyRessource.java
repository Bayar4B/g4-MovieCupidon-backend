package ch.unige;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.Lobby;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;

@Path("/create-lobby")
public class CreateLobbyRessource {

    private static LobbyDB lobbyDB = LobbyDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countlobbys(){
        return lobbyDB.getlobbyDB_size();
    }

    @POST
    @Path("/new-lobby")
    @Produces(MediaType.APPLICATION_JSON) 
    public Response createlobby(@FormParam("username") String username) {
    	
    	User creator_user = new User(username);
    	int ownerID = creator_user.getUserId();

    	Lobby newlobby = new Lobby(creator_user.getUserId()); 
    	String token = newlobby.getToken();
        UserInLobby userInLobby = new UserInLobby(creator_user, token);
        userLobbyDB.addUserInLobby(userInLobby);

        // Message JSON envoyé
        String message = "{\"ownerID\":"+ownerID+", \"ownerID\":"+token+"}";
        
        // Retourne 200 en cas de succès et le body "{"ownerID": ownerID}"
        return Response.status(Response.Status.OK)
        		.entity(message)
        		.type(MediaType.APPLICATION_JSON)
        		.build(); 
    }

    @GET
    @Path("/seeDB")
    @Produces(MediaType.TEXT_PLAIN)
    public ArrayList<Lobby> seeDatabaseFull(){
        return lobbyDB.getFullDB();
    }

}

