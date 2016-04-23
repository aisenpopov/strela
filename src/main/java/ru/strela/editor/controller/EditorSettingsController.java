package ru.strela.editor.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.strela.editor.controller.core.EditorController;
import ru.strela.model.Settings;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ValidateUtils;

@Controller
@RequestMapping("/editor/settings")
public class EditorSettingsController extends EditorController {

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public ModelAndView edit(@PathVariable Map<String, String> pathVariables) {
        return getModel();
    }
    
    @RequestMapping(value = {"/"}, method = RequestMethod.POST)
    public ModelAndView save(Settings settings, BindingResult result, @PathVariable Map<String, String> pathVariables) {
    	if(validate(settings, result)) {
        	Settings saved = applicationService.getSettings();
        	saved.setEmail(settings.getEmail());
        	
        	applicationService.save(saved);
        }         

        return new ModelAndView("editor/editSettings");
    }
    
    private boolean validate(Settings settings, BindingResult result) {
    	String emailError = ValidateUtils.checkEmail(settings.getEmail());
    	if(StringUtils.isNotBlank(emailError)) {
    		result.rejectValue("email", "field.required", emailError);
    	}
    	
    	return !result.hasErrors();
    }

    @RequestMapping(value={"/ajax"}, method=RequestMethod.POST)
	public ModelAndView onAjax(HttpServletRequest req, HttpServletResponse res,
								@PathVariable Map<String, String> pathVariables) {
		return getModel();
	}
    
    private ModelAndView getModel() {
    	ModelBuilder model = new ModelBuilder("editor/editSettings");
    	Settings settings = applicationService.getSettings();
    	model.put("settings", settings);
    	
    	return model;
    }
}
