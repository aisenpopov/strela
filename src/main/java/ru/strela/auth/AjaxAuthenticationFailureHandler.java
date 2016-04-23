package ru.strela.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public AjaxAuthenticationFailureHandler() {}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if("true".equals(request.getHeader("X-Ajax"))) {
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} catch(Exception e) {}
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}
	
}
