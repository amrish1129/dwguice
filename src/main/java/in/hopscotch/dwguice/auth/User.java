package in.hopscotch.dwguice.auth;

import java.security.Principal;

public class User implements Principal {
	String userName;
	String password;
	String role;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
