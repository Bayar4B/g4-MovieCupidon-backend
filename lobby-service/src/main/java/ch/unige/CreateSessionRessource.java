package ch.unige;

import ch.unige.dao.SessionsDB;
import ch.unige.dao.UserDB;
import ch.unige.dao.User_in_lobby_DB;
import ch.unige.domain.Session;
import ch.unige.domain.User;
import ch.unige.domain.User_in_lobby;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;

@Path("/create-session")
public class CreateSessionRessource {

    private static SessionsDB sessionsDB = SessionsDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();
    private static User_in_lobby_DB user_lobby_DB = User_in_lobby_DB.getInstance();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countSessions(){
        return sessionsDB.getSessionDB_size();
    }

    @POST
    @Path("/new-session")
    @Produces(MediaType.TEXT_PLAIN) //user-creator
    public Response createSession(@Context UriInfo info, @FormParam("username") String username) {
    	
    	// --- LE @NotEmpty NE MARCHE PAS --- //
    	
    	User creator_user = new User(username);
    	userDB.add_user(creator_user);
    	System.out.println(creator_user.getUsername());

    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);

        User_in_lobby new_user_in_lobby = new User_in_lobby(creator_user, newSession);
        user_lobby_DB.add_User_in_lobby(new_user_in_lobby);

//        String newUrl = "http://localhost/" + newSession.getToken();
        URI uri = info.getBaseUriBuilder().path("session/" + newSession.getToken()).build();
        System.out.println(uri);

//        return Response.ok(newSession).header("RedirectSession", newUrl).build(); // code 200 mais vaudrait mieux 303
        return Response.seeOther(uri).build(); //return le code 303 (normalement on veut ça)
    }

    @GET
    @Path("/seeDB")
    @Produces(MediaType.TEXT_PLAIN)
    public ArrayList<Session> seeDatabaseFull(){
        return sessionsDB.getFullDB();
    }


}

