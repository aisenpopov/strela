package ru.strela.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

public class AuthenticationProcessor {
	
private AuthenticationManager authenticationManager;
    
    private AbstractRememberMeServices rememberMeServices;
    
    public boolean startSession(String userName, String password) {
    	UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);  
		Authentication authentication = authenticationManager.authenticate(token);
		if(authentication.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(authentication);  
			return true;
		} 
		return false;
    }
    
    public void startRememberMeSession(HttpServletRequest req, HttpServletResponse res, AuthPerson person) {
    	Authentication authentication = new UsernamePasswordAuthenticationToken(person, null, AuthorityUtils.NO_AUTHORITIES);
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    	((TokenBasedRememberMeServices)rememberMeServices).onLoginSuccess(req, res, authentication);
    }
    
    public void startSession(AuthPerson person) {
    	Authentication authentication = new UsernamePasswordAuthenticationToken(person, null, AuthorityUtils.NO_AUTHORITIES);
    	SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	public void setRememberMeServices(AbstractRememberMeServices rememberMeServices) {
		this.rememberMeServices = rememberMeServices;
	}
  
}
