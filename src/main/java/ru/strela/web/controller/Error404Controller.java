package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping(value = "/error404")
public class Error404Controller extends WebController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage() {
        return new ModelAndView("error404");
    }

}
