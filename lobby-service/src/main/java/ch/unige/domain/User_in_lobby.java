package ch.unige.domain;

public class User_in_lobby {
    private User user;
    private Session lobby;

    public User_in_lobby(User user, Session lobby){
        this.user = user;
        this.lobby = lobby;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public Session getLobby(){
        return lobby;
    }

    public void setLobby(Session lobby){
        this.lobby = lobby;
    }
}
