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
    @Produces(MediaType.TEXT_PLAIN) //user-creator
    public Response createlobby(@Context UriInfo info, @FormParam("username") String username) {
    	
    	User creator_user = new User(username);
    	System.out.println("Owner ID : "+creator_user.getUserId());

    	Lobby newlobby = new Lobby(creator_user.getUserId()); 

        UserInLobby userInLobby = new UserInLobby(creator_user, newlobby.getToken());
        userLobbyDB.addUserInLobby(userInLobby);

//        String newUrl = "http://localhost/" + newlobby.getToken();
        URI uri = info.getBaseUriBuilder().path("lobby/" + newlobby.getToken()).build();
        System.out.println(uri);

//        return Response.ok(newlobby).header("Redirectlobby", newUrl).build(); // code 200 mais vaudrait mieux 303
        return Response.seeOther(uri).build(); //return le code 303 (normalement on veut ça)
    }

    @GET
    @Path("/seeDB")
    @Produces(MediaType.TEXT_PLAIN)
    public ArrayList<Lobby> seeDatabaseFull(){
        return lobbyDB.getFullDB();
    }

}

