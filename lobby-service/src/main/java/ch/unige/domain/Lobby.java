package ch.unige.domain;

import ch.unige.dao.LobbyDB;
import ch.unige.dao.UserInLobbyDB;


public class Lobby {

    private static LobbyDB lobbyDB = LobbyDB.getInstance();
    private static UserInLobbyDB userInLobbyDB = UserInLobbyDB.getInstance();



    private String token; //lobby-id
    private int creator_user_id;
    private String lobby_status;
    private int chat_id;
    private int nbMax;

    public Lobby(int user_id) {
        this.setCreator_user_id(user_id);
        LobbyDB lobby_db = LobbyDB.getInstance();
        this.setToken(lobby_db.getNewToken());
        this.setlobby_status("Preparation - Lobby");
        this.nbMax = 5;
        lobbyDB.add_lobby(this);
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
    public String getlobby_status() {
		return lobby_status;
	}

	public void setlobby_status(String lobby_status) {
		this.lobby_status = lobby_status;
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
