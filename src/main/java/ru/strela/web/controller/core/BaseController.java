package ru.strela.web.controller.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.config.ProjectConfiguration;
import ru.strela.model.BannerImage;
import ru.strela.model.BaseEntitySeo;
import ru.strela.model.City;
import ru.strela.model.Settings;
import ru.strela.model.filter.BannerImageFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.service.ApplicationService;
import ru.strela.service.PersonServer;
import ru.strela.service.PersonService;
import ru.strela.util.ModelBuilder;
import ru.strela.util.PagerUtils;
import ru.strela.util.ajax.AjaxUpdater;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by Aisen on 27.04.2016.
 */
public abstract class BaseController extends AjaxUpdater {

    @Autowired
    protected PersonService personService;

    @Autowired
    protected PersonServer personServer;

    @Autowired
    protected ApplicationService applicationService;

    @Autowired
    protected ProjectConfiguration projectConfiguration;

    @ModelAttribute("pagerPath")
    private String getPagerPath(HttpServletRequest request) {
        return PagerUtils.getPagerPath(request);
    }

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
    protected Settings settings(HttpServletRequest request) {
        return applicationService.getSettings();
    }

    @ModelAttribute("currentHref")
    protected String currentHref(HttpServletRequest req) {
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, String[]> entry : req.getParameterMap().entrySet()) {
            buf.append(buf.length() == 0 ? "?" : "&");
            buf.append(entry.getKey() + "=" + entry.getValue()[0]);
        }
        return req.getRequestURI() + buf.toString();
    }

    @ModelAttribute("year")
    protected int year(HttpServletRequest request) {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    @ModelAttribute("citiesHasGym")
    protected List<City> citiesHasGym(HttpServletRequest request) {
        return applicationService.findHasGym();
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
