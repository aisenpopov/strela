package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.City;
import ru.strela.model.filter.CityFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.Map;

@Controller
@RequestMapping("/editor/city")
public class EditorCityController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") CityFilter filter) {
    	ModelBuilder model = new ModelBuilder("editor/cities");
        if(filter == null) {        	
        	filter = new CityFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<City> page = applicationService.findCities(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        model.addTableColumn("Страна", "country");
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(City city, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, city)) {
            if(city.getId() != 0) {
            	City saved = applicationService.findById(new City(city.getId()));
            	
    			saved.setName(city.getName());
    			saved.setCountry(city.getCountry());

        		city = saved;
            }         
            
            city = applicationService.save(city);          
            
            return new Redirect("/editor/city/edit/" + city.getId() + "/");
        }

        return new ModelAndView("editor/editCity");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new City(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editCity");
        City city;
        
        if(id == 0) {
        	city = new City();
        } else {
        	city = applicationService.findById(new City(id));        	
        }
        model.put("city", city);
             
		return model;
    }
    
    private boolean validate(BindingResult result, City city) {
    	if(StringUtils.isBlank(city.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
    	if(city.getCountry() == null) {
    		result.rejectValue("country", "field.required", FIELD_REQUIRED);
    	}
        
        return !result.hasErrors();
    }
}
