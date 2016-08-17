package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.BannerImage;
import ru.strela.model.City;
import ru.strela.model.filter.BannerImageFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ajax.JsonData;
import ru.strela.util.ajax.JsonResponse;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping(value = {"/"})
public class MainController extends WebController {

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView main() {
        return new ModelBuilder("main");
    }

    @ResponseBody
    @RequestMapping(value = "/city/hasGym", method = RequestMethod.POST)
    public JsonResponse getCitiesHasGym() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        for (City city : applicationService.findHasGym()) {
            JsonData jsonData = data.createCollection("citiesHasGym");
            jsonData.put("id", city.getId());
            jsonData.put("name", city.getName());
        }

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/banner", method = RequestMethod.POST)
    public JsonResponse getBanner() {
        JsonResponse response = new JsonResponse();
        JsonData data = response.createJsonData();

        BannerImageFilter bannerImageFilter = new BannerImageFilter();
        bannerImageFilter.setVisible(true);
        bannerImageFilter.addOrder(new Order("position", OrderDirection.Asc));
        for (BannerImage bannerImage : applicationService.findBannerImages(bannerImageFilter)) {
            JsonData jsonData = data.createCollection("bannerList");
            jsonData.put("name", bannerImage.getName());
            jsonData.put("type", bannerImage.getType().name());
            jsonData.put("link", bannerImage.getLink());
            jsonData.put("image", FileDataSource.getImage(projectConfiguration, bannerImage, ImageFormat.BANNER_IMAGE));
        }

        return response;
    }

}
