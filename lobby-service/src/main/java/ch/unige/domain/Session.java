package ch.unige.domain;

import ch.unige.dao.SessionsDB;


public class Session {

    private String token; //session-id
    private String user_creator;


    public Session(String user_creator) {
        this.user_creator = user_creator;
        SessionsDB session_db = SessionsDB.getInstance();

        this.token = session_db.getNewToken();
    }

    public String getToken() {
        return token;
    }

    public String getUser_creator() {
        return user_creator;
    }

    @Override
    public String toString() {
        return "UC: " + user_creator + " token: " + token;
    }
}
