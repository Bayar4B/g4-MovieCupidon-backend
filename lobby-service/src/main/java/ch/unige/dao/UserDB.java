package ch.unige.dao;

import java.util.ArrayList;

import ch.unige.domain.User;

public class UserDB {
	private static UserDB instance;
    private static ArrayList<User> user_db = new ArrayList();
    private int next_user_id;
    
    public UserDB() {
    	this.setNext_user_id(0);
    }
    
    public static UserDB getInstance(){
        if(instance == null){
            synchronized (UserDB.class) {
                if(instance == null){
                    instance = new UserDB();
                }
            }
        }
        return instance;
    }
    
    public static int getNewUserID() {
    	int id = UserDB.getInstance().getNext_user_id();
    	UserDB.getInstance().incrementUserID();
    	return id;
    }
    
    public synchronized void add_user(User newUser){
        this.user_db.add(newUser);
    }

    public int getUserDB_size(){
        return this.user_db.size();
    }

    public  ArrayList<User> getFullUserDB(){
        return this.user_db;
    }

	public int getNext_user_id() {
		return next_user_id;
	}

	public void setNext_user_id(int next_user_id) {
		this.next_user_id = next_user_id;
	}
	
	public void incrementUserID() {
		this.next_user_id++;
	}
}
