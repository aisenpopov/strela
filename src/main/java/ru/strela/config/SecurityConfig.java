package ru.strela.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import ru.strela.auth.AjaxAuthenticationFailureHandler;
import ru.strela.auth.AjaxAuthenticationSuccessHandler;
import ru.strela.auth.AjaxLogoutSuccessHandler;
import ru.strela.auth.AuthenticationProcessor;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;

@Configuration
@EnableWebMvcSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private PersonService personService;
	
	@Autowired
	private PersonServer personServer;

	@Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private TokenBasedRememberMeServices rememberMeServices;
    
    @Autowired
    private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler = new AjaxAuthenticationFailureHandler();
    	ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/editor/login?error=true");
    	
    	AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler = new AjaxLogoutSuccessHandler();
    	ajaxLogoutSuccessHandler.setDefaultTargetUrl("/editor/login");
    	
        http
	        .authenticationProvider(rememberMeAuthenticationProvider)
	        	.rememberMe()
	        	.rememberMeServices(rememberMeServices)
	        .and()
	        .csrf().disable()
	        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/editor/logout"))
	        	.logoutSuccessHandler(ajaxLogoutSuccessHandler)
	        .and()
	        .formLogin().loginPage("/editor/login").permitAll()
	        	.loginProcessingUrl("/sign_in")
	        	.usernameParameter("username")
	        	.passwordParameter("password")
	        	.successHandler(new AjaxAuthenticationSuccessHandler())
	        	.failureHandler(ajaxAuthenticationFailureHandler)
	        	.defaultSuccessUrl("/editor/", true)
	        .and()
	        .authorizeRequests()
	        	.antMatchers("/resources/**").permitAll()
	        	.antMatchers("/media/**").permitAll()
	        	.antMatchers("/editor/**").authenticated()
	        	.anyRequest().permitAll();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.userDetailsService(personServer)
    		.passwordEncoder(passwordEncoder);
    }
    
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Override
    @Bean 
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	 return super.authenticationManagerBean();
    }
    
    @Bean
    public TokenBasedRememberMeServices getRememberMeServices() {
    	TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("rememberMe", personServer);
    	rememberMeServices.setParameter("remember");
    	rememberMeServices.setTokenValiditySeconds(2419200);
    	rememberMeServices.setCookieName("_UID_");
    	return rememberMeServices;
    }
    
    @Bean
    public RememberMeAuthenticationProvider getRememberMeAuthenticationProvider() {
    	return new RememberMeAuthenticationProvider("rememberMe");
    }
    
    @Bean
    public AuthenticationProcessor getAuthenticationProcessor() {
    	AuthenticationProcessor authenticationProcessor = new AuthenticationProcessor();
    	authenticationProcessor.setRememberMeServices(rememberMeServices);
    	authenticationProcessor.setAuthenticationManager(authenticationManager);
    	return authenticationProcessor;
    }
}
