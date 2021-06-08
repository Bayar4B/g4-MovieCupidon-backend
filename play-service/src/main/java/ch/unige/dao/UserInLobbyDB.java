package ch.unige.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
//import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
//import io.quarkus.hibernate.orm.panache.PanacheQuery;

@Entity
public class UserInLobbyDB extends PanacheEntity{
    
    public String token;
    public String userID;
    public ArrayList<Integer> votesID = new ArrayList<Integer>();

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<Integer> getVotesID() {
        return this.votesID;
    }

    public void setVotesID(ArrayList<Integer> votesID) {
        this.votesID = votesID;
    }

    public static UserInLobbyDB getUser(String userID) {
        return find("userID", userID).firstResult();
    }
    
    public static boolean getStatus(String token) {
        List<UserInLobbyDB> users = UserInLobbyDB.list("token", token);
        for(UserInLobbyDB user : users) {
            if(user.votesID.size() != 1) {
                return false;
            }
            //System.out.println(user.votesID.size());
        }
        return true;
    }

    public static void deleteUsers(String token) {
        UserInLobbyDB.delete("token", token);
    }

    public static void deleteUser(String userID) {
        UserInLobbyDB.delete("userID", userID);
    }
}