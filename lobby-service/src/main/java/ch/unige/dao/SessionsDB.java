package ch.unige.dao;

import ch.unige.domain.Session;


import java.security.SecureRandom;
import java.util.ArrayList;


public class SessionsDB {

    private static SessionsDB instance;
    private static ArrayList<Session> session_db = new ArrayList();

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static SecureRandom rnd = new SecureRandom();
    static int token_len = 6;


    public static SessionsDB getInstance(){
        if(instance == null){
            synchronized (SessionsDB.class) {
                if(instance == null){
                    instance = new SessionsDB();
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

    public boolean sessionExist(String token){
        return session_db.stream().map(Session::getToken).filter(token::equals).findFirst().isPresent();
    }

    public int sessionSize(String token) {
        for (int i = 0; i < session_db.size(); i++) {
            if (session_db.get(i).getToken().equals(token)) {
                return session_db.get(i).getNbMax();
            }
        }
        return -1;
    }

    public synchronized void add_session(Session session_to_add){
        session_db.add(session_to_add);
    }

    public static int getSessionDB_size(){
        return session_db.size();
    }

    public static ArrayList<Session> getFullDB(){
        return session_db;
    }

    public static void main(String[] args) {
        String test = randomString();
        System.out.println(test + " longueur est: " + test.length());
    }


}

