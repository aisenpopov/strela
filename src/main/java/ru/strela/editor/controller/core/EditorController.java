package ru.strela.editor.controller.core;

import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.strela.config.ProjectConfiguration;
import ru.strela.editor.menu.EditorMenuBar;
import ru.strela.editor.menu.MenuItem;
import ru.strela.model.Athlete;
import ru.strela.model.City;
import ru.strela.model.Country;
import ru.strela.model.RegistrationRegion;
import ru.strela.model.auth.Person;
import ru.strela.service.ApplicationService;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;
import ru.strela.util.PagerUtils;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.AjaxUpdater;

public abstract class EditorController extends AjaxUpdater {
	
	protected static final String FIELD_REQUIRED = "Необходимо заполнить поле";
	
	@Autowired
	protected ApplicationService applicationService; 
    
	@Autowired
	protected PersonServer personServer;
	
	@Autowired
	protected PersonService personService;
	
    @Autowired
    protected ProjectConfiguration projectConfiguration;
	
	@Autowired
	private EditorMenuBar editorMenuBar;
	
	protected class CustomEditorSupport extends PropertyEditorSupport {
		
		private String fieldName;
		
		public CustomEditorSupport(String fieldName) {
			super();
			this.fieldName = fieldName;
		}
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if("country".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new Country(TextUtils.getIntValue(text))));
				}
			} else if("person".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(personService.findById(new Person(TextUtils.getIntValue(text))));
				}
			} else if("registrationRegion".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new RegistrationRegion(TextUtils.getIntValue(text))));
				}
			} else if("city".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new City(TextUtils.getIntValue(text))));
				}
			} else if("athlete".equals(fieldName) || "chiefInstructor".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(personService.findById(new Athlete(TextUtils.getIntValue(text))));
				}
			}
		}

		@Override
		public String getAsText() {
			if("country".equals(fieldName)) {
				Country country = (Country)getValue();
				if(country != null) {
					return String.valueOf(country.getId());
				}
			} else if("person".equals(fieldName)) {
				Person person = (Person)getValue();
				if(person != null) {
					return String.valueOf(person.getId());
				}
			} else if("registrationRegion".equals(fieldName)) {
				RegistrationRegion registrationRegion = (RegistrationRegion)getValue();
				if(registrationRegion != null) {
					return String.valueOf(registrationRegion.getId());
				}
			} else if("city".equals(fieldName)) {
				City city = (City)getValue();
				if(city != null) {
					return String.valueOf(city.getId());
				}
			} else if("athlete".equals(fieldName) || "chiefInstructor".equals(fieldName)) {
				Athlete athlete = (Athlete)getValue();
				if(athlete != null) {
					return String.valueOf(athlete.getId());
				}
			}
			
			return null;
		}
		
	}

    @InitBinder
    protected void initBinder(WebDataBinder binder) throws Exception {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        binder.registerCustomEditor(Country.class, "country", new CustomEditorSupport("country"));
        binder.registerCustomEditor(Person.class, "person", new CustomEditorSupport("person"));
        binder.registerCustomEditor(City.class, "city", new CustomEditorSupport("city"));
        binder.registerCustomEditor(Athlete.class, "athlete", new CustomEditorSupport("athlete"));
        binder.registerCustomEditor(Athlete.class, "chiefInstructor", new CustomEditorSupport("chiefInstructor"));
    }
    
    @ModelAttribute("menu")
    public List<MenuItem> menu() {
        return editorMenuBar.getItems();
    }

    @ModelAttribute("activeMenu")
    public MenuItem activeMenu() {
    	String currenHref = currentHref();
    	for(MenuItem item : editorMenuBar.getItems()) {
    		if(currenHref.contains(item.getHref())) {
    			return item;
    		}
    		for(MenuItem subItem : item.getItems()) {
    			if(currenHref.contains(subItem.getHref())) {
        			return subItem;
        		}
    		}
    	}
    	return null;
    }
    
    @ModelAttribute("currentHref")
    public String currentHref() {
    	return ((ServletRequestAttributes)RequestContextHolder
				.getRequestAttributes())
				.getRequest().getRequestURI();
    }
	
	@ModelAttribute("pagerPath")
	private String getPagerPath(HttpServletRequest request) {
		return PagerUtils.getPagerPath(request);
	}
	
}