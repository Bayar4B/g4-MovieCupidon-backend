package ch.unige.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class User {
	
    @NotBlank
    @NotEmpty(message = "Name may not be empty")
	private String username;
    
    private int user_id;
	
	public User(String username) {
		this.setUsername(username);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}