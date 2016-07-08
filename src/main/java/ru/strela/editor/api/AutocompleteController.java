package ru.strela.editor.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.strela.model.*;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.*;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.filter.payment.CouponFilter;
import ru.strela.model.filter.payment.TariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.model.payment.Coupon;
import ru.strela.model.payment.Tariff;
import ru.strela.service.ApplicationService;
import ru.strela.service.PaymentService;
import ru.strela.service.PersonService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/autocomplete")
public class AutocompleteController {
	
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

    @Autowired
    private PaymentService paymentService;
    
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

    @RequestMapping(value = "/team/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findTeam(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        TeamFilter filter = new TeamFilter();
        filter.setQuery(q);
        for(Team team : applicationService.findTeams(filter)) {
            result.add(new ResponseItem(team.getId(), team.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/team/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getTeam(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Team team = applicationService.findById(new Team(id));
            if(team == null) continue;

            result.add(new ResponseItem(team.getId(), team.getName()));
        }

        return result;
    }

    @RequestMapping(value = "/gym/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findGym(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        GymFilter filter = new GymFilter();
        filter.setQuery(q);
        for(Gym gym : applicationService.findGyms(filter, true)) {
            result.add(new ResponseItem(gym.getId(), gym.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/gym/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getGym(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Gym gym = applicationService.findById(new Gym(id));
            if(gym == null) continue;

            result.add(new ResponseItem(gym.getId(), gym.getName()));
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
    public List<ResponseItem> findAthlete(@RequestParam(value = "q", required = false) String q, @RequestParam(value = "instructor", required = false) Boolean instructor) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        AthleteFilter filter = new AthleteFilter();
        filter.setQuery(q);
        filter.setInstructor(instructor);
        for(Athlete athlete : personService.findAthletes(filter, 0, 30)) {
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

    @RequestMapping(value = "/tariff/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findTariff(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        TariffFilter filter = new TariffFilter();
        filter.setQuery(q);
        for(Tariff tariff : paymentService.findTariffs(filter, true)) {
            result.add(new ResponseItem(tariff.getId(), tariff.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/tariff/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getTariff(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Tariff tariff = paymentService.findById(new Tariff(id));
            if(tariff == null) continue;

            result.add(new ResponseItem(tariff.getId(), tariff.getName()));
        }

        return result;
    }

    @RequestMapping(value = "/coupon/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findCoupon(@RequestParam(value = "q", required = false) String q) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        CouponFilter filter = new CouponFilter();
        filter.setQuery(q);
        for(Coupon coupon : paymentService.findCoupons(filter)) {
            result.add(new ResponseItem(coupon.getId(), coupon.getName()));
        }
        return result;
    }

    @RequestMapping(value = "/coupon/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getCoupon(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            Coupon coupon = paymentService.findById(new Coupon(id));
            if(coupon == null) continue;

            result.add(new ResponseItem(coupon.getId(), coupon.getName()));
        }

        return result;
    }

    @RequestMapping(value = "/athleteTariff/find", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> findAthleteTariff(@RequestParam(value = "q", required = false) String q,
                                                @RequestParam(value = "athleteId", required = false) Integer athleteId) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();
        AthleteTariffFilter filter = new AthleteTariffFilter();
        filter.setQuery(q);
        if (athleteId != null) {
            filter.setAthlete(new Athlete(athleteId));
        }
        for (AthleteTariff athleteTariff : paymentService.findAthleteTariffs(filter, true)) {
            result.add(new ResponseItem(athleteTariff.getId(), athleteTariff.getDisplayName()));
        }
        return result;
    }

    @RequestMapping(value = "/athleteTariff/get", method = RequestMethod.POST)
    @ResponseBody
    public List<ResponseItem> getAthleteTariff(@RequestParam(value = "ids[]") Integer[] ids) {
        List<ResponseItem> result = new ArrayList<ResponseItem>();

        for(Integer id : ids) {
            if(id == null) continue;

            AthleteTariff athleteTariff = paymentService.findById(new AthleteTariff(id));
            if(athleteTariff == null) continue;

            result.add(new ResponseItem(athleteTariff.getId(), athleteTariff.getDisplayName()));
        }

        return result;
    }
    
}
