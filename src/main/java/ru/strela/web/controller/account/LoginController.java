package ru.strela.web.controller.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.util.ModelBuilder;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping("/account/login")
public class LoginController extends WebController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage() {
        ModelBuilder model = new ModelBuilder("account/login");

        return model;
    }

}
