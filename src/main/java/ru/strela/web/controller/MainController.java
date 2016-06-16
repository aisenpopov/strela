package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Article;
import ru.strela.model.BannerImage;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.BannerImageFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.web.controller.core.BaseController;

@Controller
@RequestMapping(value = {"/"})
public class MainController extends BaseController {

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView main() {
        ModelBuilder model = new ModelBuilder("main");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.addOrder(new Order("publish", OrderDirection.Desc));
        for (Article article : applicationService.findArticles(articleFilter, 0, 3)) {
            ModelBuilder image = model.createCollection("newsList");
            image.put("news", article);
            image.put("image", FileDataSource.getImage(projectConfiguration, article, ImageFormat.NEWS_PREVIEW));
        }

        model.addObject("showBanner", true);
        BannerImageFilter bannerImageFilter = new BannerImageFilter();
        bannerImageFilter.setVisible(true);
        bannerImageFilter.addOrder(new Order("position", OrderDirection.Asc));
        for (BannerImage bannerImage : applicationService.findBannerImages(bannerImageFilter)) {
            ModelBuilder item = model.createCollection("bannerImageList");
            item.put("bannerImage", bannerImage);
            item.put("image", FileDataSource.getImage(projectConfiguration, bannerImage, ImageFormat.BANNER_IMAGE));
        }

        return model;
    }

}
