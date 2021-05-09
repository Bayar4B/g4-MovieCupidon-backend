package ch.unige.dao;

import ch.unige.domain.Lobby;


import java.security.SecureRandom;
import java.util.ArrayList;


public class LobbyDB {

    private static LobbyDB instance;
    private static ArrayList<Lobby> lobby_db = new ArrayList();

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    static int token_len = 6;


    public static LobbyDB getInstance(){
        if(instance == null){
            synchronized (LobbyDB.class) {
                if(instance == null){
                    instance = new LobbyDB();
                }
            }
        }
        return instance;
    }

    private static String randomString(){
        StringBuilder sb = new StringBuilder(token_len);
        for(int i = 0; i < token_len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public String getNewToken() {
        /*
        TODO
            Verifier que le token generer ne se trouve pas deja dans la DB. MySQL creera tout seul un token unique je pense
         */
        return randomString();
    }

    public boolean lobbyExist(String token){
        return lobby_db.stream().map(Lobby::getToken).filter(token::equals).findFirst().isPresent();
    }

    public int lobbySize(String token) {
        for (int i = 0; i < lobby_db.size(); i++) {
            if (lobby_db.get(i).getToken().equals(token)) {
                return lobby_db.get(i).getNbMax();
            }
        }
        return -1;
    }

    public synchronized void add_lobby(Lobby lobby_to_add){
        lobby_db.add(lobby_to_add);
    }

    public static int getlobbyDB_size(){
        return lobby_db.size();
    }

    public static ArrayList<Lobby> getFullDB(){
        return lobby_db;
    }

    public static void main(String[] args) {
        String test = randomString();
        System.out.println(test + " longueur est: " + test.length());
    }


}

