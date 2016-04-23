package ru.strela.editor.controller;

import java.util.Map;

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
import ru.strela.model.RegistrationRegion;
import ru.strela.model.filter.BaseFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

@Controller
@RequestMapping("/editor/registration_region")
public class EditorRegistrationRegionController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") BaseFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/registrationRegions");
        if(filter == null) {        	
        	filter = new BaseFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<RegistrationRegion> page = applicationService.findRegistrationRegions(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(RegistrationRegion registrationRegion, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, registrationRegion)) {
            if(registrationRegion.getId() != 0) {
            	RegistrationRegion saved = applicationService.findById(new RegistrationRegion(registrationRegion.getId()));
            	
    			saved.setName(registrationRegion.getName());

        		registrationRegion = saved;
            }         
            
            registrationRegion = applicationService.save(registrationRegion);          
            
            return new Redirect("/editor/registration_region/edit/" + registrationRegion.getId() + "/");
        }

        return new ModelAndView("editor/editRegistrationRegion");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new RegistrationRegion(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editRegistrationRegion");
        RegistrationRegion registrationRegion;
        
        if(id == 0) {
        	registrationRegion = new RegistrationRegion();
        } else {
        	registrationRegion = applicationService.findById(new RegistrationRegion(id));        	
        }
        model.put("registrationRegion", registrationRegion);
             
		return model;
    }
    
    private boolean validate(BindingResult result, RegistrationRegion registrationRegion) {
    	if(StringUtils.isBlank(registrationRegion.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
        
        return !result.hasErrors();
    }
}
