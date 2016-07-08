package ru.strela.web.controller;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.strela.model.Article;
import ru.strela.model.filter.ArticleFilter;
import ru.strela.model.filter.Order;
import ru.strela.model.filter.OrderDirection;
import ru.strela.util.ModelBuilder;
import ru.strela.util.ResourceNotFoundException;
import ru.strela.util.image.FileDataSource;
import ru.strela.util.image.ImageFormat;
import ru.strela.util.processor.ArticlePrepareProcessor;
import ru.strela.web.controller.core.WebController;

/**
 * Created by Aisen on 27.04.2016.
 */
@Controller
@RequestMapping(value = {"/news"})
public class NewsController extends WebController {

    @RequestMapping("/")
    public ModelAndView list(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "size", required = false, defaultValue = "6") int pageSize) {
        ModelBuilder model = new ModelBuilder("newsList");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.addOrder(new Order("publish", OrderDirection.Desc));
        Page<Article> page = applicationService.findArticles(articleFilter, pageNumber - 1, pageSize);
        model.put("page", page);
        for (Article article : page) {
            ModelBuilder newsItem = model.createCollection("newsList");
            newsItem.put("news", article);
            newsItem.put("image", FileDataSource.getImage(projectConfiguration, article, ImageFormat.NEWS_PREVIEW));
        }

        fillBanner(model);

        return model;
    }

    @RequestMapping(value = "/{path}/", method = {RequestMethod.GET})
    public ModelAndView getNews(@PathVariable String path) {
        ModelBuilder model = new ModelBuilder("news");

        ArticleFilter articleFilter = new ArticleFilter();
        articleFilter.setType(Article.Type.news);
        articleFilter.setVisible(true);
        articleFilter.setPath(path);
        Article news = applicationService.findOneArticle(articleFilter);
        if (news != null) {
            model.put("news", news);

            ArticlePrepareProcessor prepareProcessor = new ArticlePrepareProcessor(applicationService, projectConfiguration, news);
            String text = prepareProcessor.process(news.getText());
            model.put("text", text);
        } else {
            throw new ResourceNotFoundException();
        }

        return model;
    }

}
