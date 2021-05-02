package ch.unige.dao;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import ch.unige.domain.Session;
import ch.unige.domain.UserInLobby;

public class UserInLobbyDB {
    private static UserInLobbyDB instance;
    private static ArrayList<UserInLobby> userInLobbiesDB = new ArrayList();
    private int nextUserLobbyId;

    public UserInLobbyDB() {
        this.setNextAssociationUserLobby(0);
    }

    public static UserInLobbyDB getInstance() {
        if (instance == null) {
            synchronized (UserInLobbyDB.class) {
                if (instance == null) {
                    instance = new UserInLobbyDB();
                }
            }
        }
        return instance;
    }

    public static int getNewUserInLobbyId() {
    	int id = UserInLobbyDB.getInstance().getNextUserLobbyId();
        UserInLobbyDB.getInstance().incrementUserLobbyId();
        return id;
    }

    public boolean isTherePlaceInLobby(String token){
        SessionsDB sessionDBinstance = SessionsDB.getInstance();
        int sizeOfSession = sessionDBinstance.sessionSize(token);
        if (userInLobbiesDB.stream().map(UserInLobby::getLobby).filter(token::equals).count()+1 <= sizeOfSession) {
            return true;
        }
        return false;
    }
    
    public boolean isEveryoneReady(String token) {
    	SessionsDB sessionDBinstance = SessionsDB.getInstance();
    	
    	long ownerId = sessionDBinstance.getFullDB().stream()
    			.filter(s -> s.getToken().equals(token))
    			.map(Session::getCreator_user_id).count();
    	
    	System.out.println("Owner ID : "+ownerId);
    			
    	long nbUser = userInLobbiesDB.stream()		// Nombre de personne dans un lobby
    			.filter(s -> s.getLobby().equals(token))
    			.count();
    	
    	System.out.println("Nb user : "+nbUser);

    	if (userInLobbiesDB.stream()
    			.filter(s -> s.getLobby().equals(token) &&
    					s.getReadyOrNot() == 1 && 
    					s.getUser().getUser_id() != ownerId)
    			.count() == nbUser-1)
    	{
    		System.out.println("Return : true");
            return true;
        }
		System.out.println("Return : false");
    	return false;
    }

    public synchronized void addUserInLobby(UserInLobby association){
        userInLobbiesDB.add(association);
    }

    public int getUserInLobbyDBSize(){
        return userInLobbiesDB.size();
    }

    public ArrayList<UserInLobby> getFullUserInLobbyDB(){
        return userInLobbiesDB;
    }

    public int getNextUserLobbyId() {
		return nextUserLobbyId;
	}   

    public void setNextAssociationUserLobby(int nextUserLobbyId) {
        this.nextUserLobbyId = nextUserLobbyId;
    }

    public void incrementUserLobbyId(){
        this.nextUserLobbyId++;
    }
}
