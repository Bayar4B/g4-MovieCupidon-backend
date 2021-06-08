package ch.unige;

import io.quarkus.test.junit.QuarkusTest;
//import io.restassured.http.Header;

import org.junit.jupiter.api.Test;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserInLobbyDB;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
//import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@QuarkusTest
public class PlayResourceTest {

    @Test
    public void InitGameTest() {
        // Init new room
        given().body(
                "sfql1a4vXLYdvHRJBhxPlgntz9jJy38Sfd+raZnOWAYxLQGtRJPV5ayH7zXsXUEsC118UcjVivBMgjbk95pekNP1v799frKY6ZWzYBONKWAqbanVAy1QAOjg2nKDPsPi19DUp/UX4IQ3kF11ae3KR2rZesPhlJlInvSnhdKYMp5tvi/ai3JS/D97tcvMN/nWMXNx+1N/4Ppoqgo5WmtkgX/rzgnexmpwLj7378jOLKQ=")
                .when().post("/play/initGame").then().statusCode(201);
        // Essayer de re-init la même room
        given().body(
                "sfql1a4vXLYdvHRJBhxPlgntz9jJy38Sfd+raZnOWAYxLQGtRJPV5ayH7zXsXUEsC118UcjVivBMgjbk95pekNP1v799frKY6ZWzYBONKWAqbanVAy1QAOjg2nKDPsPi19DUp/UX4IQ3kF11ae3KR2rZesPhlJlInvSnhdKYMp5tvi/ai3JS/D97tcvMN/nWMXNx+1N/4Ppoqgo5WmtkgX/rzgnexmpwLj7378jOLKQ=")
                .when().post("/play/initGame").then().statusCode(401);
        // Essayer de init avec une encryption non valide
        given().body(
                "fql1a4vXLYdvHRJBhxPlgntz9jJy38Sfd+raZnOWAYxLQGtRJPV5ayH7zXsXUEsC118UcjVivBMgjbk95pekNP1v799frKY6ZWzYBONKWAqbanVAy1QAOjg2nKDPsPi19DUp/UX4IQ3kF11ae3KR2rZesPhlJlInvSnhdKYMp5tvi/ai3JS/D97tcvMN/nWMXNx+1N/4Ppoqgo5WmtkgX/rzgnexmpwLj7378jOLKQ=")
                .when().post("/play/initGame").then().statusCode(400);
        // Essayer de init avec une encryption vide
        given().body("").when().post("/play/initGame").then().statusCode(400);
        // Essayer de init sans encryption
        given().when().post("/play/initGame").then().statusCode(415);

    }

    @Test
    public void VerifyUserInLobbyTest() {
        // Le lobby est déjà créé, vérifier que les joueurs y soient
        List<UserInLobbyDB> UIL = UserInLobbyDB.listAll();
        for(UserInLobbyDB user : UIL) {
            // Vérifier le token des users
            assertEquals("ABVDC8", user.token);
            assertEquals("ABVDC8", user.getToken());
            // Vérifier que la liste les votes est vide
            assertTrue(user.votesID.isEmpty());
            assertTrue(user.getVotesID().isEmpty());
            // Vérifier les userID
            if(user.id == 1) {
                assertEquals("OwnerIDTest", user.userID);
                assertEquals("OwnerIDTest", user.getUserID());
            }
            if(user.id == 2) {
                assertEquals("JoinerIDTest1", user.userID);
                assertEquals("JoinerIDTest1", user.getUserID());
            }
            if(user.id == 3) {
                assertEquals("JoinerIDTest2", user.userID);
                assertEquals("JoinerIDTest2", user.getUserID());
            }
            if(user.id == 4) {
                assertEquals("JoinerIDTest3", user.userID);
                assertEquals("JoinerIDTest3", user.getUserID());
            }
        }
    }

    @Test
    public void VerifyLobbyTest() {
        // Le lobby est déjà créé, vérifier que les joueurs y soient
        List<LobbyDB> L = LobbyDB.listAll();
        for(LobbyDB lobby : L) {
            // Vérifier le token des users
            assertEquals("ABVDC8", lobby.token);
            assertEquals("ABVDC8", lobby.getToken());
            // Get les deux arrays du lobby
            ArrayList<Integer> nb_Votes = lobby.numberVotes;
            ArrayList<Integer> sum_Scores = lobby.sumScores;
            ArrayList<Integer> nb_Votes_get = lobby.getNumberVotes();
            ArrayList<Integer> sum_Scores_get = lobby.getSumScores();
            assertEquals(nb_Votes, nb_Votes_get);
            assertEquals(sum_Scores, sum_Scores_get);
            // Vérifier la tailles des array (20)
            assertEquals(20,nb_Votes.size());
            assertEquals(20,nb_Votes_get.size());
            assertEquals(20,sum_Scores.size());
            assertEquals(20,sum_Scores_get.size());
            // Vérifier que tout soit initialisé à 0
            for(int i = 0; i < 20; i++) {
                assertEquals(0, nb_Votes.get(i));
                assertEquals(0, nb_Votes_get.get(i));
            }
        }
    }
    /*
    @Test
    public void getStartedTest() {
        // get une game déjà démarrée avec un id valide
        given().header("X-User", "OwnerIDTest").when().get("/play/gameStarted").then().statusCode(202);
        given().header("X-User", "JoinerIDTest2").when().get("/play/gameStarted").then().statusCode(202);
        // get une game déjà démarrée avec un id non valide
        given().header("X-User", "OwnerIDIDTest").when().get("/play/gameStarted").then().statusCode(404);
        given().header("X-User", "JoinerIDIDTest2").when().get("/play/gameStarted").then().statusCode(404);
        // get une game non existante donc pac encore démarrée
    }
    */    
}