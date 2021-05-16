package ch.unige.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ch.unige.domain.Lobby;
import ch.unige.domain.UserInLobby;

public class UserInLobbyDB {
    private static UserInLobbyDB instance;
    private static ArrayList<UserInLobby> userInLobbiesDB = new ArrayList<UserInLobby>();
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
        LobbyDB lobbyDBinstance = LobbyDB.getInstance();
        int sizeOflobby = lobbyDBinstance.lobbySize(token);
        if (userInLobbiesDB.stream().map(UserInLobby::getLobbyToken).filter(token::equals).count()+1 <= sizeOflobby) {
            return true;
        }
        return false;
    }
    
    public boolean isEveryoneReady(String token) {
    	LobbyDB lobbyDBinstance = LobbyDB.getInstance();
    	
    	// Récupère le Owner id 
    	
    	List<Integer> ownerId_List = lobbyDBinstance.getFullDB().stream()
    			.filter(s -> s.getToken().equals(token))
    			.map(Lobby::getCreator_user_id).collect(Collectors.toList());
    	
    	int ownerId = ownerId_List.get(0);
    			
    	// Récupère le nombre d'untilisateur dans une lobby 
    	long nbUser = userInLobbiesDB.stream()		// Nombre de personne dans un lobby
    			.filter(s -> s.getLobbyToken().equals(token))
    			.count();
    	
    	// Verifier que toutes les personnes dans le lobby différentes du owner sont ready
    	if (userInLobbiesDB.stream()
    			.filter(s -> s.getLobbyToken().equals(token) &&
    					s.getReady_status() == true && 
    					s.getUser().getUserId() != ownerId)
    			.count() == nbUser-1)
    	{
            return true;
        }
    	return false;
    }
    
    public boolean isUserInLobby(String token, int userID) {
    	//Verifie qu'un User est bien dans le lobby souhaité
    	
    	Long listUserInLobby = userInLobbiesDB.stream()
    		.filter(l -> l.getLobbyToken().equals(token) && l.getUser().getUserId() == userID)
    		.count();
    	
    	return (listUserInLobby > 0);
    }

    public synchronized void addUserInLobby(UserInLobby association){
        userInLobbiesDB.add(association);
    }

    public synchronized boolean removeUserFromLobby(String token, int user_id) {
        for (Iterator<UserInLobby> iterator = userInLobbiesDB.iterator(); iterator.hasNext();) {
            UserInLobby association = iterator.next();
            
            // Remove le user s'il correspond au token ainsi qu'a l'id
            if (token.equals(association.getLobbyToken()) && user_id == association.getUser().getUserId()) {
                iterator.remove();
                return true;
            }
            //System.out.println(association.getUser().getUsername() + " : " + association.getUser().getUserId() + " : " + association.getLobbyToken());
        }
        return false;
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

    	int index = findUserInLobbyById(user.getUser().getUserId());
    	if(index == -1) {
    		//TODO Handling when user not found in DB...
    		return;
    	}
    	userInLobbiesDB.set(index,user);

    }
    
    public int findUserInLobbyById(int id) {
    	for (int i = 0; i < userInLobbiesDB.size(); i++) {
			if(id == userInLobbiesDB.get(i).getUser().getUserId() ) {
				return(i);		
			}
		}
    	return(-1);
    }
    
    public int findUserInLobbyById(String token) {
    	// TODO This isn't really usefull for now..
    	for (int i = 0; i < userInLobbiesDB.size(); i++) {
			if(token.equalsIgnoreCase(userInLobbiesDB.get(i).getLobbyToken())) {
				return(i);		
			}
		}
    	return(-1);
    }

	@Override
	public String toString() { 
	    String result = "";
	    for (int i = 0; i < userInLobbiesDB.size(); i++) {result += String.valueOf(i) + ") "+ String.valueOf(userInLobbiesDB.get(i)) + " \n";}
	    return result;
	} 
}