package ru.strela.web.controller.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.core.AbstractController;
import ru.strela.model.*;
import ru.strela.model.auth.Person;
import ru.strela.model.filter.BannerImageFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Aisen on 27.04.2016.
 */
public abstract class WebController extends AbstractController {

    protected void fillMetaInf(BaseEntitySeo entity, ModelAndView model) {
        if (StringUtils.isNotBlank(entity.getHtmlTitle())) {
            model.addObject("htmlTitle", entity.getHtmlTitle());
        } else if (StringUtils.isNotBlank(entity.getName())) {
            model.addObject("htmlTitle", entity.getName());
        }
        if (StringUtils.isNotBlank(entity.getMetaDescription())) {
            model.addObject("htmlDescription", entity.getMetaDescription());
        }
        if (StringUtils.isNotBlank(entity.getMetaKeywords())) {
            model.addObject("htmlKeywords", entity.getMetaKeywords());
        }
    }

    protected void fillMetaInf(String title, String description, String keywords, ModelAndView model) {
        if (StringUtils.isNotBlank(title)) {
            model.addObject("htmlTitle", title);
        }
        if (StringUtils.isNotBlank(description)) {
            model.addObject("htmlDescription", description);
        }
        if (StringUtils.isNotBlank(keywords)) {
            model.addObject("htmlKeywords", keywords);
        }
    }

    @ModelAttribute("settings")
    protected Settings settings() {
        return applicationService.getSettings();
    }

    @Override
    @ModelAttribute("currentHref")
    public String currentHref(HttpServletRequest req) {
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            buf.append(buf.length() == 0 ? "?" : "&");
            buf.append(entry.getKey() + "=" + entry.getValue()[0]);
        }
        return req.getRequestURI() + buf.toString();
    }

    @ModelAttribute("year")
    protected int year() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    @ModelAttribute("citiesHasGym")
    protected List<City> citiesHasGym() {
        return applicationService.findHasGym();
    }

    @ModelAttribute("currentAthlete")
    protected Athlete currentAthlete() {
        Person currentPerson = personServer.getCurrentPerson();
        if (currentPerson != null) {
            return personService.findByPerson(currentPerson);
        }

        return null;
    }

    protected void fillBanner(ModelBuilder model) {
        model.put("showBanner", true);
        BannerImageFilter bannerImageFilter = new BannerImageFilter();
        bannerImageFilter.setVisible(true);
        bannerImageFilter.addOrder(new Order("position", OrderDirection.Asc));
        for (BannerImage bannerImage : applicationService.findBannerImages(bannerImageFilter)) {
            ModelBuilder item = model.createCollection("bannerImageList");
            item.put("bannerImage", bannerImage);
            item.put("image", FileDataSource.getImage(projectConfiguration, bannerImage, ImageFormat.BANNER_IMAGE));
        }
    }

}
