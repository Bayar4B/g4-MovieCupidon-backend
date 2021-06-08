package ch.unige;

//import java.net.URI;
import java.util.ArrayList;
import java.util.List;

//import javax.management.relation.RelationSupport;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
//import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
//import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserInLobbyDB;
//import io.vertx.core.json.JsonObject;

@Path("/play")
public class PlayResource {

    @GET
    @Path("users")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<UserInLobbyDB> UIL = UserInLobbyDB.listAll();
        return Response.ok(UIL).build();
    }

    @GET
    @Path("lobbies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllLobbies() {
        List<LobbyDB> L = LobbyDB.listAll();
        return Response.ok(L).build();
    }

    @GET
    @Path("gameStarted")
    public Response getStarted(@Context HttpHeaders headers) {

        String userid = headers.getHeaderString("X-User");

        UserInLobbyDB userExistant = UserInLobbyDB.getUser(userid);
        if (userExistant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).build();
        }
    }

    @POST
    @Transactional
    @Path("initGame/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response initGame(String initString) {

        String initJSON = "";
        try {
            initJSON = SecurityUtility.decrypt(initString);
        } catch (Exception e) {
            String message = "Encryption non valide";
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }

        StringToJSON jsonObj = null;
        try {
            jsonObj = new ObjectMapper().readValue(initJSON, StringToJSON.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Vérifier que le lobby n'est pas déjà en DB
        LobbyDB lobbyExistant = LobbyDB.getLobby(jsonObj.token);
        if (lobbyExistant != null) { 
            String message = "Lobby déjà enregistré !";
            return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
        }

        // Create players
        for (String userID : jsonObj.listPlayer) {
            UserInLobbyDB player = new UserInLobbyDB();
            player.token = jsonObj.token;
            player.userID = userID;
            UserInLobbyDB.persist(player);
        }

        // Create and init lobby
        LobbyDB lobby = new LobbyDB();
        lobby.token = jsonObj.token;
        for (int i = 0; i < 20; i++) {
            lobby.sumScores.add(0);
            lobby.numberVotes.add(0);
        }

        // Add lobby dans la DB
        LobbyDB.persist(lobby);
        return Response.created(null).build();
    }

    @POST
    @Transactional
    @Path("/send/{MOVIEID}/{SCORE}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response GameScore(@PathParam("MOVIEID") int movie_id, @PathParam("SCORE") int score,
            @Context HttpHeaders headers) {

        String userid = headers.getHeaderString("X-User");

        // movie_id valide ?
        if (movie_id < 0 || movie_id > 19 || movie_id != (int) movie_id) {
            String message = "Le id du film n'est pas valide !";
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }

        // score valide ?
        if (score < 0 || score > 100 || score != (int) score) {
            String message = "Le score attribué n'est pas valide !";
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }

        // Regarder si le sender est dans une partie
        UserInLobbyDB UIL = UserInLobbyDB.getUser(userid);
        if (UIL == null) {
            String message = "Le joueur n'est pas dans une partie !";
            return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
        }

        // Get les attributs du sender
        String token = UIL.getToken();
        // boolean user_finished = UIL.getUser_finished();
        ArrayList<Integer> votesID = UIL.getVotesID();

        // Le joueur a déjà joué toutes ses cartes ?
        if (votesID.size() >= 20) {
            String message = "Le joueur a déjà épuisé son nombre de votes !";
            return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
        }

        // Le joueur a déjà voté pour ce même film ?
        if (votesID.contains(movie_id)) {
            String message = "Le joueur a déjà voté pour ce film !";
            return Response.status(Response.Status.UNAUTHORIZED).entity(message).build();
        }

        // Get le LobbyDB correspondant au token
        LobbyDB L = LobbyDB.getLobby(token);

        // Add le score côté user
        votesID.add(movie_id);
        UIL.setVotesID(votesID);

        // Add le score côté lobby
        int currentScore = L.sumScores.get(movie_id);
        L.sumScores.set(movie_id, currentScore + score);
        int currentVotes = L.numberVotes.get(movie_id);
        L.numberVotes.set(movie_id, currentVotes + 1);

        return Response.ok().build();
    }

    @DELETE
    @Transactional
    @Path("/quit")
    @Produces(MediaType.TEXT_PLAIN)
    public Response UserQuitLobby(@Context HttpHeaders headers) {

    	String userID = headers.getHeaderString("X-User");

        // Regarder si le joueur est dans une partie
        UserInLobbyDB UIL = UserInLobbyDB.getUser(userID);
        if (UIL == null) {
            String message = "Le joueur n'est pas dans une partie !";
            return Response.status(Response.Status.NO_CONTENT).entity(message).build();
        }
        UserInLobbyDB.deleteUser(userID);
        String message = "Joueur retiré de la partie";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Transactional
    @Path("/getResult")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResult(@Context HttpHeaders headers) {

        String userid = headers.getHeaderString("X-User");

        // Vérifier si le user est dand un lobby et get le token
        String token;
        try {
            UserInLobbyDB UIL = UserInLobbyDB.getUser(userid);
            token = UIL.getToken();
        } catch (Exception e) {
            String message = "La requête n'est pas valide !";
            return Response.status(Response.Status.BAD_REQUEST).entity(message).build();
        }

        // Vérifier que tous les joueurs de ce lobby ont fini de voter pour tous les
        // films
        if (UserInLobbyDB.getStatus(token)) {
            // Retourner le résultat
            int movie_winner_id = LobbyDB.getMovieWinner(token);
            UserInLobbyDB.deleteUsers(token);
            LobbyDB.deleteLobby(token);
            return Response.status(Response.Status.OK).entity(movie_winner_id).build();
        } else {
            String message = "La partie n'est pas finie";
            return Response.status(Response.Status.NO_CONTENT).entity(message).build();
        }
    }
}