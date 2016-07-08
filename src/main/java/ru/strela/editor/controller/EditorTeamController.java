package ru.strela.editor.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Team;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.model.filter.TeamFilter;
import ru.strela.util.ModelBuilder;
import ru.strela.util.Redirect;
import ru.strela.util.TextUtils;
import ru.strela.util.ajax.JsonResponse;

import java.util.Map;

@Controller
@RequestMapping("/editor/team")
public class EditorTeamController extends EditorController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "50") int pageSize,
                             @ModelAttribute("filter") TeamFilter filter) {
    	ModelBuilder model = new ModelBuilder("editor/teams");
        if(filter == null) {        	
        	filter = new TeamFilter();
        }
        filter.addOrder(new Order("name", OrderDirection.Asc));
        Page<Team> page = applicationService.findTeams(filter, pageNumber - 1, pageSize);
        model.put("page", page);
        
        return model;
    }

    @RequestMapping(value = {"/edit/{id}", "/edit"}, method = RequestMethod.GET)
    public ModelAndView get(@PathVariable Map<String, String> pathVariables) {
    	ModelAndView modelAndView = getModel(TextUtils.getIntValue(pathVariables.get("id")));
        
        return modelAndView;
    }
    
    @RequestMapping(value = {"/edit", "/edit/{id}"}, method = RequestMethod.POST)
    public ModelAndView save(Team team, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(result, team)) {
            if(team.getId() != 0) {
            	Team saved = applicationService.findById(new Team(team.getId()));
            	
    			saved.setName(team.getName());
    			saved.setCity(team.getCity());
    			saved.setChiefInstructor(team.getChiefInstructor());

        		team = saved;
            }         
            
            team = applicationService.save(team);          
            
            return new Redirect("/editor/team/edit/" + team.getId() + "/");
        }

        return new ModelAndView("editor/editTeam");
    }
    
    @RequestMapping(value = "/remove/{id}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse remove(@PathVariable("id") int id) {
        JsonResponse response = new JsonResponse();
        try {
            applicationService.remove(new Team(id));
        } catch(Exception e) {
            response.setStatus("error");
        }
        return response;
    }
    
    private ModelAndView getModel(int id) {
        ModelBuilder model = new ModelBuilder("editor/editTeam");
        Team team;
        
        if(id == 0) {
        	team = new Team();
        } else {
        	team = applicationService.findById(new Team(id));        	
        }
        model.put("team", team);
             
		return model;
    }
    
    private boolean validate(BindingResult result, Team team) {
    	if(StringUtils.isBlank(team.getName())) {
    		result.rejectValue("name", "field.required", FIELD_REQUIRED);
    	}
    	if(team.getCity() == null) {
    		result.rejectValue("city", "field.required", FIELD_REQUIRED);
    	}
    	if(team.getChiefInstructor() == null) {
    		result.rejectValue("chiefInstructor", "field.required", FIELD_REQUIRED);
    	}
        
        return !result.hasErrors();
    }
}
