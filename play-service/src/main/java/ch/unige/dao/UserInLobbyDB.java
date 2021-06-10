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

    @Column(length = 1024)
    public ArrayList<Integer> votesID = new ArrayList<Integer>(); 

    public static UserInLobbyDB getUser(String userID) {
        return find("userID", userID).firstResult();
    }

    public static UserInLobbyDB getUserFromToken(String token) {
        return find("token", token).firstResult();
    }
    
    public static boolean getStatus(String token) {
        List<UserInLobbyDB> users = UserInLobbyDB.list("token", token);
        for(UserInLobbyDB user : users) {
            if(user.votesID.size() != 20) {
                return false;
            }
        }
        return true;
    }

    public static void writeResultToDB(Integer movie_winner_id, String token) {
        List<UserInLobbyDB> users = UserInLobbyDB.list("token", token);
        for(UserInLobbyDB user : users) {
            user.result = movie_winner_id;
        }
    }

    public static void deleteUsers(String token) {
        UserInLobbyDB.delete("token", token);
    }

    public static void deleteUser(String userID) {
        UserInLobbyDB.delete("userID", userID);
    }
}