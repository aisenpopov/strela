package ru.strela.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	public AjaxAuthenticationSuccessHandler() {}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if("true".equals(request.getHeader("X-Ajax"))) {
			try {
				response.getWriter().print("success");
				response.getWriter().flush();
			} catch(IOException e) {}
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
	
}
