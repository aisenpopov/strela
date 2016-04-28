package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.util.ModelBuilder;
import ru.strela.web.controller.core.BaseController;

/**
 * Created by Aisen on 27.04.2016.
 */
@Controller
@RequestMapping(value = {"/"})
public class MainController extends BaseController {

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView main() {
        ModelBuilder model = new ModelBuilder("main");

        return model;
    }

}
