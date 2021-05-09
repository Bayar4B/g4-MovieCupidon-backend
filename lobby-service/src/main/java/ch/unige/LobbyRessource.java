package ch.unige;

import ch.unige.dao.LobbyDB;
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

@Path("/lobby")
public class LobbyRessource {

    private static LobbyDB lobbyDB = LobbyDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static UserInLobbyDB userLobbyDB = UserInLobbyDB.getInstance();


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

        User creator_user = new User(username);
        userDB.add_user(creator_user);

        UserInLobby newUserInLobby = new UserInLobby(creator_user, token);
        userLobbyDB.addUserInLobby(newUserInLobby);

        URI uri = info.getBaseUriBuilder().path("lobby/" + token).build();
        return Response.ok(uri).build();
    }

    @GET
    @Path("/{token}/start")
    @Produces(MediaType.TEXT_PLAIN)
    public Response startGame(@PathParam("token") String token) {
    	
    	if (!lobbyDB.lobbyExist(token)){
    		return Response.status(Response.Status.NOT_FOUND).entity("lobby inexistante ou mauvais token.").build();
    	}

    	if(!userLobbyDB.isEveryoneReady(token)) {
    		return Response.status(Response.Status.CONFLICT).entity("Tous les joiner ne sont pas ready.").build();
    	}	
    	
    	return Response.ok().build();
    }
    
    
    @GET
    @Path("/helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "This is lobby service in lobbyRessource";
    }

    @POST
    @Path("/{TOKEN}/{USERID}/toggleready")
    @Produces(MediaType.TEXT_PLAIN)
    public Response toggleReady(@Context UriInfo info, @PathParam("TOKEN") String token, @PathParam("USERID") int userid ){

        if (!lobbyDB.lobbyExist(token)){
            // Check if lobby exist
        	System.out.println("Aucun Lobby avec ce Token" );
            return Response.status(Response.Status.BAD_REQUEST).entity("Aucun Lobby avec ce Token").build();
        }
        int userIndexUserInDB = userLobbyDB.findUserInLobbyById(userid);
        if (userIndexUserInDB  == -1){
            // User doesn't exist in the userInLobbyDB...
        	System.out.println("Aucun User avec cet ID");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Aucun User avec cet ID").build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */
        }
        //TODO Check if user is in the said lobby.
        
        if(  !token.equalsIgnoreCase( userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getLobby() )  ){
        		// User Not in the correct Lobby..
        	System.out.println("Cet utilisateur(" +userid +") n'est pas dans ce Lobby:"+token);
        	return Response.status(Response.Status.UNAUTHORIZED).entity("Cet utilisateur n'est pas dans ce Lobby").build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */	
        }

        userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).toggleReadyStatus();
        return Response.ok("Nouveau Ready Statut: " + String.valueOf( userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getReadyStatus()) ).build();

    }
    

    
    /*TODO: This is for dev purposes only: */
    
    @GET
    @Path("/seeUserInLobbyDB")
    @Produces(MediaType.TEXT_PLAIN)
    public String seeUserInLobbyDB() {
        return String.valueOf(userLobbyDB);
    }
    
}

