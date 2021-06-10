package ch.unige.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class UserInLobbyDB extends PanacheEntity{
    
    public String token;
    public String userID;
    public Integer result = -1;

    static final String tokenString = "token";

    @Column(length = 1024)
    public ArrayList<Integer> votesID = new ArrayList<Integer>(); 

    public static UserInLobbyDB getUser(String userID) {
        return find("userID", userID).firstResult();
    }

    public static UserInLobbyDB getUserFromToken(String token) {
        return find(tokenString, token).firstResult();
    }
    
    public static boolean getStatus(String token) {
        List<UserInLobbyDB> users = UserInLobbyDB.list(tokenString, token);
        for(UserInLobbyDB user : users) {
            if(user.votesID.size() != 20) {
                return false;
            }
        }
        return true;
    }

    public static void writeResultToDB(Integer movieWinnerID, String token) {
        List<UserInLobbyDB> users = find(tokenString, token).list();
        for(UserInLobbyDB user : users) {
            user.result = movieWinnerID;
        }
    }

    public static void deleteUser(String userID) {
        UserInLobbyDB.delete("userID", userID);
    }
}