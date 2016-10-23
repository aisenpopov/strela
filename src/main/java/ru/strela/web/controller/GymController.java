package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.strela.model.Article;
import ru.strela.model.Athlete;
import ru.strela.model.City;
import ru.strela.model.Gym;
import ru.strela.model.filter.GymFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ResourceNotFoundException;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.WebController;

import java.util.List;

@Controller
@RequestMapping(value = {"/gym"})
public class GymController extends WebController {

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public JsonResponse getGym(@RequestParam(value = "id", required = true) Integer id) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        Gym gym = applicationService.findById(new Gym(id));
        if (gym != null) {
            data.put("name", gym.getName());
            data.put("latitude", gym.getLatitude());
            data.put("longitude", gym.getLongitude());

            Article article = gym.getArticle();
            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, article);
            String text = prepareProcessor.process(article.getText());
            data.put("text", text);

            for (Athlete instructor : gym.getInstructors()) {
                JsonData item = data.createCollection("instructors");
                item.put("displayName", instructor.getDisplayName());
            }
        } else {
            throw new ResourceNotFoundException();
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JsonResponse getGymList(@RequestParam(required = false) Integer cityId) {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        City city = null;
        if (cityId != null) {
            city = applicationService.findById(new City(cityId));
        }

        GymFilter gymFilter = new GymFilter();
        gymFilter.setCity(city);
        gymFilter.addOrder(new Order("id", OrderDirection.Asc));
        List<Gym> gyms = applicationService.findGyms(gymFilter, false);
        for (Gym gym : gyms) {
            JsonData jsonData = data.createCollection("gyms");
            jsonData.put("id", gym.getId());
            jsonData.put("name", gym.getName());
            jsonData.put("address", gym.getAddress());
            jsonData.put("image", FileDataSource.getImage(projectConfiguration, gym, ImageFormat.GYM_PREVIEW));
        }

        return response;
    }

}
