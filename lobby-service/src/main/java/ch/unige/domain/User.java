package ch.unige.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import ch.unige.dao.UserDB;


public class User {
	
	private String username;
    private int user_id;
	
	public User(String username) {
		if(this.validUsername(username)) {
			this.setUsername(username);
		}else {
			throw new BadRequestException();
		}
		
		this.user_id = UserDB.getNewUserID();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getUserId() {
		return user_id;
	}

	public void setUserID(int user_id) {
		this.user_id = user_id;
	}


	
	public boolean validUsername(String username) {
		if (username.isEmpty()) {		// Pas forcement besoin de cette partie mais permet de differencier si le username
										// est juste vide ou s'il contient uniquement des espace ou tabs
	        System.out.println("Username empty");
			return false;
		}
		
		if (username.isBlank()) {		// Test si un username contient que des espaces ou est nul
			System.out.println("Username blank");
			return false;
		}
		if (username.length() <= 2) {	// Test si le username est assez grand
			System.out.println("Username too short");
			return false;
		}

		for (int i=0; i<username.length(); i++) {	// Test si le username ne contient pas des caractère spéciaux
			if (!((username.charAt(i) >= 'a' && username.charAt(i) <= 'z')
		            || (username.charAt(i) >= 'A' && username.charAt(i) <= 'Z')
		            || (username.charAt(i) >= '0' && username.charAt(i) <= '9')
		            || username.charAt(i) == '_')) {
				return false;
			}
		}
		return true;
		
	}

	
	@Override
	public String toString() { 
	    String result = "Username: "+ this.getUsername() + " , userId:" + this.getUserId(); 
	    return result;
	} 
	
}