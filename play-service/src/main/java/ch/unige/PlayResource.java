package ch.unige;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ch.unige.dao.GameDB;

@Path("/play")
public class PlayResource {

    private static GameDB gameDB = GameDB.getInstance();

    @GET
    @Path("/send/{TOKEN}/{MOVIEID}/{SCORE}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response GetGameScores(@PathParam("TOKEN") String token, @PathParam("MOVIEID") int movie_id, @PathParam("SCORE") int score) {

        /**
         * TOKEN : token du lobby qui joue la partie
         * MOVIEID : id du film voté par le joueur. Les ids vont de 0 à 19 !
         * SCORE : score du joueur pour un film particulier
         */

        // * Récupère le score d'un user donné pour un film donné
        gameDB.saveScores(token, movie_id, score);

        String message = "Success";
        return Response.status(Response.Status.OK).entity(message).build();
    }

    @GET
    @Path("/getScore/{TOKEN}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response GetGameScores(@PathParam("TOKEN") String token) {

        /**
         * TOKEN : token du lobby dont on veut le résultat
         * L'appel de cette fonction peut se faire à tout moment 
         * même si la partie n'est pas terminée.
         */

        // * Retourne le movie_id du film gagnant
        int result_id = gameDB.getResult(token);
        gameDB.deleteData(token);

        return Response.status(Response.Status.OK).entity(result_id).build();
    }
}