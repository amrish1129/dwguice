package in.hopscotch.dwguice.auth;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;

import io.dropwizard.auth.AuthFilter;

public class SecurityFilter extends AuthFilter<UserCredentials, User>{
	private UserAuthenticator userAuth;
	public SecurityFilter(UserAuthenticator userAuth) {
		this.userAuth = userAuth;
	}
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("In Security");
	}

}
