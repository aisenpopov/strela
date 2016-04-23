package ru.strela.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class Redirect extends ModelAndView {

    public Redirect(String url) {
        super(new RedirectView(url, true, true, false));
    }
}
