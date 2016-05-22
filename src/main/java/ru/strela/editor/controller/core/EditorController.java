package ru.strela.editor.controller.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.strela.config.ProjectConfiguration;
import ru.strela.editor.menu.EditorMenuBuilder;
import ru.strela.editor.menu.MenuItem;
import ru.strela.model.*;
import ru.strela.model.auth.Person;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.Tariff;
import ru.strela.service.ApplicationService;
import ru.strela.service.PaymentService;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;
import ru.strela.util.AutocompleteHelper;
import ru.strela.util.PagerUtils;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.AjaxUpdater;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class EditorController extends AjaxUpdater {
	
	protected static final String FIELD_REQUIRED = "Необходимо заполнить поле";

	protected static final String POSITIVE_NUMBER = "Значение должно быть больше нуля";
	
	@Autowired
	protected ApplicationService applicationService; 
    
	@Autowired
	protected PersonServer personServer;
	
	@Autowired
	protected PersonService personService;

	@Autowired
	protected PaymentService paymentService;
	
    @Autowired
    protected ProjectConfiguration projectConfiguration;
	
	@Autowired
	private EditorMenuBuilder editorMenuBuilder;
	
	protected class CustomEditorSupport extends PropertyEditorSupport {
		
		private String fieldName;
		
		public CustomEditorSupport(String fieldName) {
			super();
			this.fieldName = fieldName;
		}
		
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if ("country".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new Country(TextUtils.getIntValue(text))));
				}
			} else if ("person".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(personService.findById(new Person(TextUtils.getIntValue(text))));
				}
			} else if ("registrationRegion".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new RegistrationRegion(TextUtils.getIntValue(text))));
				}
			} else if ("city".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new City(TextUtils.getIntValue(text))));
				}
			} else if ("athlete".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(personService.findById(new Athlete(TextUtils.getIntValue(text))));
				}
			} else if ("athletes".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					List<Athlete> athletes = new ArrayList<Athlete>();
					for(Integer id: AutocompleteHelper.parseMultipleValue(text)) {
						Athlete athlete = personService.findById(new Athlete(id));
						athletes.add(athlete);
					}
					setValue(athletes);
				}
			} else if("team".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new Team(TextUtils.getIntValue(text))));
				}
			} else if ("gym".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(applicationService.findById(new Gym(TextUtils.getIntValue(text))));
				}
			} else if ("tariff".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(paymentService.findById(new Tariff(TextUtils.getIntValue(text))));
				}
			} else if ("coupon".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(paymentService.findById(new Coupon(TextUtils.getIntValue(text))));
				}
			} else if ("athleteTariff".equals(fieldName)) {
				if(StringUtils.isBlank(text)) {
					setValue(null);
				} else {
					setValue(paymentService.findById(new AthleteTariff(TextUtils.getIntValue(text))));
				}
			}
		}

		@Override
		public String getAsText() {
			if ("country".equals(fieldName)) {
				Country country = (Country)getValue();
				if(country != null) {
					return String.valueOf(country.getId());
				}
			} else if ("person".equals(fieldName)) {
				Person person = (Person)getValue();
				if(person != null) {
					return String.valueOf(person.getId());
				}
			} else if ("registrationRegion".equals(fieldName)) {
				RegistrationRegion registrationRegion = (RegistrationRegion)getValue();
				if(registrationRegion != null) {
					return String.valueOf(registrationRegion.getId());
				}
			} else if ("city".equals(fieldName)) {
				City city = (City)getValue();
				if(city != null) {
					return String.valueOf(city.getId());
				}
			} else if ("athlete".equals(fieldName)) {
				Athlete athlete = (Athlete)getValue();
				if(athlete != null) {
					return String.valueOf(athlete.getId());
				}
			} else if ("athletes".equals(fieldName)) {
				AutocompleteHelper autocompleteHelper = new AutocompleteHelper();
				for(Athlete athlete : (List<Athlete>)getValue()) {
					autocompleteHelper.addValue(athlete.getId());
				}
				return autocompleteHelper.getValue();

			} else if ("team".equals(fieldName)) {
				Team team = (Team)getValue();
				if(team != null) {
					return String.valueOf(team.getId());
				}
			} else if ("gym".equals(fieldName)) {
				Gym gym = (Gym)getValue();
				if(gym != null) {
					return String.valueOf(gym.getId());
				}
			} else if ("tariff".equals(fieldName)) {
				Tariff tariff = (Tariff)getValue();
				if(tariff != null) {
					return String.valueOf(tariff.getId());
				}
			} else if ("coupon".equals(fieldName)) {
				Coupon coupon = (Coupon)getValue();
				if(coupon != null) {
					return String.valueOf(coupon.getId());
				}
			} else if ("athleteTariff".equals(fieldName)) {
				AthleteTariff athleteTariff = (AthleteTariff)getValue();
				if(athleteTariff != null) {
					return String.valueOf(athleteTariff.getId());
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
		binder.registerCustomEditor(Person.class, "operator", new CustomEditorSupport("person"));
        binder.registerCustomEditor(City.class, "city", new CustomEditorSupport("city"));
        binder.registerCustomEditor(Athlete.class, "athlete", new CustomEditorSupport("athlete"));
        binder.registerCustomEditor(Athlete.class, "chiefInstructor", new CustomEditorSupport("athlete"));
		binder.registerCustomEditor(Athlete.class, "athleteTariff.athlete", new CustomEditorSupport("athlete"));
		binder.registerCustomEditor(Team.class, "team", new CustomEditorSupport("team"));
		binder.registerCustomEditor(RegistrationRegion.class, "registrationRegion", new CustomEditorSupport("registrationRegion"));
		binder.registerCustomEditor(List.class, "instructors", new CustomEditorSupport("athletes"));
		binder.registerCustomEditor(Gym.class, "gym", new CustomEditorSupport("gym"));
		binder.registerCustomEditor(Gym.class, "athleteTariff.tariff.gym", new CustomEditorSupport("gym"));
		binder.registerCustomEditor(Coupon.class, "coupon", new CustomEditorSupport("coupon"));
		binder.registerCustomEditor(Tariff.class, "tariff", new CustomEditorSupport("tariff"));
		binder.registerCustomEditor(AthleteTariff.class, "athleteTariff", new CustomEditorSupport("athleteTariff"));
    }

	@ModelAttribute("root")
	public boolean root() {
		Person currentPerson = getCurrentPerson();
		return currentPerson != null ? currentPerson.isRoot() : false;
	}
    
    @ModelAttribute("menu")
    public List<MenuItem> menu() {
		Person currentPerson = getCurrentPerson();
        return currentPerson != null ? editorMenuBuilder.build(currentPerson.isRoot()).getItems() : null;
    }

	private Person getCurrentPerson() {
		return personServer.getCurrentPerson();
	}

    @ModelAttribute("activeMenu")
    public MenuItem activeMenu() {
    	String currentHref = currentHref();
		List<MenuItem> menuItems = menu();
		if (menuItems != null) {
			for(MenuItem item : menuItems) {
				if(currentHref.contains(item.getHref())) {
					return item;
				}
				for(MenuItem subItem : item.getItems()) {
					if(currentHref.contains(subItem.getHref())) {
						return subItem;
					}
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