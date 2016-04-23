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
import ru.strela.model.Country;
import ru.strela.model.filter.CountryFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

@Controller
@RequestMapping("/editor/country")
public class EditorCountryController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") CountryFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/countries");
        if(filter == null) {        	
        	filter = new CountryFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Country> page = applicationService.findCountries(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Country country, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, country)) {
            if(country.getId() != 0) {
            	Country saved = applicationService.findById(new Country(country.getId()));
            	
    			saved.setName(country.getName());

        		country = saved;
            }         
            
            country = applicationService.save(country);          
            
            return new Redirect("/editor/country/edit/" + country.getId() + "/");
        }

        return new ModelAndView("editor/editCountry");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new Country(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editCountry");
        Country country;
        
        if(id == 0) {
        	country = new Country();
        } else {
        	country = applicationService.findById(new Country(id));        	
        }
        model.put("country", country);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Country country) {
    	if(StringUtils.isBlank(country.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
        
        return !result.hasErrors();
    }
}
