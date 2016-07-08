package ru.strela.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AjaxAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	public AjaxAuthenticationFailureHandler() {}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		if("true".equals(request.getHeader("X-Ajax"))) {
			try {
				response.getWriter().print("fail");
				response.getWriter().flush();
			} catch(Exception e) {}
		} else {
			super.onAuthenticationFailure(request, response, exception);
		}
	}
	
}
