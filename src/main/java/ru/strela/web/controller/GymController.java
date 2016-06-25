package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Article;
import ru.strela.model.City;
import ru.strela.model.Gym;
import ru.strela.model.filter.GymFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ResourceNotFoundException;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.BaseController;

import java.util.List;

/**
 * Created by Aisen on 27.04.2016.
 */
@Controller
@RequestMapping(value = {"/gym"})
public class GymController extends BaseController {

    @RequestMapping(value = "/", method = {RequestMethod.GET})
    public ModelAndView getGyms(@RequestParam(required = false) Integer cityId) {
        ModelBuilder model = new ModelBuilder("gyms");

        City city = null;
        if (cityId != null) {
            city = applicationService.findById(new City(cityId));
        }
        model.put("cityName", city != null ? city.getName() : "Залы");

        GymFilter gymFilter = new GymFilter();
        gymFilter.setCity(city);
        gymFilter.addOrder(new Order("id", OrderDirection.Asc));
        List<Gym> gyms = applicationService.findGyms(gymFilter);
        for (Gym gym : gyms) {
            ModelBuilder gymItem = model.createCollection("gyms");
            gymItem.put("gym", gym);
            gymItem.put("image", FileDataSource.getImage(projectConfiguration, gym, ImageFormat.GYM_PREVIEW));
        }

        fillBanner(model);

        return model;
    }

    @RequestMapping(value = "/{id}/", method = {RequestMethod.GET})
    public ModelAndView getGym(@PathVariable Integer id) {
        ModelBuilder model = new ModelBuilder("gym");

        Gym gym = applicationService.findById(new Gym(id));
        if (gym != null) {
            model.put("gym", gym);

            Article article = gym.getArticle();
            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, article);
            String text = prepareProcessor.process(article.getText());
            model.put("text", text);
        } else {
            throw new ResourceNotFoundException();
        }

        return model;
    }

}
