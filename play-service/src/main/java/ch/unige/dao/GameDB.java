package ch.unige.dao;

import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import ch.unige.domain.Game;

public class GameDB {
    private static GameDB instance;

    private static ArrayList<Game> game_DB = new ArrayList<Game>();

    public static GameDB getInstance() {
        if (instance == null) {
            synchronized (GameDB.class) {
                if (instance == null) {
                    instance = new GameDB();
                }
            }
        }
        return instance;
    }

    public void saveScores(String token, int movie_id, int score) {
        // * Vérifier si une instance de Game avec ce token existe déjà dans la GameDB
        boolean createGame = true;
        for (Game game : game_DB) {
            if (token.equals(game.getToken())) {
                /**
                 * Instance de Game déjà existante et initialisé on veut maintenant ajouter le
                 * score du joueur au film portant le movie_id. movie_id est simplement l'indice
                 * de la array de l'instance.
                 */
                game.addScore(movie_id, score);

                // * Instance de Game déjà existante donc on ne crée pas de nouvelle instance
                createGame = false;
                break;
            }
        }
        // * Crée une instance de Game si non existant
        if (createGame) {
            Game newGame = new Game(token, movie_id, score);
            game_DB.add(newGame);
        }

        //* Print les DB
        /*
        for (Game game : game_DB) {
            for (int i = 0; i < 20 ; i++) {
                System.out.print(game.getScores().get(i) + ", ");
            }
            System.out.println();
            for (int i = 0; i < 20 ; i++) {
                System.out.print(game.getNumberOfScores().get(i) + ", ");
            }
            System.out.println();
        }
        */
    }

    /**
     * getResult retourne l'id du film gagnant.
     * La méthode crée une arraylist intermédiaire contanant les moyennes des
     * scores pour chaque film, puis on sélectionne le maximum.
     */
    public int getResult(String token) {
        ArrayList<Float> resultList = new ArrayList<Float>();
        for (Game game : game_DB) {
            if (token.equals(game.getToken())) {
                for (int i = 0; i < 20; i++){
                    if(game.getNumberOfScores().get(i) != 0){
                        resultList.add((float) ((float) game.getScores().get(i)/ (float) game.getNumberOfScores().get(i)));
                    }
                    else {
                        resultList.add((float) 0);
                    }
                }
            }
        }
        //* Print result array
        /*
        for (int i = 0; i < 20 ; i++) {
            System.out.print(resultList.get(i) + ", ");
        }
        System.out.println();
        */

        //* Regarde si le token n'existe pas dans la DB
        if(resultList.isEmpty()) {
            return -1; // error code
        }

        return calcMax(resultList);
    }

    //* Calcul du maximum dans une arrayList de moyennes de scores
    public int calcMax(ArrayList<Float> resultList) {
        float max = 0;
        int maxIndex = 0;
        for (int i = 0; i < 20; i++) {
            if(resultList.get(i) > max) {
                max = resultList.get(i);
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public void deleteData(String token) {
        for (Iterator<Game> iterator = game_DB.iterator(); iterator.hasNext();) {
            Game game = iterator.next();
            if (token.equals(game.getToken())) {
                //* Delete tout ce qui se réfère à ce token
                iterator.remove();
            }
        }
    }
}
