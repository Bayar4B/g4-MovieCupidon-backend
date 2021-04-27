package ch.unige.dao;

import java.util.ArrayList;

import ch.unige.domain.User_in_lobby;

public class User_in_lobby_DB {
    private static User_in_lobby_DB instance;
    private static ArrayList<User_in_lobby> user_in_lobby = new ArrayList();
    private int next_user_lobby_id;

    public User_in_lobby_DB() {
        this.set_next_association_user_lobby(0);
    }

    public static User_in_lobby_DB getInstance() {
        if (instance == null) {
            synchronized (User_in_lobby_DB.class) {
                if (instance == null) {
                    instance = new User_in_lobby_DB();
                }
            }
        }
        return instance;
    }

    public static int getNew_User_in_lobby_id() {
    	int id = User_in_lobby_DB.getInstance().getNext_user_lobby_id();
        User_in_lobby_DB.getInstance().incrementUser_lobby_id();
        return id;
    }

    public synchronized void add_User_in_lobby(User_in_lobby association){
        user_in_lobby.add(association);
    }

    public int getUser_in_lobby_DB_size(){
        return user_in_lobby.size();
    }

    public ArrayList<User_in_lobby> getFullUser_in_lobby_DB(){
        return user_in_lobby;
    }

    public int getNext_user_lobby_id() {
		return next_user_lobby_id;
	}   

    public void set_next_association_user_lobby(int next_user_lobby_id) {
        this.next_user_lobby_id = next_user_lobby_id;
    }

    public void incrementUser_lobby_id(){
        this.next_user_lobby_id++;
    }
}
