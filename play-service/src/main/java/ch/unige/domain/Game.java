package ch.unige.domain;

import java.util.ArrayList;

public class Game {
    private String token;
    //* ArrayList indexée de 0-19 contenant les scores des 20 films
    private ArrayList<Integer> scores = new ArrayList<Integer>();  
    //* ArrayList indexée de 0-19 contenant le nombre de votants par film  
    private ArrayList<Integer> numberOfScores = new ArrayList<Integer>(); 

    public Game(String token, int movie_id, int score) {
        this.token = token;
        //! Taille du sample hardcodée à 20 films
        int sample_size = 20;
        initArrayList(sample_size);
        /**
         * Nouvelle instance de Game avec le premier push du score d'un
         * joueur par conséquent il s'agit forcément du premier film
         * de sample selection. On attribue à ce premier film l'indice 
         * 0 de la liste.
         */
        //* Incérment le score du premier film
        scores.set(movie_id, score);
        //* Incrément le nombre de personne ayant voté pour le premier film
        incrNumberOfScores(movie_id);
    }

    //* Incrémente le nombre de votant pour un id de film donné
    public void incrNumberOfScores(int index) {
        int count = numberOfScores.get(index);
        numberOfScores.set(index, count+1);
    }

    //* Incrémente le score du film pour un id de film donné
    public void incrScores(int index, int score) {
        int currentScore = scores.get(index);
        scores.set(index, currentScore + score);
    }

    //* Ajouter un nouveau score
    public void addScore(int movie_id, int score) {
        incrScores(movie_id, score);
        incrNumberOfScores(movie_id);
    }

    //* Init les arrayList avec size et des 0
    public void initArrayList(int sample_size) {
        /**
         * scores :
         * Init la liste avec la bonne taille ainsi que des scores de 0.
         * L'idée est de ensuite accéder aux cellules indexées par les
         * movie_id est d'incrémenter les valeurs.
         * 
         * numberOfScores :
         * Init la liste avec la bonne taille ainsi que des scores de 0. 
         * Cette liste compte le nombre de joueurs qui votent pour chaque
         * film. Par exemple, si pour le film indexé 7 nous avons 4 dans
         * cette liste, cela signifie que 4 joueurs ont donné un vote pour
         * le film indexé 7. 
         */
        for (int i = 0; i < 20; i++) {
            scores.add(0);
            numberOfScores.add(0);
        }
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Integer> getScores() {
        return this.scores;
    }

    public void setScores(ArrayList<Integer> scores) {
        this.scores = scores;
    }

    public ArrayList<Integer> getNumberOfScores() {
        return this.numberOfScores;
    }

    public void setNumberOfScores(ArrayList<Integer> numberOfScores) {
        this.numberOfScores = numberOfScores;
    }
    
}
