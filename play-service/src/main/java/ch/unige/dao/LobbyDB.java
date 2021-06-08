package ch.unige.dao;

import java.util.ArrayList;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class LobbyDB extends PanacheEntity{
    
    public String token;
    public ArrayList<Integer> sumScores = new ArrayList<Integer>();
    public ArrayList<Integer> numberVotes = new ArrayList<Integer>();

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<Integer> getSumScores() {
        return this.sumScores;
    }

    public void setSumScores(ArrayList<Integer> sumScores) {
        this.sumScores = sumScores;
    }

    public ArrayList<Integer> getNumberVotes() {
        return this.numberVotes;
    }

    public void setNumberVotes(ArrayList<Integer> numberVotes) {
        this.numberVotes = numberVotes;
    }

    public static LobbyDB getLobby(String token) {
        return find("token", token).firstResult();
    }

    public static int getMovieWinner(String token) {
        LobbyDB L = getLobby(token);
        ArrayList<Float> resultList = new ArrayList<Float>();
        for (int i = 0; i < 20; i++){
            if(L.getNumberVotes().get(i) != 0){
                resultList.add((float) ((float) L.getSumScores().get(i)/ (float) L.getNumberVotes().get(i)));
            }
            else {
                resultList.add((float) 0);
            }
        }
        return calcMax(resultList);
    }

    // Calcul du maximum dans une arrayList de moyennes de scores
    public static int calcMax(ArrayList<Float> resultList) {
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

    public static void deleteLobby(String token) {
        LobbyDB L = getLobby(token);
        L.delete();
    }
}
