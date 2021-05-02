package ch.unige.domain;

public class UserInLobby {
    private User user;
    private String lobby;
    private int readyOrNot; //0 for ready, 1 for ready

    public UserInLobby(User user, String lobby){
        this.user = user;
        this.lobby = lobby;
        this.setReadyOrNot(0);
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getLobby(){
        return lobby;
    }

    public void setLobby(String lobby){
        this.lobby = lobby;
    }

	public int getReadyOrNot() {
		return readyOrNot;
	}

	public void setReadyOrNot(int readyOrNot) {
		this.readyOrNot = readyOrNot;
	}
}
