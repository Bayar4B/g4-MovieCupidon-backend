package ch.unige.domain;

import ch.unige.dao.SessionsDB;


public class Session {

    private String token; //session-id
    private int creator_user_id;
    private String session_status;
    private int chat_id;
    private int nbMax;

    public Session(int user_id) {
        this.setCreator_user_id(user_id);
        SessionsDB session_db = SessionsDB.getInstance();
        this.setToken(session_db.getNewToken());
        this.setSession_status("Preparation - Lobby");
        this.nbMax = 5;
    }

    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
    	this.token = token;
    }

    public int getCreator_user_id() {
		return creator_user_id;
	}

	public void setCreator_user_id(int creator_user_id) {
		this.creator_user_id = creator_user_id;
	}
    public String getSession_status() {
		return session_status;
	}

	public void setSession_status(String session_status) {
		this.session_status = session_status;
	}

	public int getChat_id() {
		return chat_id;
	}

	public void setChat_id(int chat_id) {
		this.chat_id = chat_id;
	}
    @Override
    public String toString() {
        return "UC: " + getCreator_user_id() + " token: " + token;
    }

    public int getNbMax() {
        return nbMax;
    }
}
