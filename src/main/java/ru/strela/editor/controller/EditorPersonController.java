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
import ru.strela.model.auth.Person;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.PersonFilter;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.Map;

@Controller
@RequestMapping("/editor/person")
public class EditorPersonController extends EditorController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") PersonFilter filter,
                             @PathVariable Map<String, String> pathVariables) {
    	ModelBuilder model = new ModelBuilder("editor/persons");
        if(filter == null) {        	
        	filter = new PersonFilter();
        }
        filter.addOrder(new Order("id", OrderDirection.Asc));
        Page<Person> page = personService.find(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Person person, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, person)) {
            if(person.getId() != 0) {
            	Person saved = personService.findById(new Person(person.getId()));
            	
    			saved.setLogin(person.getLogin());
    			saved.setAdmin(person.isAdmin());
                saved.setRoot(person.isRoot());
    			saved.setDisabled(person.isDisabled());
    			if(StringUtils.isNotBlank(person.getPassword())) {
    				saved.setPassword(passwordEncoder.encode(person.getPassword()));
    			}

        		person = saved;
            } else {
            	person.setPassword(passwordEncoder.encode(person.getPassword()));
            }
            
            person = personService.save(person);          
            
            return new Redirect("/editor/person/edit/" + person.getId() + "/");
        }

        return new ModelAndView("editor/editPerson");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
        	personService.remove(new Person(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editPerson");
        Person person;
        
        if(id == 0) {
        	person = new Person();
        } else {
        	person = personService.findById(new Person(id));
        	person.setPassword(null);
        }
        model.put("person", person);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Person person) {
    	if(StringUtils.isBlank(person.getLogin())) {
    		result.rejectValue("login", "field.required", FIELD_REQUIRED);
    	} else {
    		Person saved = personService.findByLogin(person);
    		if(saved != null && person.getId() != saved.getId()) {
    			result.rejectValue("login", "field.required", "Пользователь с таким login-ом уже существует");
    		}
    	}
    	if(person.getId() == 0) {
    		if(StringUtils.isBlank(person.getPassword())) {
    			result.rejectValue("password", "field.required", FIELD_REQUIRED);
    		}
    	}
        
        return !result.hasErrors();
    }
}
