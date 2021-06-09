package ch.unige;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Test;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserInLobbyDB;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import javax.transaction.Transactional;

@QuarkusTest
public class PlayResourceTest {

    @Transactional
    public void AddLobby(String token) {
        // Create and init lobby
        LobbyDB lobby = new LobbyDB();
        lobby.token = token;
        for (int i = 0; i < 20; i++) {
            lobby.sumScores.add(0);
            lobby.numberVotes.add(0);
        }
        // Add lobby dans la DB
        LobbyDB.persist(lobby);
    }

    @Transactional
    public void AddUser(String token, String userID0, String userID1, String userID2) {
        // Create players
        for (Integer i = 0; i < 3; i++) {
            UserInLobbyDB player = new UserInLobbyDB();
            player.token = token;
            if(i == 0) {
                player.userID = userID0;
            }
            if(i == 1) {
                player.userID = userID1;
            }
            if(i == 2) {
                player.userID = userID2;
            }
            
            // add player to lobby
            UserInLobbyDB.persist(player);
        }
    }

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

        String token = "ABVDC8";

        // Le lobby est déjà créé, vérifier que les joueurs y soient
        List<UserInLobbyDB> UIL = UserInLobbyDB.list("token", token);
        for (UserInLobbyDB user : UIL) {
            // Vérifier le token des users
            assertEquals("ABVDC8", user.token);
            assertEquals("ABVDC8", user.getToken());
            // Vérifier que la liste les votes est vide
            assertTrue(user.votesID.isEmpty());
            assertTrue(user.getVotesID().isEmpty());
            // Vérifier les userID
            if (user.id == 1) {
                assertEquals("OwnerIDTest", user.userID);
                assertEquals("OwnerIDTest", user.getUserID());
            }
            if (user.id == 2) {
                assertEquals("JoinerIDTest1", user.userID);
                assertEquals("JoinerIDTest1", user.getUserID());
            }
            if (user.id == 3) {
                assertEquals("JoinerIDTest2", user.userID);
                assertEquals("JoinerIDTest2", user.getUserID());
            }
            if (user.id == 4) {
                assertEquals("JoinerIDTest3", user.userID);
                assertEquals("JoinerIDTest3", user.getUserID());
            }
        }
        // Le lobby est déjà créé, vérifier que les joueurs y soient
        List<LobbyDB> L = LobbyDB.list("token", token);
        for (LobbyDB lobby : L) {
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
            assertEquals(20, nb_Votes.size());
            assertEquals(20, nb_Votes_get.size());
            assertEquals(20, sum_Scores.size());
            assertEquals(20, sum_Scores_get.size());
            // Vérifier que tout soit initialisé à 0
            for (int i = 0; i < 20; i++) {
                assertEquals(0, nb_Votes.get(i));
                assertEquals(0, nb_Votes_get.get(i));
            }
        }
        // get une game déjà démarrée avec un id valide
        given().header("X-User", "OwnerIDTest").when().get("/play/gameStarted").then().statusCode(202);
        given().header("X-User", "JoinerIDTest2").when().get("/play/gameStarted").then().statusCode(202);
        // get une game déjà démarrée avec un id non valide
        given().header("X-User", "OwnerIDIDTest").when().get("/play/gameStarted").then().statusCode(404);
        given().header("X-User", "JoinerIDIDTest2").when().get("/play/gameStarted").then().statusCode(404);

