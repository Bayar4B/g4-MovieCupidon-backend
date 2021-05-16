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

@Path("/join-lobby")
public class JoinLobbyRessource {
	
	private static LobbyDB lobbyDB = LobbyDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();
	
	@POST
    @Path("/join")
    @Produces(MediaType.APPLICATION_JSON)
    public Response joinLobby(@FormParam("username") String username, @FormParam("token") String token){
    	
        if (!lobbyDB.lobbyExist(token)){
            // Check if lobby exist
            return Response.status(Response.Status.NOT_FOUND).entity("lobby inexistante ou mauvais token.").build();
        }

        if (!userLobbyDB.isTherePlaceInLobby(token)){
            //check if there is space in the looby
            return Response.status(Response.Status.UNAUTHORIZED).entity("Aucune place dans le lobby restante.").build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */
        }

        User joiner = new User(username);
        int joinerID = joiner.getUserId();

        UserInLobby newUserInLobby = new UserInLobby(joiner, token);
        userLobbyDB.addUserInLobby(newUserInLobby);

        String message = "{\"joinerID\":"+joinerID+"}";
        
        // Retourne 200 en cas de succès et le body "{"ownerID": ownerID}"
        return Response.status(Response.Status.OK)
        		.entity(message)
        		.type(MediaType.APPLICATION_JSON)
        		.build(); 
    }
	
}
