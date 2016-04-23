package ru.strela.editor.controller.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.strela.model.auth.Person;

@Controller
@RequestMapping("/editor")
public class EditorMainController extends EditorController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String getMainPage() {
		Person person = personServer.getCurrentUser();
		if(person == null || !person.isAdmin()) {
			return "redirect:/editor/login";
		}
		return "editor/main";
	}

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "editor/login";
    }

}