        given().header("X-User", "OwnerIDTest").when().post("/play/send/15/33").then().statusCode(200);
    }

    
    @Test
    public void sendTest() {
        String token = "ABCDEF";
        AddLobby(token);
        AddUser(token, "userID0", "userID1", "userID2");
        // Add scores, userid valide
        given().header("X-User", "userID0").when().post("/play/send/15/33").then().statusCode(200);
        given().header("X-User", "userID1").when().post("/play/send/15/33").then().statusCode(200);
        given().header("X-User", "userID2").when().post("/play/send/15/33").then().statusCode(200);
        // Add scores, userid non valide
        given().header("X-User", "userNonValide").when().post("/play/send/15/33").then().statusCode(401);
        given().header("X-User", "").when().post("/play/send/15/33").then().statusCode(401);
        // Add scores pour film déjà voté
        given().header("X-User", "userID0").when().post("/play/send/15/33").then().statusCode(401);
        given().header("X-User", "userID1").when().post("/play/send/15/33").then().statusCode(401);
        given().header("X-User", "userID2").when().post("/play/send/15/33").then().statusCode(401);
        // Add scores pour un autre film
        given().header("X-User", "userID0").when().post("/play/send/14/66").then().statusCode(200);
        given().header("X-User", "userID1").when().post("/play/send/14/66").then().statusCode(200);
        given().header("X-User", "userID2").when().post("/play/send/14/66").then().statusCode(200);
        // Add scores pour un film non valide
        given().header("X-User", "userID0").when().post("/play/send/-9/33").then().statusCode(400);
        given().header("X-User", "userID1").when().post("/play/send/5.5/33").then().statusCode(404);
        given().header("X-User", "userID1").when().post("/play/send/abc/33").then().statusCode(404);
        given().header("X-User", "userID2").when().post("/play/send/20/33").then().statusCode(400);
        // Add scores avec un score non valide
        given().header("X-User", "userID0").when().post("/play/send/13/-140").then().statusCode(400);
        given().header("X-User", "userID1").when().post("/play/send/12/22.5").then().statusCode(404);
        given().header("X-User", "userID1").when().post("/play/send/13/abc").then().statusCode(404);
        given().header("X-User", "userID2").when().post("/play/send/13/101").then().statusCode(400); 
    }

    @Test 
    public void maxVotes() {
        String token = "FEDCBA";
        AddLobby(token);
        AddUser(token, "userID3", "userID4", "userID5");
        
        // Saturer les votes pour un user
        given().header("X-User", "userID4").when().post("/play/send/0/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/1/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/2/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/3/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/4/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/5/28").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/6/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/7/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/8/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/9/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/10/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/11/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/12/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/13/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/14/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/15/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/16/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/17/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/18/33").then().statusCode(200);
        given().header("X-User", "userID4").when().post("/play/send/19/33").then().statusCode(200);
        // Add le vote de trop...
        given().header("X-User", "userID4").when().post("/play/send/6/33").then().statusCode(401);
    }


    @Test
    public void QuitTest() {
        String token = "JDHTZG";
        AddLobby(token);
        AddUser(token, "userID6", "userID7", "userID8");
        // Un joueur quitte la partie
        given().header("X-User", "userID7").when().delete("/play/quit").then().statusCode(200);
        // send un score avec le user removed ?
        given().header("X-User", "userID7").when().post("/play/send/4/21").then().statusCode(401);
        // Re remove le même user
        given().header("X-User", "userID7").when().delete("/play/quit").then().statusCode(204);
        // remove un user non enregistré
        given().header("X-User", "sfuzhgfr54f").when().delete("/play/quit").then().statusCode(204);
    }
    
    @Test
    public void ResultTest() {
        String token = "QWERTZ";
        AddLobby(token);
        AddUser(token, "userID9", "userID10", "userID11");

        // essayer de résupérer le résultat du lobby
        given().header("X-User", "userID9").when().get("/play/getResult").then().statusCode(204);
        given().header("X-User", "userID10").when().get("/play/getResult").then().statusCode(204);
        given().header("X-User", "userID11").when().get("/play/getResult").then().statusCode(204);
        // essayer de récupérer le résultat d'un user non enregistré
        given().header("X-User", "kjnhijbg7r6tdrtz").when().get("/play/getResult").then().statusCode(400);

        // joueur 11 a voté pour tous les films
        given().header("X-User", "userID11").when().post("/play/send/0/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/1/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/2/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/3/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/4/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/5/28").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/6/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/7/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/8/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/9/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/10/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/11/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/12/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/13/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/14/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/15/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/16/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/17/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/18/33").then().statusCode(200);
        given().header("X-User", "userID11").when().post("/play/send/19/33").then().statusCode(200);

        // essayer de résupérer le résultat du lobby
        given().header("X-User", "userID10").when().get("/play/getResult").then().statusCode(204);

        // joueur 10 a voté pour tous les films
        given().header("X-User", "userID10").when().post("/play/send/0/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/1/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/2/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/3/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/4/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/5/28").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/6/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/7/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/8/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/9/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/10/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/11/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/12/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/13/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/14/34").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/15/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/16/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/17/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/18/33").then().statusCode(200);
        given().header("X-User", "userID10").when().post("/play/send/19/33").then().statusCode(200);

        // essayer de résupérer le résultat du lobby
        given().header("X-User", "userID10").when().get("/play/getResult").then().statusCode(204);

        // joueur 9 joue partiellement
        given().header("X-User", "userID9").when().post("/play/send/0/33").then().statusCode(200);
        given().header("X-User", "userID9").when().post("/play/send/1/33").then().statusCode(200);
        given().header("X-User", "userID9").when().post("/play/send/2/33").then().statusCode(200);
        given().header("X-User", "userID9").when().post("/play/send/3/33").then().statusCode(200);

        // joueur 9 quitte subitement !
        given().header("X-User", "userID9").when().delete("/play/quit").then().statusCode(200);

        // get result avec user qui a quitté
        given().header("X-User", "userID9").when().get("/play/getResult").then().statusCode(400);

        // get le result de la partie
        given().header("X-User", "userID10").when().get("/play/getResult").then().statusCode(200).body(is("14"));
        given().header("X-User", "userID11").when().get("/play/getResult").then().statusCode(200).body(is("14"));

        // re get le résultat pour un joueur
        given().header("X-User", "userID10").when().get("/play/getResult").then().statusCode(400);

        // Le lobby est supprimé à présent donc on devrait pourvoir le recréer sans erreur
        AddLobby(token);
        AddUser(token, "userID12", "userID13", "userID14");
    }  
}