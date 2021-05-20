package ch.unige;

import org.junit.jupiter.api.Test;

import ch.unige.domain.Lobby;
import io.quarkus.test.junit.QuarkusTest;
import junit.framework.*;

@QuarkusTest
public class LobbyTest extends TestCase{
	
	@Test
	public void createLobbyTest() {
		Lobby lobby = new Lobby(11);
		assertEquals(11, lobby.getCreator_user_id());
		assertEquals("Preparation - Lobby", lobby.getlobby_status());
	}
	
	@Test
	public void setTokenIDTest() {
		Lobby lobby = new Lobby(11);
		lobby.setToken("TestToken");
		assertEquals("TestToken", lobby.getToken());
	}

	@Test
	public void setLobbyStatutTest() {
		Lobby lobby = new Lobby(11);
		lobby.setlobby_status("In Game");
		assertEquals("In Game", lobby.getlobby_status());
	}
	
	@Test
	public void setChatIDTest() {
		Lobby lobby = new Lobby(11);
		lobby.setChat_id(11);
		assertEquals(11, lobby.getChat_id());
	}
	
	@Test
	public void toStringTest() {
		Lobby lobby = new Lobby(11);
		String token = lobby.getToken();
		lobby.setToken("ABCD");
		assertEquals("UC: 11 token: ABCD", lobby.toString() );
		lobby.setToken(token);		// Sert juste a reset le token pour ne pas 
							//avoir de conflit plus tard dans les autres test qui essaye la valeur ABCD comme token
	}
	
}
