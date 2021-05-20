package ch.unige.domain;

import ch.unige.dao.UserInLobbyDB;

public class UserInLobby {
    private User user;
    private String lobbyToken;
    private boolean ReadyStatus; //1 for ready, 0 for ready

    public UserInLobby(User user, String lobby){
        this.user = user;
        this.lobbyToken = lobby;
        this.ReadyStatus = false;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getLobbyToken(){
        return lobbyToken;
    }

    public void setLobby(String lobby){
        this.lobbyToken = lobby;
    }
    
    public boolean getReadyStatus() {
    	return this.ReadyStatus;	
    }
    
	public void setReadyStatut(boolean ready_statut) {
		this.ReadyStatus = ready_statut;
	}
	
	public boolean toggleReadyStatus() {
		/* Devrait-on ajouter un synchronized pour éviter les problèmes de concurrence..? Es-ce que synchronized est assez?*/
		
		this.ReadyStatus = !this.ReadyStatus; 
		UserInLobbyDB userInLobbyDBInstance = UserInLobbyDB.getInstance();
		userInLobbyDBInstance.updateUserInLobby(this); // Updates it's value on the database.
		return(this.ReadyStatus);
	}
	
	public boolean getReady_status() {
		return this.ReadyStatus;
	}
	

	@Override
	public String toString() { 
	    String result = "User: "+ String.valueOf(this.user) + " , lobbyId:" + this.lobbyToken + " , Ready Status: " + String.valueOf(this.ReadyStatus); 
	    return result;
	} 
    
}
