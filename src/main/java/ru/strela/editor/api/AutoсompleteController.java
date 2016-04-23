package ru.strela.editor.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.strela.model.Athlete;
import ru.strela.model.City;
import ru.strela.model.Country;
import ru.strela.model.RegistrationRegion;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.filter.CityFilter;
import ru.strela.model.filter.CountryFilter;
import ru.strela.model.filter.PersonFilter;
import ru.strela.service.ApplicationService;
import ru.strela.service.PersonService;

@Controller
@RequestMapping("/autocomplete")
public class AutoсompleteController {
	
	public static class ResponseItem {
		
		private long id;
		private String text;
		private String value;

        public ResponseItem(long id, String text) {
            this.id = id;
            this.text = text;
        }

        public ResponseItem(long id, String text, String value) {
            this.id = id;
            this.text = text;
            this.value = value;
        }

        public long getId() {
			return id;
		}
		
		public void setId(long id) {
			this.id = id;
		}
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

    @Autowired
    private ApplicationService applicationService;
    
    @Autowired
    private PersonService personService;
    
    @RequestMapping(value = "/country/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findCountry(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        CountryFilter filter = new CountryFilter();
        filter.setQuery(q);
        for(Country country : applicationService.findCountries(filter)) {
        	result.add(new ResponseItem(country.getId(), country.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/country/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getCountry(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Country country = applicationService.findById(new Country(id));
            if(country == null) continue;

            result.add(new ResponseItem(country.getId(), country.getName()));
        }

        return result;
    }
    
    @RequestMapping(value = "/city/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findCity(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        CityFilter filter = new CityFilter();
        filter.setQuery(q);
        for(City city : applicationService.findCities(filter)) {
        	result.add(new ResponseItem(city.getId(), city.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/city/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getCity(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            City city = applicationService.findById(new City(id));
            if(city == null) continue;

            result.add(new ResponseItem(city.getId(), city.getName()));
        }

        return result;
    }
    
    @RequestMapping(value = "/person/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findPerson(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "hasNotAthlete", required = false) Boolean hasNotAthlete) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        List<Person> persons = null;
        if(hasNotAthlete != null && hasNotAthlete) {
        	persons = personService.findHasNotAthlete(q);
        } else {
        	PersonFilter filter = new PersonFilter();
        	filter.setQuery(q);
        	persons = personService.find(filter);
        }
        for(Person person : persons) {
        	result.add(new ResponseItem(person.getId(), person.getLogin()));
        }
        return result;
    }

    @RequestMapping(value = "/person/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getPerson(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Person person = personService.findById(new Person(id));
            if(person == null) continue;

            result.add(new ResponseItem(person.getId(), person.getLogin()));
        }

        return result;
    }
    
    @RequestMapping(value = "/athlete/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findAthlete(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        AthleteFilter filter = new AthleteFilter();
        filter.setQuery(q);
        for(Athlete athlete : personService.findAthletes(filter)) {
        	result.add(new ResponseItem(athlete.getId(), athlete.getDisplayName()));
        }
        
        return result;
    }

    @RequestMapping(value = "/athlete/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getAthlete(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Athlete athlete = personService.findById(new Athlete(id));
            if(athlete == null) continue;

            result.add(new ResponseItem(athlete.getId(), athlete.getDisplayName()));
        }

        return result;
    }
    
    @RequestMapping(value = "/registration_region/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findRegistrationRegion(@RequestParam(value = "q", required = false) String q) {
    	 List<ResponseItem> result = new ArrayList<ResponseItem>();
         BaseFilter filter = new BaseFilter();
         filter.setQuery(q);
         for(RegistrationRegion registrationRegion : applicationService.findRegistrationRegions(filter)) {
         	result.add(new ResponseItem(registrationRegion.getId(), registrationRegion.getName()));
         }
         return result;
    }

    @RequestMapping(value = "/registration_region/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getRegistrationRegion(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            RegistrationRegion registrationRegion = applicationService.findById(new RegistrationRegion(id));
            if(registrationRegion == null) continue;

            result.add(new ResponseItem(registrationRegion.getId(), registrationRegion.getName()));
        }

        return result;
    }
    
}
