package ru.strela.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import ru.strela.util.ajax.AjaxUpdateFilter;

public class WebApp implements WebApplicationInitializer {

    private static final String DISPATCHER_SERVLET_NAME = "dispatcher";
    private static final String DISPATCHER_SERVLET_MAPPING = "/";

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
	    // to select profile use -Dspring.profiles.default=profile_name
	    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	    rootContext.register(PropertySourcesConfig.class);
	    rootContext.register(ApplicationConfig.class);
	    rootContext.register(WebApplicationConfig.class);
	    rootContext.register(SecurityConfig.class);
	    rootContext.register(EditorMenuConfig.class);
	
	    ServletRegistration.Dynamic dispatcher = servletContext.addServlet(DISPATCHER_SERVLET_NAME, new DispatcherServlet(rootContext));
	    dispatcher.setLoadOnStartup(1);
	    dispatcher.setAsyncSupported(true);
	    dispatcher.addMapping(DISPATCHER_SERVLET_MAPPING);
	    
	    FilterRegistration.Dynamic ajaxFilter = servletContext.addFilter("ajaxUpdateFilter", new AjaxUpdateFilter());
	    ajaxFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");
	    
        FilterRegistration.Dynamic securityFilter = servletContext.addFilter(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, DelegatingFilterProxy.class);
        securityFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");

	    FilterRegistration.Dynamic encodingFilter = servletContext.addFilter("encodingFilter", new CharacterEncodingFilter());
	    encodingFilter.setInitParameter("encoding", "UTF-8");
	    encodingFilter.setInitParameter("forceEncoding", "true");
	    encodingFilter.addMappingForUrlPatterns(null, true, "/*");
	    encodingFilter.setAsyncSupported(true);
	    
	    servletContext.addListener(new ContextLoaderListener(rootContext));
    }
}
