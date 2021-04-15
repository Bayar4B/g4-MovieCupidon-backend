package ch.unige;

import ch.unige.dao.SessionsDB;
import ch.unige.dao.UserDB;
import ch.unige.domain.Session;
import ch.unige.domain.User;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.ArrayList;

@Path("")
public class SessionRessource {

    private static SessionsDB sessionsDB = SessionsDB.getInstance();
    private static UserDB userDB = UserDB.getInstance();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countSessions(){
        return sessionsDB.getSessionDB_size();
    }

    @POST
    @Path("/create-session")
    @Produces(MediaType.TEXT_PLAIN) //user-creator
    public Response createSession(@Context UriInfo info, @FormParam("username") String username) {
    	
    	// --- LE @NotEmpty NE MARCHE PAS --- //
    	
    	User creator_user = new User(username);
    	userDB.add_user(creator_user);
    	System.out.println(creator_user.getUsername());

    	
    	Session newSession = new Session(creator_user.getUser_id()); 
        sessionsDB.add_session(newSession);
//        String newUrl = "http://localhost/" + newSession.getToken();
        URI uri = info.getBaseUriBuilder().path(newSession.getToken()).build();
        System.out.println(uri);

//        Response ballek = Response.temporaryRedirect(new URI(newUrl)).build();

//        return Response.ok(newSession).header("RedirectSession", newUrl).build(); // code 200 mais vaudrait mieux 303
        return Response.seeOther(uri).build(); //return le code 303 (normalement on veut ça)
        // TODO:
        //  maintenant c'est au front de rediriger vers la nouvelle adresse de la room
        //  Enfin jcrois pas finalement que le front a besoin de gerer
    }

    @GET
    @Path("/seeDB")
    @Produces(MediaType.TEXT_PLAIN)
    public ArrayList<Session> seeDatabaseFull(){
        return sessionsDB.getFullDB();
    }

    @GET
    @Path("{TOKEN}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response Lobby(@PathParam("TOKEN") String token, @Context UriInfo info){
        URI uri = info.getAbsolutePath();
        return Response.ok(uri).build();
    }


}

