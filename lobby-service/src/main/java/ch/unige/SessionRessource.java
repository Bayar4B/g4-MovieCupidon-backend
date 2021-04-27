package ch.unige;

import ch.unige.dao.SessionsDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.UserInLobbyDB;
import ch.unige.domain.User;
import ch.unige.domain.UserInLobby;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;

@Path("/session")
public class SessionRessource {

    private static SessionsDB sessionsDB = SessionsDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static UserInLobbyDB user_lobby_DB = UserInLobbyDB.getInstance();


    @GET
    @Path("{TOKEN}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response Lobby(@PathParam("TOKEN") String token, @Context UriInfo info){
        // Here we are in the lobby
        URI uri = info.getAbsolutePath();
        return Response.ok(uri).build();
    }

    @POST
    @Path("/join")
    @Produces(MediaType.TEXT_PLAIN)
    public Response joinLobby(@Context UriInfo info, @FormParam("username") String username, @FormParam("token") String token){

        if (!sessionsDB.sessionExist(token)){
            // Check if session exist
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (!user_lobby_DB.isTherePlaceInLobby(token)){
            //check if there is space in the looby
            return Response.status(Response.Status.UNAUTHORIZED).build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */
        }

        User creator_user = new User(username);
        userDB.add_user(creator_user);

        UserInLobby newUserInLobby = new UserInLobby(creator_user, token);
        user_lobby_DB.addUserInLobby(newUserInLobby);

        URI uri = info.getBaseUriBuilder().path("session/" + token).build();
        return Response.ok(uri).build();
    }

    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "This is lobby service in SessionRessource";
    }

    @POST
    @Path("/{TOKEN}/{USERID}/ToggleReady")
    @Produces(MediaType.TEXT_PLAIN)
    public Response toggleReady(@Context UriInfo info, @PathParam("TOKEN") String token, @PathParam("USERID") int userid ){

        if (!sessionsDB.sessionExist(token)){
            // Check if session exist
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        int userIndexUserInDB = user_lobby_DB.findUserInLobbyById(userid);
        if (userIndexUserInDB  == -1){
            // User doesn't exist in the userInLobbyDB...
        	
            return Response.status(Response.Status.UNAUTHORIZED).build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */
        }
        //TODO Check if user is in the said lobby.
//        int lobbyIndexUserInDB = user_lobby_DB.findLobbyInUserInLobbyDBByToken(token);
//        if(  lobbyIndexUserInDB != userIndexUserInDB  ){
//        		// User Not in the correct Database..
//        	return Response.status(Response.Status.UNAUTHORIZED).build(); //code 401
//            /*
//            TODO
//                code 401 pour le moment mais il faudra voir si on change pas
//             */	
//        }

        user_lobby_DB.getFullUserInLobbyDB().get(userIndexUserInDB).toggleReadyStatus();
        
        return Response.ok().build();
    }
}

