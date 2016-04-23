package ru.strela.editor.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Athlete;
import ru.strela.model.filter.AthleteFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;

@Controller
@RequestMapping("/editor/athlete")
public class EditorAthleteController extends EditorController {
	
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") AthleteFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/athletes");
        if(filter == null) {        	
        	filter = new AthleteFilter();
        }
        filter.addOrder(new Order("id", OrderDirection.Asc));
        Page<Athlete> page = personService.findAthletes(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Athlete athlete, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, athlete)) {
            if(athlete.getId() != 0) {
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
            	saved.setPerson(athlete.getPerson());
            	
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
        	
    		model.put("athleteImage", FileDataSource.getImage(projectConfiguration, athlete, ImageFormat.ATHLETE_MIDDLE));
        }
        model.put("athlete", athlete);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Athlete athlete) {
    	if(StringUtils.isBlank(athlete.getFirstName())) {
    		result.rejectValue("firstName", "field.required", FIELD_REQUIRED);
    	}
    	
        return !result.hasErrors();
    }
    
}
