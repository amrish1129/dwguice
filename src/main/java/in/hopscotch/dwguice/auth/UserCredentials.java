package in.hopscotch.dwguice.auth;

import java.util.UUID;

public class UserCredentials {
	private UUID token;
	private String userId;
	
	public UUID getToken() {
		return token;
	}
	public void setToken(UUID token) {
		this.token = token;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
