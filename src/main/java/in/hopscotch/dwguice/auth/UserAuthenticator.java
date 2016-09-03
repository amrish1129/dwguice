package in.hopscotch.dwguice.auth;

import java.util.Optional;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

public class UserAuthenticator implements Authenticator<UserCredentials, User>{

	@Override
	public Optional<User> authenticate(UserCredentials credentials) throws AuthenticationException {
		User u = null;
		if (credentials.getUserId().equals("Hopscotch")) {
			u = new User();
			u.setUserName("Hopscotch");
			u.setRole("Admin");
			//Optional<User> user = Optional.of(u);
		}
		return Optional.ofNullable(u);
	}

}
