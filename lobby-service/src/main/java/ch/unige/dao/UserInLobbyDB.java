package ch.unige.dao;

import java.util.ArrayList;
import java.util.Iterator;

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
    
    /**
    *  Updates a user info in the UserInLobby table.
    * @param  user  a UserInLobby object, which is the user whose info we want to update.
    */
    public void updateUserInLobby(UserInLobby user) {

    	int index = findUserInLobbyById(user.getUser().getUser_id());
    	if(index == -1) {
    		//TODO Handling when user not found in DB...
    		return;
    	}
    	userInLobbiesDB.set(index,user);

    }
    
    public int findUserInLobbyById(int id) {
    	for (int i = 0; i < userInLobbiesDB.size(); i++) {
			if(id == userInLobbiesDB.get(i).getUser().getUser_id() ) {
				return(i);		
			}
		}
    	return(-1);
    }
    
    public int findLobbyInUserInLobbyDBByToken(String token) {
    	// TODO This isn't really usefull for now..
    	for (int i = 0; i < userInLobbiesDB.size(); i++) {
			if(token.equalsIgnoreCase(userInLobbiesDB.get(i).getLobby())) {
				return(i);		
			}
		}
    	return(-1);
    }

}
