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
import java.util.List;
import java.util.stream.Collectors;

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleReady(@PathParam("TOKEN") String token, @PathParam("USERID") int userid ){

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
        
        if(  !token.equalsIgnoreCase( userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getLobbyToken() )  ){
        		// User Not in the correct Lobby..
        	System.out.println("Cet utilisateur(" +userid +") n'est pas dans ce Lobby:"+token);
        	return Response.status(Response.Status.UNAUTHORIZED).entity("Cet utilisateur n'est pas dans ce Lobby").build(); //code 401
            /*
            TODO
                code 401 pour le moment mais il faudra voir si on change pas
             */	
        }

        userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).toggleReadyStatus();
        
        String message = "{\"isOwner\":"+userid+", \"Status\": "+userLobbyDB.getFullUserInLobbyDB().get(userIndexUserInDB).getReadyStatus()+"}";
        
        return Response.status(Response.Status.OK)
    			.entity(message)
    			.type(MediaType.APPLICATION_JSON)
    			.build();
    }

    @GET
    @Path("/quit/{TOKEN}/{USERID}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response UserInL(@PathParam("TOKEN") String token, @PathParam("USERID") int user_id){
        String message;
        if(userLobbyDB.removeUserFromLobby(token, user_id) && userDB.removeUser(user_id)){
            message = "User removed end deleted";
            return Response.status(Response.Status.OK).entity(message).build();
        }
        message = "User or lobby not found";
        return Response.status(Response.Status.NOT_FOUND).entity(message).build();
    }
    
    /*TODO: This is for dev purposes only: */
    
    @GET
    @Path("/seeUserInLobbyDB")
    public String seeUserInLobbyDB() {
        return String.valueOf(userLobbyDB);
    }
    
    @GET
    @Path("/{token}/{userID}/isOwner")
    @Produces(MediaType.APPLICATION_JSON)
    public Response isOwner(@PathParam("token") String token, @PathParam("userID") int userID) {
    	
    	// Test si le lobby est bien présent
    	if (!lobbyDB.lobbyExist(token)){
            // Check if lobby exist
            return Response.status(Response.Status.NOT_FOUND).entity("lobby inexistante ou mauvais token.").build();
        }
    	
    	// Verifie que ce userID est bien dans la session demandée
    	if (!userLobbyDB.isUserInLobby(token, userID)) {
    		return Response.status(Response.Status.CONFLICT).entity("Ce user n'est pas présent dans cette session.").build();
    	}
    	
    	// La valeur de retour si le user est bien le owner ou non
    	boolean isHeTheOwner = (lobbyDB.getFullDB().stream()
    		.filter(l -> l.getToken().equals(token) && l.getCreator_user_id() == userID)
    		.count() > 0);
    	
    	// Creation du string convertit en JSON
    	String message = "{\"isOwner\":"+isHeTheOwner+"}";
    	
    	return Response.status(Response.Status.OK)
    			.entity(message)
    			.type(MediaType.APPLICATION_JSON)
    			.build();
    }
    
    @GET
    @Path("/{token}/whoIsTheOwner")
    @Produces(MediaType.APPLICATION_JSON)
    public Response whoIsTheOwner(@PathParam("token") String token) {
    	
    	// Test si le lobby est bien présent
    	if (!lobbyDB.lobbyExist(token)){
            // Check if lobby exist
            return Response.status(Response.Status.NOT_FOUND).entity("lobby inexistante ou mauvais token.").build();
        }
    	
    	int ownerID = lobbyDB.getFullDB().stream()
    			.filter(l -> l.getToken().equals(token))
    			.collect(Collectors.toList())
    			.get(0)
    			.getCreator_user_id();
    	
    	String message = "{\"ownerID\":"+ownerID+"}";
    	return Response.status(Response.Status.OK)
    			.entity(message)
    			.type(MediaType.APPLICATION_JSON)
    			.build();
    }
    
}

