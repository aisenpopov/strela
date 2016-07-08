package ru.strela.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.web.controller.core.WebController;

@Controller
@RequestMapping(value = {"/"})
public class MainController extends WebController {

    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView main() {
        ModelBuilder model = new ModelBuilder("main");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.addOrder(new Order("publish", OrderDirection.Desc));
        for (Article article : applicationService.findArticles(articleFilter, 0, 3)) {
            ModelBuilder newsItem = model.createCollection("newsList");
            newsItem.put("news", article);
            newsItem.put("image", FileDataSource.getImage(projectConfiguration, article, ImageFormat.NEWS_PREVIEW));
        }

        fillBanner(model);

        return model;
    }

}
