package ru.strela.editor.controller.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.strela.core.AbstractController;
import ru.strela.editor.menu.EditorMenuBuilder;
import ru.strela.editor.menu.MenuItem;
import ru.strela.model.auth.Person;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class EditorController extends AbstractController {
	
	@Autowired
	private EditorMenuBuilder editorMenuBuilder;
	
    @ModelAttribute("menu")
    public List<MenuItem> menu() {
		Person currentPerson = getCurrentPerson();
        return currentPerson != null ? editorMenuBuilder.build(currentPerson.isAdmin()).getItems() : null;
    }

	private Person getCurrentPerson() {
		return personServer.getCurrentPerson();
	}

    @ModelAttribute("activeMenu")
    public MenuItem activeMenu(HttpServletRequest req) {
    	String currentHref = currentHref(req);
		List<MenuItem> menuItems = menu();
		if (menuItems != null) {
			for(MenuItem item : menuItems) {
				if(currentHref.contains(item.getHref())) {
					return item;
				}
				for(MenuItem subItem : item.getItems()) {
					if(currentHref.contains(subItem.getHref())) {
						return subItem;
					}
				}
			}
		}
    	return null;
    }

}