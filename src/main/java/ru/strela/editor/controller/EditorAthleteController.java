package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Athlete;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.payment.AthleteTariffFilter;
import ru.strela.model.payment.AthleteTariff;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/editor/athlete")
public class EditorAthleteController extends EditorController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
    @RequestMapping(value = {"/"}, method = {RequestMethod.GET})
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") AthleteFilter filter) {
    	ModelBuilder model = new ModelBuilder("editor/athletes");
        if (filter == null) {
        	filter = new AthleteFilter();
        }
		if (filter.getOrders() == null || filter.getOrders().isEmpty()) {
			filter.addOrder(new Order("id", OrderDirection.Asc));
		}
        Page<Athlete> page = personService.findAthletes(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	return getModel(TextUtils.getIntValue(pathVariables.get("id")), null);
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Athlete athlete, BindingResult result) {
    	if (validate(result, athlete)) {
			Person person = athlete.getPerson();
			String newPassword = person.getPassword();
			if (person.getId() != 0) {
				Person existPerson = personService.findById(person);

				existPerson.setLogin(person.getLogin());
				existPerson.setAdmin(person.isAdmin());
				existPerson.setDisabled(person.isDisabled());

				person = existPerson;
			}
			if (StringUtils.isNotBlank(newPassword)) {
				person.setPassword(passwordEncoder.encode(newPassword));
			}

            if (athlete.getId() != 0) {
            	Athlete exist = personService.findById(new Athlete(athlete.getId()));

            	exist.setFirstName(athlete.getFirstName());
            	exist.setLastName(athlete.getLastName());
            	exist.setMiddleName(athlete.getMiddleName());
            	exist.setNickName(athlete.getNickName());
            	exist.setSex(athlete.getSex());
            	exist.setBirthday(athlete.getBirthday());
            	exist.setStartDate(athlete.getStartDate());
            	exist.setWeight(athlete.getWeight());
            	exist.setHeight(athlete.getHeight());
            	exist.setGiSize(athlete.getGiSize());
            	exist.setRashguardSize(athlete.getRashguardSize());
            	exist.setPassportNumber(athlete.getPassportNumber());
            	exist.setInstructor(athlete.isInstructor());
            	exist.setRetired(athlete.isRetired());

            	exist.setRegistrationRegion(athlete.getRegistrationRegion());
				exist.setTeam(athlete.getTeam());
            	
            	exist.setEmail(athlete.getEmail());
            	exist.setPhoneNumber(athlete.getPhoneNumber());
            	exist.setMobileNumber(athlete.getMobileNumber());
            	exist.setVk(athlete.getVk());
            	exist.setFacebook(athlete.getFacebook());
            	exist.setInstagram(athlete.getInstagram());
            	exist.setSkype(athlete.getSkype());
            	exist.setComment(athlete.getComment());

        		athlete = exist;
            }

			Person savedPerson = personService.save(person);
			athlete.setPerson(savedPerson);
            athlete = personService.save(athlete);          
            
            return new Redirect("/editor/athlete/edit/" + athlete.getId() + "/");
        }

        return new ModelAndView("editor/editAthlete");
    }

	@RequestMapping(value={"/edit/{idd}/ajax/save/"}, method=RequestMethod.POST)
	public ModelAndView saveAthleteTariff(HttpServletRequest req,
										   HttpServletResponse res,
										   @ModelAttribute("athleteTariff") AthleteTariff athleteTariff,
										   BindingResult result,
										   @PathVariable("idd") int athleteId) {
		if (validate(result, athleteTariff)) {
			if (athleteTariff.getId() > 0) {
				AthleteTariff exist = paymentService.findById(new AthleteTariff(athleteTariff.getId()));

				exist.setTariff(athleteTariff.getTariff());
				exist.setCoupon(athleteTariff.getCoupon());

				athleteTariff = exist;
			}

			AthleteTariff saved = paymentService.save(athleteTariff);

			ajaxUpdate(req, res, "athleteTariffList");
			ajaxUpdate(req, res, "athleteTariffForm");

			return getModel(athleteId, null);
		} else {
			ajaxUpdate(req, res, "athleteTariffForm");

			return getModel(athleteId, athleteTariff);
		}
	}
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
        	personService.remove(new Athlete(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    @RequestMapping(value="/edit/{id}/ajax/", method=RequestMethod.POST)
	public ModelAndView onAjax(HttpServletRequest req, 
								HttpServletResponse res, 
								@PathVariable Map<String, String> pathVariables) {
		String action = req.getParameter("action");

		int id = TextUtils.getIntValue(pathVariables.get("id"));
		if (id != 0) {
			if ("refresh-crop-image".equals(action)) {
				ajaxUpdate(req, res, "cropImagePanel");
				ajaxUpdate(req, res, "cropImagePanelSmall");
				ajaxUpdate(req, res, "cropImagePanel" + req.getParameter("type"));
			} else if ("refresh-athlete-tariff-form".equals(action)) {
				int athleteTariffId = TextUtils.getIntValue(req.getParameter("athleteTariffId"));

				ajaxUpdate(req, res, "athleteTariffForm");

				return getModel(id, paymentService.findById(new AthleteTariff(athleteTariffId)));
			} else if ("remove-athlete-tariff".equals(action)) {
				int athleteTariffId = TextUtils.getIntValue(req.getParameter("athleteTariffId"));
				AthleteTariff athleteTariff = paymentService.findById(new AthleteTariff(athleteTariffId));
				paymentService.remove(athleteTariff);

				ajaxUpdate(req, res, "athleteTariffList");

				return getModel(id, null);
			}
		}

		return getModel(id, null);
	}
    
    private ModelAndView getModel(int id, AthleteTariff athleteTariff) {
        ModelBuilder model = new ModelBuilder("editor/editAthlete");
        Athlete athlete;
        
        if(id == 0) {
        	athlete = new Athlete();
        } else {
        	athlete = personService.findById(new Athlete(id));
			athlete.getPerson().setPassword(null);
        	
    		model.put("athleteImage", FileDataSource.getImage(projectConfiguration, athlete, ImageFormat.ATHLETE_MIDDLE));

			if (athleteTariff == null) {
				athleteTariff = new AthleteTariff();
				athleteTariff.setAthlete(athlete);
			}
			model.put("athleteTariff", athleteTariff);

			AthleteTariffFilter filter = new AthleteTariffFilter();
			filter.setAthlete(athlete);
			filter.addOrder(new Order("id", OrderDirection.Asc));
			model.put("athleteTariffs", paymentService.findAthleteTariffs(filter));
        }
        model.put("athlete", athlete);

		return model;
    }
    
    private boolean validate(BindingResult result, Athlete athlete) {
		Person person = athlete.getPerson();
		if (person == null || StringUtils.isBlank(person.getLogin())) {
			result.rejectValue("person.login", "field.required", FIELD_REQUIRED);
		} else {
			Person saved = personService.findByLogin(person);
			if(saved != null && person.getId() != saved.getId()) {
				result.rejectValue("person.login", "field.required", "Пользователь с таким login-ом уже существует");
			}
		}
		if (athlete.getId() == 0 && StringUtils.isBlank(person.getPassword())) {
			result.rejectValue("person.password", "field.required", FIELD_REQUIRED);
		}
    	if (StringUtils.isBlank(athlete.getFirstName())) {
    		result.rejectValue("firstName", "field.required", FIELD_REQUIRED);
    	}
    	
        return !result.hasErrors();
    }

	private boolean validate(BindingResult result, AthleteTariff athleteTariff) {
		if (athleteTariff.getAthlete() == null) {
			result.rejectValue("athlete", "field.required", FIELD_REQUIRED);
		}
		if (athleteTariff.getTariff() == null) {
			result.rejectValue("tariff", "field.required", FIELD_REQUIRED);
		}

		return !result.hasErrors();
	}
    
}
