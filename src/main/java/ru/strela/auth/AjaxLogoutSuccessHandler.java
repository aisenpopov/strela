package ru.strela.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

public class AjaxLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler{
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		if("true".equals(request.getHeader("X-Ajax"))) {
			try {
				response.getWriter().print("success");
				response.getWriter().flush();
			} catch(IOException e) {}
		} else {
			super.onLogoutSuccess(request, response, authentication);
		}
	}

}
