package ch.unige.domain;

import ch.unige.dao.SessionsDB;


public class Session {

    private String token; //session-id
    private int creator_user_id;
    private int session_status;
    private int chat_id;

    public Session(int user_id) {
        this.creator_user_id = user_id;
        SessionsDB session_db = SessionsDB.getInstance();

        this.token = session_db.getNewToken();
    }

    public String getToken() {
        return token;
    }

    public int getUser_creator() {
        return creator_user_id;
    }

    @Override
    public String toString() {
        return "UC: " + creator_user_id + " token: " + token;
    }
}
