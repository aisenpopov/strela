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
                             @ModelAttribute("filter") AthleteFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
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
    	return getModel(TextUtils.getIntValue(pathVariables.get("id")));
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Athlete athlete, BindingResult result, @PathVariable Map<String, String> pathVariables) {
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
            	Athlete saved = personService.findById(new Athlete(athlete.getId()));

            	saved.setFirstName(athlete.getFirstName());
            	saved.setLastName(athlete.getLastName());
            	saved.setMiddleName(athlete.getMiddleName());
            	saved.setNickName(athlete.getNickName());
            	saved.setSex(athlete.getSex());
            	saved.setBirthday(athlete.getBirthday());
            	saved.setStartDate(athlete.getStartDate());
            	saved.setWeight(athlete.getWeight());
            	saved.setHeight(athlete.getHeight());
            	saved.setGiSize(athlete.getGiSize());
            	saved.setRashguardSize(athlete.getRashguardSize());
            	saved.setPassportNumber(athlete.getPassportNumber());
            	saved.setInstructor(athlete.isInstructor());
            	saved.setRetired(athlete.isRetired());

            	saved.setRegistrationRegion(athlete.getRegistrationRegion());
				saved.setTeam(athlete.getTeam());
            	
            	saved.setEmail(athlete.getEmail());
            	saved.setPhoneNumber(athlete.getPhoneNumber());
            	saved.setMobileNumber(athlete.getMobileNumber());
            	saved.setVk(athlete.getVk());
            	saved.setFacebook(athlete.getFacebook());
            	saved.setInstagram(athlete.getInstagram());
            	saved.setSkype(athlete.getSkype());
            	saved.setComment(athlete.getComment());

        		athlete = saved;
            }

			Person savedPerson = personService.save(person);
			athlete.setPerson(savedPerson);
            athlete = personService.save(athlete);          
            
            return new Redirect("/editor/athlete/edit/" + athlete.getId() + "/");
        }

        return new ModelAndView("editor/editAthlete");
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
		if("refrash-crop-image".equals(action) && id != 0) {
			ajaxUpdate(req, res, "cropImagePanel");
			ajaxUpdate(req, res, "cropImagePanelSmall");
			ajaxUpdate(req, res, "cropImagePanel" + req.getParameter("type"));
		}
		
		return getModel(id);
	}
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editAthlete");
        Athlete athlete;
        
        if(id == 0) {
        	athlete = new Athlete();
        } else {
        	athlete = personService.findById(new Athlete(id));
			athlete.getPerson().setPassword(null);
        	
    		model.put("athleteImage", FileDataSource.getImage(projectConfiguration, athlete, ImageFormat.ATHLETE_MIDDLE));
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
    
}
