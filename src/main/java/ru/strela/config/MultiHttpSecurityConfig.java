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
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.strela.auth.AjaxAuthenticationFailureHandler;
import ru.strela.auth.AjaxAuthenticationSuccessHandler;
import ru.strela.auth.AjaxLogoutSuccessHandler;
import ru.strela.auth.AuthenticationProcessor;
import ru.strela.service.PersonServer;

@Configuration
@EnableWebMvcSecurity
@Order(1)
public class MultiHttpSecurityConfig {

    @Autowired
    private PersonServer personServer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personServer)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
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
        RememberMeAuthenticationProvider rememberMeAuthenticationProvider = new RememberMeAuthenticationProvider("rememberMe");
        return rememberMeAuthenticationProvider;
    }

    @Configuration
    public static class EditorWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private  TokenBasedRememberMeServices rememberMeServices;

        @Autowired
        private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
            logoutSuccessHandler.setDefaultTargetUrl("/editor/login");

            http
                .authenticationProvider(rememberMeAuthenticationProvider)
                    .rememberMe()
                    .rememberMeServices(rememberMeServices)
                .and()
                .csrf().disable()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/editor/logout"))
                    .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                .formLogin()
                    .loginPage("/editor/login").permitAll()
                    .loginProcessingUrl("/editor/sign_in")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .failureUrl("/editor/login?error=true")
                    .defaultSuccessUrl("/editor/")
                .and()
                .authorizeRequests()
                    .antMatchers("/resources/**", "/media/**").permitAll()
                    .antMatchers("/editor/banner_image/**").hasRole("ADMIN")
                    .antMatchers("/editor/registration_region/**").hasRole("ADMIN")
                    .antMatchers("/editor/country/**").hasRole("ADMIN")
                    .antMatchers("/editor/city/**").hasRole("ADMIN")
                    .antMatchers("/editor/article/news/**").hasRole("ADMIN")
                    .antMatchers("/editor/article/static_page/**").hasRole("ADMIN")
                    .antMatchers("/editor/settings/**").hasRole("ADMIN")
                    .antMatchers("/editor/person_account/**").hasRole("ADMIN")
                    .antMatchers("/editor/transaction/").hasAnyRole("ADMIN", "INSTRUCTOR")
                    .antMatchers("/editor/transaction/**").hasRole("ADMIN")
                    .antMatchers("/editor/**").hasAnyRole("ADMIN", "INSTRUCTOR")
                    .anyRequest().permitAll();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Bean
        public AuthenticationProcessor getAuthenticationProcessor() {
            AuthenticationProcessor authenticationProcessor = new AuthenticationProcessor();
            authenticationProcessor.setRememberMeServices(rememberMeServices);
            authenticationProcessor.setAuthenticationManager(authenticationManager);
            return authenticationProcessor;
        }
    }

    @Configuration
    @Order(1)
    public static class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

        @Autowired
        private  TokenBasedRememberMeServices rememberMeServices;

        @Autowired
        private RememberMeAuthenticationProvider rememberMeAuthenticationProvider;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler = new AjaxAuthenticationSuccessHandler();
            ajaxAuthenticationSuccessHandler.setDefaultTargetUrl("/account/");

            AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler = new AjaxAuthenticationFailureHandler();
            ajaxAuthenticationFailureHandler.setDefaultFailureUrl("/account/login?error=true");

            AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler = new AjaxLogoutSuccessHandler();
            ajaxLogoutSuccessHandler.setDefaultTargetUrl("/");

            http
                .antMatcher("/account/**")
                .authenticationProvider(rememberMeAuthenticationProvider)
                    .rememberMe()
                    .rememberMeServices(rememberMeServices)
                .and()
                .csrf().disable()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/account/logout"))
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                .and()
                .formLogin()
                    .loginPage("/account/login").permitAll()
                    .loginProcessingUrl("/account/sign_in").permitAll()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(ajaxAuthenticationSuccessHandler)
                    .failureHandler(ajaxAuthenticationFailureHandler)
                .and()
                .authorizeRequests()
                    .anyRequest().authenticated();
        }
    }
}
